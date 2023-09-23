package io.weyoui.weyouiappcore.order.query.infrastructure;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.command.domain.OrderState;
import io.weyoui.weyouiappcore.order.command.domain.QOrder;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderSearchRequest;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderViewResponse;
import io.weyoui.weyouiappcore.order.query.application.dto.QOrderLineViewResponse;
import io.weyoui.weyouiappcore.order.query.application.dto.QOrderViewResponse;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static io.weyoui.weyouiappcore.order.command.domain.QOrder.order;
import static io.weyoui.weyouiappcore.order.command.domain.QOrderLine.orderLine;

public class OrderQueryRepositoryImpl implements OrderQueryRepositoryCustom{

    JPAQueryFactory jpaQueryFactory;

    public OrderQueryRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    @Override
    public Page<OrderViewResponse> findByConditions(OrderSearchRequest orderSearchRequest, Pageable pageable) {
        List<OrderViewResponse> contents = getContents(orderSearchRequest, pageable);
        JPAQuery<Long> countQuery = getCountQuery(orderSearchRequest);

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    @Override
    public OrderViewResponse findByIdToFetchAll(OrderId id) {
        OrderViewResponse resultSet = jpaQueryFactory
                .from(order)
                .join(order.orderLines,orderLine)
                .where(id == null ? null : order.id.eq(id))
                .transform(
                        groupBy(order.id)
                                .as(
                                        new QOrderViewResponse(
                                                order.id.id,
                                                order.orderer,
                                                order.orderStore,
                                                list(new QOrderLineViewResponse(
                                                        orderLine.productId.id,
                                                        orderLine.name,
                                                        orderLine.price,
                                                        orderLine.quantity,
                                                        orderLine.amounts
                                                )),
                                                order.orderDate,
                                                order.state,
                                                order.message,
                                                order.paymentInfo,
                                                order.totalAmounts
                                        )
                                )
                ).getOrDefault(id, null);

        if(resultSet == null) throw new IllegalArgumentException("ID와 일치하는 주문 정보가 존재하지 않습니다.");

        return resultSet;
    }

    private JPAQuery<Long> getCountQuery(OrderSearchRequest orderSearchRequest) {
        return jpaQueryFactory
                .select(order.count())
                .from(order)
                .join(order.orderLines, orderLine)
                .where(
                        isInDate(orderSearchRequest.getStartDateTime(), orderSearchRequest.getEndDateTime()),
                        isInState(orderSearchRequest.getStates()),
                        nameLike(orderSearchRequest.getOrderer().getName()),
                        OrderGroupIdEquals(orderSearchRequest.getOrderer().getGroupId()),
                        OrderUserIdEquals(orderSearchRequest.getOrderer().getUserId())
                );
    }

    private List<OrderViewResponse> getContents(OrderSearchRequest orderSearchRequest, Pageable pageable) {

        return jpaQueryFactory
                .from(order)
                .join(order.orderLines, orderLine)
                .where(
                        OrderIdLt(orderSearchRequest.getLastSearchedId()),
                        isInDate(orderSearchRequest.getStartDateTime(), orderSearchRequest.getEndDateTime()),
                        isInState(orderSearchRequest.getStates()),
                        nameLike(orderSearchRequest.getOrderer().getName()),
                        OrderGroupIdEquals(orderSearchRequest.getOrderer().getGroupId()),
                        OrderUserIdEquals(orderSearchRequest.getOrderer().getUserId())
                )
                .orderBy(getOrderSpecifier(pageable.getSort()))
                .limit(pageable.getPageSize())
                .transform(
                        groupBy(order.id)
                                .list(new QOrderViewResponse(
                                        order.id.id,
                                        order.orderer,
                                        order.orderStore,
                                        list(new QOrderLineViewResponse(
                                                orderLine.productId.id,
                                                orderLine.name,
                                                orderLine.price,
                                                orderLine.quantity,
                                                orderLine.amounts
                                        )),
                                        order.orderDate,
                                        order.state,
                                        order.message,
                                        order.paymentInfo,
                                        order.totalAmounts
                                ))
                );
    }




    private BooleanExpression OrderIdLt(OrderId lastSearchedId) {
        return lastSearchedId == null ? null : order.id.id.lt(lastSearchedId.getId());
    }


    private OrderSpecifier<?>[] getOrderSpecifier(Sort sort) {
        if(sort.isEmpty()) return  new OrderSpecifier[] {order.id.id.desc()};
        return sort.stream()
                .map(order -> {
                    PathBuilder<io.weyoui.weyouiappcore.order.command.domain.Order> pathBuilder = new PathBuilder<>(QOrder.order.getType(), QOrder.order.getMetadata());
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

    private BooleanExpression OrderGroupIdEquals(GroupId groupId) {
        return groupId == null ? null : order.orderer.groupId.eq(groupId);
    }

    private BooleanExpression OrderUserIdEquals(UserId userId) {
        return userId == null ? null : order.orderer.userId.eq(userId);
    }
}
