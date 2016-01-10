package com.hitvol.vectboard;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by archevel on 2015-11-16.
 */
public class RamerDouglasPeuckerFilamentSimplifierTest {
    @Test
    public void should_leave_unchanged_simple_filaments() {
        Filament f = new Filament();
        f.add(0,0);
        f.add(1,1);

        Filament actual = RamerDouglasPeuckerFilamentSimplifier.simplifyFilament(f, 1d);


        assertEquals(2, actual.getPoints().size());
        assertEquals(new Filament.Point(0,0), actual.getPoints().get(0));
        assertEquals(new Filament.Point(1,1), actual.getPoints().get(1));
    }

    @Test
    public void should_leave_unchanged_filaments_where_all_points_are_outside_tolerance() {
        Filament f = new Filament();
        f.add(0,0);
        f.add(1,1);
        f.add(2,2);
        f.add(3,3);

        double unreasonableTolerance = -1d;
        Filament actual = RamerDouglasPeuckerFilamentSimplifier.simplifyFilament(f, unreasonableTolerance);

        assertEquals(4, actual.getPoints().size());
        assertEquals(new Filament.Point(0, 0), actual.getPoints().get(0));
        assertEquals(new Filament.Point(1,1), actual.getPoints().get(1));
        assertEquals(new Filament.Point(2,2), actual.getPoints().get(2));
        assertEquals(new Filament.Point(3,3), actual.getPoints().get(3));
    }

    @Test
    public void should_remove_points_inside_tolerance() {
        Filament f = new Filament();
        f.add(0, 0);
        f.add(1, 1);
        f.add(2, 2);
        Filament actual = RamerDouglasPeuckerFilamentSimplifier.simplifyFilament(f, 1d);

        assertEquals(2, actual.getPoints().size());
        assertEquals(new Filament.Point(0,0), actual.getPoints().get(0));
        assertEquals(new Filament.Point(2,2), actual.getPoints().get(1));
    }


    @Test
    public void should_remove_points_inside_tolerance_from_square_like_shape_except_last() {
        Filament f = new Filament();
        f.add(0, 0);
        f.add(0, 2);
        f.add(0, 4);
        f.add(2, 4);
        f.add(4, 4);
        f.add(4, 2);
        f.add(4, 0);
        f.add(2, 0);
        Filament actual = RamerDouglasPeuckerFilamentSimplifier.simplifyFilament(f, 1d);

        assertEquals(5, actual.getPoints().size());
        assertEquals(new Filament.Point(0,0), actual.getPoints().get(0));
        assertEquals(new Filament.Point(0,4), actual.getPoints().get(1));
        assertEquals(new Filament.Point(4,4), actual.getPoints().get(2));
        assertEquals(new Filament.Point(4,0), actual.getPoints().get(3));
        assertEquals(new Filament.Point(2,0), actual.getPoints().get(4));
    }
}
