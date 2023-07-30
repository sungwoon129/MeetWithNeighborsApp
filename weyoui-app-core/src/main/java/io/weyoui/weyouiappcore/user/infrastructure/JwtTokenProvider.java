package io.weyoui.weyouiappcore.user.infrastructure;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.weyoui.weyouiappcore.user.command.domain.RoleType;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.infrastructure.dto.UserSession;
import io.weyoui.weyouiappcore.user.query.application.dto.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private final SecretKey secretKey;
    private long accessTokenExpireTime;
    private long refreshTokenExpireTime;
    private static final String BEARER_TYPE = "Bearer";

    public JwtTokenProvider(@Value("${jwt.token.secret-key}")String key
            , @Value("${jwt.token.expiration-in-access-token}") final long accessTokenExpireTime
            , @Value("${jwt.token.expiration-in-refresh-token}") final long refreshTokenExpireTime) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    public UserResponse.Token generateToken(Authentication authentication, UserId userId) {

        String authorities = getAuthorities(authentication);

        Date now = new Date();

        String accessToken = generateAccessToken(authentication, now, authorities, userId);

        String refreshToken = generateRefreshToken(authentication, now, authorities, userId);

        return UserResponse.Token.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(refreshTokenExpireTime)
                .build();

    }

    private String generateRefreshToken(Authentication authentication, Date now, String authorities, UserId userId) {
        // Refresh Token 생성
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("id",userId.getId())
                .setExpiration(new Date(now.getTime() + refreshTokenExpireTime))
                .setIssuedAt(now)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateAccessToken(Authentication authentication, Date now, String authorities, UserId userId) {
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now.getTime() + accessTokenExpireTime);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("id",userId.getId())
                .setExpiration(accessTokenExpiresIn)
                .setIssuedAt(now)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public UserResponse.Token reissue(String refreshToken, UserId userId) {

        Authentication authentication = getAuthentication(refreshToken);
        Date now = new Date();
        String authorities = getAuthorities(authentication);

        String newAccessToken = generateAccessToken(authentication,now,authorities, userId);

        return UserResponse.Token.builder()
                .grantType(BEARER_TYPE)
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(refreshTokenExpireTime)
                .build();

    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        RoleType role = getFirstRoleType(claims);

        UserDetails userSession = UserSession.builder()
                .email(claims.getSubject())
                .role(role)
                .build();

        return new UsernamePasswordAuthenticationToken(userSession.getUsername(), "", userSession.getAuthorities());
    }

    private RoleType getFirstRoleType(Claims claims) {
        // 클레임에서 권한 정보 가져오기
        RoleType role = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(RoleType::findByName)
                .findFirst()
                .orElse(null);

        return role;
    }

    private String getAuthorities(Authentication authentication) {
        // 권한 가져오기
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }

    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        }  catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }

        return true;

    }

    public Long getExpiration(String accessToken) {

        Date expiration = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody().getExpiration();

        long now = new Date().getTime();
        return (expiration.getTime() - now);
    }


    public String getUserEmailByToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public UserId getUserIdByToken(String token) {
        Claims claims = parseClaims(token);
        String userid = claims.get("id").toString();

        if(userid == null || userid.trim().isEmpty()) throw new SecurityException("Invalid JWT Token");

        return new UserId(claims.get("id").toString());
    }
}
