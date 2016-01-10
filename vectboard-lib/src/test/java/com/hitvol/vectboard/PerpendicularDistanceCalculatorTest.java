package com.hitvol.vectboard;

import boofcv.struct.Tuple2;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertThat;
import static com.hitvol.vectboard.Filament.Point;

/**
 * Created by archevel on 2015-12-20.
 */
public class PerpendicularDistanceCalculatorTest {

    @Test
    public void perpendicular_distance_for_simple_cases() {
        Point p1 = new Point(0,0);
        Point p2 = new Point(0,100);

        PerpendicularDistanceCalculator calc = new PerpendicularDistanceCalculator(p1,p2);

        Tuple2<Point, Double>[] inputsAndExpected = new Tuple2[] {
                new Tuple2<Point, Double>(new Point(2, 0), 2.0),
                new Tuple2<Point, Double>(new Point(0, 3), 0.0),
                new Tuple2<Point, Double>(new Point(1, 3), 1.0),
        };
        for (Tuple2<Point, Double> inAndExpected : inputsAndExpected){
            double expected = inAndExpected.data1;
            double actual = calc.distanceTo(inAndExpected.data0);
            assertEquals(expected, actual);
        }
    }
}
