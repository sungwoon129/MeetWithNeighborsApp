package io.weyoui.weyouiappcore.order.query.infrastructure;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.weyoui.weyouiappcore.order.command.domain.OrderState;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderSearchRequest;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderViewResponseDto;
import io.weyoui.weyouiappcore.order.query.application.dto.QOrderViewResponseDto;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static io.weyoui.weyouiappcore.order.command.domain.QOrder.order;
import static io.weyoui.weyouiappcore.store.command.domain.QStore.store;

public class OrderQueryRepositoryImpl implements OrderQueryRepositoryCustom{

    JPAQueryFactory jpaQueryFactory;

    public OrderQueryRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    @Override
    public Page<OrderViewResponseDto> findByConditions(OrderSearchRequest orderSearchRequest, Pageable pageable) {
        List<OrderViewResponseDto> contents = getContents(orderSearchRequest, pageable);
        JPAQuery<Long> countQuery = getCountQuery(orderSearchRequest);

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    private JPAQuery<Long> getCountQuery(OrderSearchRequest orderSearchRequest) {
        return jpaQueryFactory
                .select(order.count())
                .from(order)
                .where(
                        isInDate(orderSearchRequest.getStartDateTime(), orderSearchRequest.getEndDateTime()),
                        isInState(orderSearchRequest.getStates()),
                        nameLike(orderSearchRequest.getOrderer())
                );
    }

    private List<OrderViewResponseDto> getContents(OrderSearchRequest orderSearchRequest, Pageable pageable) {
        return jpaQueryFactory
                .from(order)
                .where(
                        isInDate(orderSearchRequest.getStartDateTime(), orderSearchRequest.getEndDateTime()),
                        isInState(orderSearchRequest.getStates()),
                        nameLike(orderSearchRequest.getOrderer())
                )
                .orderBy(getOrderSpecifier(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        groupBy(order.orderId)
                                .list(
                                        new QOrderViewResponseDto(
                                                order.orderId.id,
                                                order.orderer,
                                                order.orderLines,
                                                order.orderDate,
                                                order.state,
                                                order.message,
                                                order.paymentInfo,
                                                order.totalAmounts
                                        )
                                ));
    }


    private OrderSpecifier<?>[] getOrderSpecifier(Sort sort) {
        return sort.stream()
                .map(order -> {
                    PathBuilder<Store> pathBuilder = new PathBuilder<>(store.getType(), store.getMetadata());
                    return new OrderSpecifier(toQuerydslDirection(order.getDirection()), pathBuilder.get(order.getProperty()));
                }).toArray(OrderSpecifier[]::new);
    }

    private Order toQuerydslDirection(Sort.Direction direction) {
        return direction.isAscending() ? Order.ASC : Order.DESC;
    }

    private BooleanExpression isInState(String ... stateCode) {
        return stateCode == null ? null : order.state.in(Arrays.stream(stateCode).map(OrderState::findByCode).toList());
    }

    private BooleanExpression nameLike(String name) {
        return StringUtils.isNullOrEmpty(name) ? null : order.orderer.name.contains(name);
    }

    private BooleanExpression isInDate(LocalDateTime start, LocalDateTime end) {
        if(start == null || end == null) return null;
        return order.orderDate.goe(start.toEpochSecond(ZoneOffset.UTC)).and(order.orderDate.loe(end.toEpochSecond(ZoneOffset.UTC)));
    }
}
