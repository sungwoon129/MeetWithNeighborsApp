package io.weyoui.weyouiappcore;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.weyoui.weyouiappcore.user.query.application.dao.UserRepositoryCustom;
import io.weyoui.weyouiappcore.user.query.application.dao.UserRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @PersistenceContext
    EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean(name = "userRepositoryImpl")
    public UserRepositoryCustom UserRepositoryCustom() {
        return new UserRepositoryImpl(jpaQueryFactory());
    }

}
