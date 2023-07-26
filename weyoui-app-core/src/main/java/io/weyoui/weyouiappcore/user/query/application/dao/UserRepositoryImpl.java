package io.weyoui.weyouiappcore.user.query.application.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import io.weyoui.weyouiappcore.user.domain.User;
import io.weyoui.weyouiappcore.user.presentation.dto.UserSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.weyoui.weyouiappcore.user.domain.QUser.user;


@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<User> searchAll(UserSearch userSearch, Pageable pageable) {


         List<User> content = getContent(userSearch, pageable);
         JPAQuery<Long> countQuery = getCountQuery(userSearch);

         return PageableExecutionUtils.getPage(content,pageable, countQuery::fetchOne);

    }

    private JPAQuery<Long> getCountQuery(UserSearch userSearch) {
        return jpaQueryFactory
                .select(user.count())
                .where(
                        emailLike(userSearch.getEmail()),
                        nicknameLike(userSearch.getNickname()),
                        addressLike(userSearch.getAddress())
                );
    }

    private List<User> getContent(UserSearch userSearch, Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(user)
                .where(
                        emailLike(userSearch.getEmail()),
                        nicknameLike(userSearch.getNickname()),
                        addressLike(userSearch.getAddress())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression addressLike(String address) {
        return StringUtils.isBlank(address) ? null : user.address.address2.contains(address);
    }


    private BooleanExpression nicknameLike(String nickname) {
        return StringUtils.isBlank(nickname) ? null : user.nickname.contains(nickname);
    }

    private BooleanExpression emailLike(String email) {
        return StringUtils.isBlank(email) ? null : user.email.contains(email);
    }
}
