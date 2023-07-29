package io.weyoui.weyouiappcore.config.app_config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.weyoui.weyouiappcore.user.infrastructure.JwtTokenProvider;
import io.weyoui.weyouiappcore.user.presentation.dto.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = JwtTokenProvider.class)
class JwtTokenProviderTest {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;              // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;    // 7일

    @Autowired
    private JwtTokenProvider provider;
    @Value("${jwt.token.secret-key}")
    private String key;
    private SecretKey secretKey;

    @BeforeEach
    void setup() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Test
    void generateToken() {
        //given
        String email = "jwttester@weyoui.com";
        String password = "123456";
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,password);

        //when
        UserResponse.Token responseToken = provider.generateToken(token);

        //then
        JwtParser parser = Jwts.parserBuilder().setSigningKey(secretKey).build();
        Jws<Claims> accessClaimsJws = parser.parseClaimsJws(responseToken.getAccessToken());
        Jws<Claims> refreshClaimsJws = parser.parseClaimsJws(responseToken.getRefreshToken());

        assertThat(accessClaimsJws.getBody().getSubject()).isEqualTo(email);
        assertTrue(accessClaimsJws.getBody().getExpiration().after(new Date()));
        assertTrue(refreshClaimsJws.getBody().getExpiration().after(new Date()));

    }

    @Test
    void getAuthentication() {
        //given
        String email = "jwttester@weyoui.com";
        String password = "123456";
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,password);

        String accessToken = Jwts.builder()
                .setSubject(token.getName())
                .claim("auth", "USER")
                .setExpiration(new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .setIssuedAt(new Date())
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        //when
        Authentication authentication = provider.getAuthentication(accessToken);

        //then
        assertTrue(authentication.isAuthenticated());
        assertThat(authentication.getName()).isEqualTo(email);
        assertThat(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","))).isEqualTo("USER");

    }

    @DisplayName("유효하지 않은 비밀키로 토큰을 생성할 경우, 사인 예외를 발생시킨다.")
    @Test
    void testInvalidSecretKey() {
        //given
        String invalidKey = "invalidSecreKeyinvalidSecreKeyinvalidSecreKeyinvalidSecreKeyinvalidSecreKey";
        byte[] keyBytes = Decoders.BASE64.decode(invalidKey);
        SecretKey invalidSecretKey = Keys.hmacShaKeyFor(keyBytes);
        String email = "jwttester@weyoui.com";
        String password = "123456";
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,password);

        String accessToken = Jwts.builder()
                .setSubject(token.getName())
                .claim("auth", "USER")
                .setExpiration(new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .setIssuedAt(new Date())
                .signWith(invalidSecretKey, SignatureAlgorithm.HS256)
                .compact();

        //when,then
        assertThrows(SecurityException.class, () ->{
            provider.validateToken(accessToken);
        });
    }

    @DisplayName("유효시간이 지난 토큰으로 요청을 하면 유효시간만료 예외를 발생시킨다")
    @Test
    void testExpiredToken() {
        //given

        String email = "jwttester@weyoui.com";
        String password = "123456";
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,password);

        String accessToken = Jwts.builder()
                .setSubject(token.getName())
                .claim("auth", "USER")
                .setExpiration(new Date(new Date().getTime() - 1000L))
                .setIssuedAt(new Date(new Date().getTime() - 2 * 1000L))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        //when,then
        assertThrows(ExpiredJwtException.class, () ->{
            provider.validateToken(accessToken);
        });
    }

}