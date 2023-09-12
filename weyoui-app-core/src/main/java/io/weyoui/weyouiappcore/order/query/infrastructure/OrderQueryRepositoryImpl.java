package io.weyoui.weyouiappcore.order.query.infrastructure;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.weyoui.weyouiappcore.order.command.domain.OrderState;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderViewResponseDto;
import io.weyoui.weyouiappcore.order.query.application.dto.QOrderViewResponseDto;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    public Page<OrderViewResponseDto> findByConditions(StoreSearchRequest storeSearchRequest, Pageable pageable) {
        return null;
    }

    private JPAQuery<Long> getCountQuery(StoreSearchRequest storeSearchRequest) {
        return jpaQueryFactory
                .select(order.count())
                .from(order)
                .where(
                        nameLike(storeSearchRequest.getName()),
                        isInState(storeSearchRequest.getStates())
                );
    }

    private List<OrderViewResponseDto> getContents(StoreSearchRequest storeSearchRequest, Pageable pageable) {
        return jpaQueryFactory
                .from(order)
                .where(
                        // TODO : 요구사항에 맞는 booleanExpressino 추가 필요
                        nameLike(storeSearchRequest.getName()),
                        isInState(storeSearchRequest.getStates())
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
}
