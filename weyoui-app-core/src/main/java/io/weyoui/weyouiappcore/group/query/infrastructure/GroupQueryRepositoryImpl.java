package io.weyoui.weyouiappcore.group.query.infrastructure;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.command.domain.GroupState;
import io.weyoui.weyouiappcore.group.query.application.Direction;
import io.weyoui.weyouiappcore.group.query.application.GeometryUtil;
import io.weyoui.weyouiappcore.group.query.application.dto.GroupSearchRequest;
import io.weyoui.weyouiappcore.group.query.application.dto.Location;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static io.weyoui.weyouiappcore.group.command.domain.QGroup.group;

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
                  isWithInRadius(groupSearchRequest.getLocation(),groupSearchRequest.getDistance())
                );
    }

    private List<Group> getContents(GroupSearchRequest groupSearchRequest, Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(group)
                .where(
                    nameLike(groupSearchRequest.getName()),
                    equalsState(groupSearchRequest.getState()),
                    isWithInRadius(groupSearchRequest.getLocation(),groupSearchRequest.getDistance())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression isWithInDistance(Point location, double distance) {

        if(location == null) return null;

        return generateMBRContainsSQL(location,distance);
    }

    private BooleanExpression isWithInRadius(Point location, double radius) {

        if(location == null) return null;

        return generateDistanceCalcSQL(location,radius).loe(radius);
    }

    private NumberExpression<Double> generateDistanceCalcSQL(Point location, double radius) {
        // TODO : group.venue.point의 lat,lot 값 접근해야함
        return nativeSQLGenerator.generateDistanceCalcSQL(location.getX(), location.getY(), 1d,2d);
    }

    private BooleanExpression generateMBRContainsSQL(Point currentLocation, double distance) {
        Location northEastLocation = GeometryUtil.calculate(currentLocation.getX(), currentLocation.getY(), distance, Direction.NORTHEAST.getBearing());
        Location southWest = GeometryUtil.calculate(currentLocation.getX(), currentLocation.getY(), distance, Direction.SOUTHWEST.getBearing());

        return nativeSQLGenerator.generateMBRContainsSQL(northEastLocation.getLatitude(), northEastLocation.getLongitude(), southWest.getLatitude(),southWest.getLongitude()
        , group.venue.point);
    }

    private BooleanExpression equalsState(GroupState state) {
        return state == null ? null : group.state.eq(state);
    }

    private BooleanExpression nameLike(String name) {
        return StringUtils.isNullOrEmpty(name) ? null : group.name.like(name);
    }


}
