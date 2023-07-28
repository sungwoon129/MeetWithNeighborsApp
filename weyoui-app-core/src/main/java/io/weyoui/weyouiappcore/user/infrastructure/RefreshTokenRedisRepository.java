package io.weyoui.weyouiappcore.user.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRedisRepository {

    private final StringRedisTemplate redisTemplate;
    private final long refreshTokenExpireTime;

    public RefreshTokenRedisRepository(StringRedisTemplate redisTemplate, @Value("${jwt.token.expiration-in-refresh-token}") final long refreshTokenExpireTime) {
        this.redisTemplate = redisTemplate;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    public void save(final String token, final String id) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(token, id);
        redisTemplate.expire(token, refreshTokenExpireTime, TimeUnit.MILLISECONDS);
    }

    public Optional<Map<String, String>> findById(final String token) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String id = valueOperations.get(token);

        if(Objects.isNull(id)) {
            return Optional.empty();
        }

        return Optional.of(Map.of(token,id));
    }
}
