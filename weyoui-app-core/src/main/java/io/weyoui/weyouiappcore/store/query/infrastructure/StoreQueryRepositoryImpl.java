package io.weyoui.weyouiappcore.store.query.infrastructure;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.weyoui.weyouiappcore.group.query.application.Direction;
import io.weyoui.weyouiappcore.group.query.application.GeometryUtil;
import io.weyoui.weyouiappcore.group.query.application.dto.Location;
import io.weyoui.weyouiappcore.common.querydsl.NativeSQLGenerator;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.command.domain.StoreState;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static io.weyoui.weyouiappcore.group.command.domain.QGroup.group;
import static io.weyoui.weyouiappcore.store.command.domain.QStore.store;

public class StoreQueryRepositoryImpl implements StoreQueryRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    private final NativeSQLGenerator nativeSQLGenerator;

    public StoreQueryRepositoryImpl(JPAQueryFactory jpaQueryFactory, NativeSQLGenerator nativeSQLGenerator) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.nativeSQLGenerator = nativeSQLGenerator;
    }

    @Override
    public Page<Store> findByConditions(StoreSearchRequest storeSearchRequest, Pageable pageable) {
        List<Store> contents = getContents(storeSearchRequest, pageable);
        JPAQuery<Long> countQuery = getCountQuery(storeSearchRequest);

        return PageableExecutionUtils.getPage(contents,pageable,countQuery::fetchOne);
    }

    private JPAQuery<Long> getCountQuery(StoreSearchRequest storeSearchRequest) {
        return jpaQueryFactory
                .select(store.count())
                .from(store)
                .where(
                        nameLike(storeSearchRequest.getName()),
                        equalsState(storeSearchRequest.getState()),
                        isWithInDistance(storeSearchRequest.getLocation(),storeSearchRequest.getDistance())
                );
    }

    private List<Store> getContents(StoreSearchRequest storeSearchRequest, Pageable pageable) {
        return jpaQueryFactory
                .select(store)
                .from(store)
                .where(
                        nameLike(storeSearchRequest.getName()),
                        equalsState(storeSearchRequest.getState()),
                        isWithInDistance(storeSearchRequest.getLocation(),storeSearchRequest.getDistance())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
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
        return stateCode == null ? null : store.state.eq(StoreState.findByCode(stateCode));
    }

    private BooleanExpression nameLike(String name) {
        return StringUtils.isNullOrEmpty(name) ? null : store.name.contains(name);
    }
}
