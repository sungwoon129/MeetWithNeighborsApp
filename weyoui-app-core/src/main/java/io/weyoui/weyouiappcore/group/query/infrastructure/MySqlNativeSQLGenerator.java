package io.weyoui.weyouiappcore.group.query.infrastructure;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import io.weyoui.weyouiappcore.group.command.domain.Group;
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
    public BooleanExpression generateMBRContainsSQL(Double northEastLat, Double northEastLot, Double southWestLat, Double southWestLot,
                                                            ComparablePath<Point> dbLocationData) {
        return Expressions.booleanTemplate( "MBRContains({{0}, {1})",
                Expressions.stringTemplate("ST_LINESTRINGFROMTEXT({0}, {1})",
                        Expressions.stringTemplate("LINESTRING({0}, {1}, {2}, {3})", northEastLat,northEastLot,southWestLat,southWestLat),
                        dbLocationData
                        ),
                Group.class
        );
    }

}
