package io.weyoui.weyouiappcore.user.infrastructure;

import io.weyoui.weyouiappcore.user.command.domain.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final long refreshTokenExpireTime;

    public RefreshTokenRedisRepository(StringRedisTemplate redisTemplate, @Value("${jwt.token.expiration-in-refresh-token}") final long refreshTokenExpireTime) {
        this.redisTemplate = redisTemplate;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    @Transactional
    public void save(final String token, final String email) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(token, email);
        redisTemplate.expire(token, refreshTokenExpireTime, TimeUnit.MILLISECONDS);
    }

    public Optional<RefreshToken> findById(final String token) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String id = valueOperations.get(token);

        if(Objects.isNull(id)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(token,id));
    }
}
