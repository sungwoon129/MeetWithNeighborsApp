package io.weyoui.weyouiappcore;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.weyoui.weyouiappcore.common.querydsl.MySqlNativeSQLGenerator;
import io.weyoui.weyouiappcore.common.querydsl.NativeSQLGenerator;
import io.weyoui.weyouiappcore.user.query.infrastructure.UserQueryRepositoryCustom;
import io.weyoui.weyouiappcore.user.query.infrastructure.UserQueryRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestQueryDSLConfig {

    @PersistenceContext
    EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean(name = "userRepositoryImpl")
    public UserQueryRepositoryCustom UserRepositoryCustom() {
        return new UserQueryRepositoryImpl(jpaQueryFactory());
    }

    @Bean(name = "mySqlNativeSQLGenerator")
    public NativeSQLGenerator NativeSQLGenerator() {return new MySqlNativeSQLGenerator(); }

}
