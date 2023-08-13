package io.weyoui.weyouiappcore.group.query.infrastructure;

import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.NumberExpression;
import org.locationtech.jts.geom.Point;

public interface NativeSQLGenerator {

    NumberExpression<Double> generateDistanceCalcSQL(Double longitudeCond, Double latitudeCond, Double dbLongitude, Double dbLatitude);

    NumberExpression<Double> generateMBRContainsSQL(Double northEastLat, Double northEastLot, Double southWestLat, Double southWestLot,
                                             ComparablePath<Point> dbLocationData);
}
