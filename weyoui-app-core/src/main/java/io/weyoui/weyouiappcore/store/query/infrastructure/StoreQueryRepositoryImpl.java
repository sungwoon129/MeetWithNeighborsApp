package io.weyoui.weyouiappcore.store.query.infrastructure;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.weyoui.weyouiappcore.common.querydsl.NativeSQLGenerator;
import io.weyoui.weyouiappcore.group.query.application.Direction;
import io.weyoui.weyouiappcore.group.query.application.GeometryUtil;
import io.weyoui.weyouiappcore.group.query.application.dto.Location;
import io.weyoui.weyouiappcore.product.query.application.dto.QProductViewResponse;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.command.domain.StoreState;
import io.weyoui.weyouiappcore.store.query.application.dto.QStoreViewResponse;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreSearchRequest;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreViewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.Arrays;
import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static io.weyoui.weyouiappcore.product.command.domain.QProduct.product;
import static io.weyoui.weyouiappcore.store.command.domain.QStore.store;

public class StoreQueryRepositoryImpl implements StoreQueryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final NativeSQLGenerator nativeSQLGenerator;

    public StoreQueryRepositoryImpl(JPAQueryFactory jpaQueryFactory, NativeSQLGenerator nativeSQLGenerator) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.nativeSQLGenerator = nativeSQLGenerator;
    }

    @Override
    public Page<StoreViewResponse> findByConditions(StoreSearchRequest storeSearchRequest, Pageable pageable) {
        List<StoreViewResponse> contents = getContents(storeSearchRequest, pageable);
        JPAQuery<Long> countQuery = getCountQuery(storeSearchRequest);

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    private JPAQuery<Long> getCountQuery(StoreSearchRequest storeSearchRequest) {
        return jpaQueryFactory
                .select(store.count())
                .from(store)
                .leftJoin(store.productInfos, product)
                .where(
                        nameLike(storeSearchRequest.getName()),
                        isInState(storeSearchRequest.getStates()),
                        isWithInDistance(storeSearchRequest.getLocation(), storeSearchRequest.getDistance())
                );
    }

    private List<StoreViewResponse> getContents(StoreSearchRequest storeSearchRequest, Pageable pageable) {
        return jpaQueryFactory
                .from(store)
                .leftJoin(store.productInfos, product)
                .where(
                        nameLike(storeSearchRequest.getName()),
                        isInState(storeSearchRequest.getStates()),
                        isWithInDistance(storeSearchRequest.getLocation(), storeSearchRequest.getDistance())
                )
                .orderBy(getOrderSpecifier(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        groupBy(store.id)
                                .list(
                                        new QStoreViewResponse(
                                                store.id,
                                                store.name,
                                                store.owner,
                                                store.address,
                                                list(new QProductViewResponse(
                                                        product.id,
                                                        product.name,
                                                        product.price,
                                                        product.state
                                                )),
                                                store.rating,
                                                store.category,
                                                store.state
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

    private BooleanExpression isWithInDistance(Location location, double distance) {

        if (location == null) return null;

        return generateMBRContainsSQL(location, distance).eq(1d);
    }

    private NumberExpression<Double> generateMBRContainsSQL(Location currentLocation, double distance) {
        Location northEastLocation = GeometryUtil.calculate(currentLocation.getLatitude(), currentLocation.getLongitude(), distance, Direction.NORTHEAST.getBearing());
        Location southWest = GeometryUtil.calculate(currentLocation.getLatitude(), currentLocation.getLongitude(), distance, Direction.SOUTHWEST.getBearing());

        return nativeSQLGenerator.generateMBRContainsSQL(northEastLocation.getLatitude(), northEastLocation.getLongitude(), southWest.getLatitude(), southWest.getLongitude()
                , store.address.point);
    }

    private BooleanExpression equalsState(String stateCode) {
        return stateCode == null ? null : store.state.eq(StoreState.findByCode(stateCode));
    }

    private BooleanExpression isInState(String ... stateCode) {
        return stateCode == null ? null : store.state.in(Arrays.stream(stateCode).map(StoreState::findByCode).toList());
    }

    private BooleanExpression nameLike(String name) {
        return StringUtils.isNullOrEmpty(name) ? null : store.name.contains(name);
    }
}
