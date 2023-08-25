package io.weyoui.weyouiappcore.group.query.infrastructure;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.weyoui.weyouiappcore.common.querydsl.NativeSQLGenerator;
import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.command.domain.GroupState;
import io.weyoui.weyouiappcore.group.query.application.Direction;
import io.weyoui.weyouiappcore.group.query.application.GeometryUtil;
import io.weyoui.weyouiappcore.group.query.application.dto.GroupSearchRequest;
import io.weyoui.weyouiappcore.group.query.application.dto.Location;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

import static io.weyoui.weyouiappcore.group.command.domain.QGroup.group;
import static io.weyoui.weyouiappcore.store.command.domain.QStore.store;

public class GroupQueryRepositoryImpl implements GroupQueryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final NativeSQLGenerator nativeSQLGenerator;

    public GroupQueryRepositoryImpl(JPAQueryFactory jpaQueryFactory, NativeSQLGenerator nativeSQLGenerator) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.nativeSQLGenerator = nativeSQLGenerator;
    }


    @Override
    public Page<Group> findByConditions(GroupSearchRequest groupSearchRequest, Pageable pageable) {

        List<Group> contents = getContents(groupSearchRequest, pageable);
        JPAQuery<Long> countQuery = getCountQuery(groupSearchRequest);

        return PageableExecutionUtils.getPage(contents,pageable,countQuery::fetchOne);
    }

    private JPAQuery<Long> getCountQuery(GroupSearchRequest groupSearchRequest) {
        return jpaQueryFactory
                .select(group.count())
                .from(group)
                .where(
                  nameLike(groupSearchRequest.getName()),
                  equalsState(groupSearchRequest.getState()),
                  isWithInDistance(groupSearchRequest.getLocation(),groupSearchRequest.getDistance())
                );
    }

    private List<Group> getContents(GroupSearchRequest groupSearchRequest, Pageable pageable) {
        return jpaQueryFactory
                .select(group)
                .from(group)
                .where(
                    nameLike(groupSearchRequest.getName()),
                    equalsState(groupSearchRequest.getState()),
                    isWithInDistance(groupSearchRequest.getLocation(),groupSearchRequest.getDistance())
                )
                .orderBy(getOrderSpecifier(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private OrderSpecifier<?>[] getOrderSpecifier(Sort sort) {

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        sort.stream()
                .forEach(order -> {
                    PathBuilder<Group> pathBuilder = new PathBuilder<>(group.getType(), group.getMetadata());
                    orders.add(new OrderSpecifier(toQuerydslDirection(order.getDirection()), pathBuilder.get(order.getProperty())));
                });

        return orders.toArray(OrderSpecifier[]::new);
    }

    private Order toQuerydslDirection(Sort.Direction direction) {
        return direction.isAscending() ? Order.ASC : Order.DESC;
    }

    private BooleanExpression isWithInDistance(Location location, double distance) {

        if(location == null) return null;

        return generateMBRContainsSQL(location,distance).eq(1d);
    }

    private NumberExpression<Double> generateMBRContainsSQL(Location currentLocation, double distance) {
        Location northEastLocation = GeometryUtil.calculate(currentLocation.getLongitude(), currentLocation.getLatitude(), distance, Direction.NORTHEAST.getBearing());
        Location southWest = GeometryUtil.calculate(currentLocation.getLongitude(), currentLocation.getLatitude(), distance, Direction.SOUTHWEST.getBearing());

        return nativeSQLGenerator.generateMBRContainsSQL(northEastLocation.getLatitude(), northEastLocation.getLongitude(), southWest.getLatitude(),southWest.getLongitude()
                , group.place.point);
    }

    private BooleanExpression equalsState(String stateCode) {
        return stateCode == null ? null : group.state.eq(GroupState.findByCode(stateCode));
    }

    private BooleanExpression nameLike(String name) {
        return StringUtils.isNullOrEmpty(name) ? null : group.name.contains(name);
    }


}
