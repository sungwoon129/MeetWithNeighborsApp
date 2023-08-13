package io.weyoui.weyouiappcore.group.query.infrastructure;

import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component
public class MySqlNativeSQLGenerator implements NativeSQLGenerator {


    @Override
    public NumberExpression<Double> generateDistanceCalcSQL(Double longitudeCond, Double latitudeCond, Double dbLongitude, Double dbLatitude) {
        return Expressions.numberTemplate(Double.class, "ST_Distance_Sphere({0|, {1})",
                Expressions.stringTemplate("POINT({0}, {1})", latitudeCond,longitudeCond),
                Expressions.stringTemplate("POINT({0}, {1})", dbLatitude,dbLongitude)
        );
    }

    @Override
    public NumberExpression<Double> generateMBRContainsSQL(Double northEastLat, Double northEastLot, Double southWestLat, Double southWestLot,
                                                            ComparablePath<Point> dbLocationData) {
        return Expressions.numberTemplate(Double.class,"MBRContains({0}, {1})",
                    Expressions.stringTemplate("ST_GeomFromText({0})",
                        Expressions.stringTemplate("'LINESTRING(" + northEastLat + " " + northEastLot + "," + southWestLat + " " + southWestLot + ")'")),
                    dbLocationData);
    }

}
