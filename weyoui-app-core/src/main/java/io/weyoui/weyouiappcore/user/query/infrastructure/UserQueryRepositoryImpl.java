package io.weyoui.weyouiappcore.user.query.infrastructure;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.query.application.dto.UserSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static io.weyoui.weyouiappcore.user.command.domain.QUser.user;



@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<User> findByConditions(UserSearchRequest userSearchRequest, Pageable pageable) {


         List<User> content = getContents(userSearchRequest, pageable);
         JPAQuery<Long> countQuery = getCountQuery(userSearchRequest);

         return PageableExecutionUtils.getPage(content,pageable, countQuery::fetchOne);

    }

    private JPAQuery<Long> getCountQuery(UserSearchRequest userSearchRequest) {
        return jpaQueryFactory
                .select(user.count())
                .from(user)
                .where(
                        emailLike(userSearchRequest.getEmail()),
                        nicknameLike(userSearchRequest.getNickname()),
                        addressLike(userSearchRequest.getAddress())
                );
    }

    private List<User> getContents(UserSearchRequest userSearchRequest, Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(user)
                .where(
                        emailLike(userSearchRequest.getEmail()),
                        nicknameLike(userSearchRequest.getNickname()),
                        addressLike(userSearchRequest.getAddress())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression addressLike(String address) {
        return StringUtils.isNullOrEmpty(address) ? null : user.address.address2.contains(address);
    }


    private BooleanExpression nicknameLike(String nickname) {
        return StringUtils.isNullOrEmpty(nickname) ? null : user.nickname.contains(nickname);
    }

    private BooleanExpression emailLike(String email) {
        return StringUtils.isNullOrEmpty(email) ? null : user.email.contains(email);
    }
}
