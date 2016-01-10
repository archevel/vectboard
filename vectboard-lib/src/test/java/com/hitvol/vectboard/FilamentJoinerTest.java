package com.hitvol.vectboard;


import com.hitvol.vectboard.Filament.Point;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

/**
 * Created by archevel on 2015-12-23.
 */
public class FilamentJoinerTest {
    @Test
    public void should_leave_unchanged_filaments_further_appart_than_threshold() throws Exception {

        FilamentJoiner joiner = new FilamentJoiner();

        Filament f1 = new Filament();
        f1.add(1,1);
        f1.add(1,10);

        Filament f2 = new Filament();
        f2.add(10,1);
        f2.add(10, 10);

        List<Filament> filaments = Arrays.asList(f1, f2);


        int distanceThreshold = 5;
        List<Filament> actual = joiner.joinAdjacent(filaments, distanceThreshold);

        assertThat(actual, hasItems(filaments.toArray(new Filament[]{})));
        assertThat(filaments, hasItems(actual.toArray(new Filament[]{})));
    }

    @Test
    public void should_join_filaments_where_end_and_start_is_within_distance_threshold_together() throws Exception {

        FilamentJoiner joiner = new FilamentJoiner();

        Filament f1 = new Filament();
        f1.add(1,1);
        f1.add(1,10);

        Filament f2 = new Filament();
        f2.add(3, 10);
        f2.add(10, 10);

        List<Filament> filaments = Arrays.asList(f1, f2);
        int distanceThreshold = 5;

        List<Filament> filamentResult = joiner.joinAdjacent(filaments, distanceThreshold);
        List<Point> actual = filamentResult.get(0).getPoints();

        assertEquals(actual.size(), 4);
        assertEquals(actual.get(0), new Point(1,1));
        assertEquals(actual.get(1), new Point(1, 10));
        assertEquals(actual.get(2), new Point(3, 10));
        assertEquals(actual.get(3), new Point(10, 10));
    }

    @Test
    public void should_join_filaments_where_starts_are_within_distance_threshold_together() throws Exception {

        FilamentJoiner joiner = new FilamentJoiner();

        Filament f1 = new Filament();
        f1.add(1,10);
        f1.add(1,1);

        Filament f2 = new Filament();
        f2.add(3, 10);
        f2.add(10, 10);

        List<Filament> filaments = Arrays.asList(f1, f2);
        int distanceThreshold = 5;

        List<Filament> filamentResult = joiner.joinAdjacent(filaments, distanceThreshold);
        List<Point> actual = filamentResult.get(0).getPoints();

        assertEquals(actual.size(), 4);
        assertEquals(actual.get(0), new Point(10,10));
        assertEquals(actual.get(1), new Point(3, 10));
        assertEquals(actual.get(2), new Point(1, 10));
        assertEquals(actual.get(3), new Point(1,1));
    }

    @Test
    public void should_join_filaments_where_ends_are_within_distance_threshold_together() throws Exception {

        FilamentJoiner joiner = new FilamentJoiner();

        Filament f1 = new Filament();
        f1.add(1,1);
        f1.add(1,10);

        Filament f2 = new Filament();
        f2.add(10, 10);
        f2.add(3, 10);

        List<Filament> filaments = Arrays.asList(f1, f2);
        int distanceThreshold = 5;

        List<Filament> filamentResult = joiner.joinAdjacent(filaments, distanceThreshold);
        List<Point> actual = filamentResult.get(0).getPoints();

        assertEquals(actual.size(), 4);
        assertEquals(actual.get(0), new Point(1,1));
        assertEquals(actual.get(1), new Point(1, 10));
        assertEquals(actual.get(2), new Point(3, 10));
        assertEquals(actual.get(3), new Point(10,10));
    }

    @Test
    public void should_join_filaments_where_start_and_end_is_within_distance_threshold_together() throws Exception {

        FilamentJoiner joiner = new FilamentJoiner();

        Filament f1 = new Filament();
        f1.add(1,10);
        f1.add(1,1);

        Filament f2 = new Filament();
        f2.add(10, 10);
        f2.add(3, 10);

        List<Filament> filaments = Arrays.asList(f1, f2);
        int distanceThreshold = 5;

        List<Filament> filamentResult = joiner.joinAdjacent(filaments, distanceThreshold);
        List<Point> actual = filamentResult.get(0).getPoints();

        assertEquals(actual.size(), 4);
        assertEquals(actual.get(0), new Point(10, 10));
        assertEquals(actual.get(1), new Point(3, 10));
        assertEquals(actual.get(2), new Point(1, 10));
        assertEquals(actual.get(3), new Point(1,1));
    }

    @Test
    public void should_join_multiple_filaments_together() throws Exception {

        FilamentJoiner joiner = new FilamentJoiner();

        Filament f1 = new Filament();
        f1.add(1,10);
        f1.add(1,1);

        Filament f2 = new Filament();
        f2.add(10, 10);
        f2.add(3, 10);

        Filament f3 = new Filament();
        f3.add(10, 12);
        f3.add(3, 20);

        List<Filament> filaments = Arrays.asList(f1, f2, f3);
        int distanceThreshold = 5;

        List<Filament> filamentResult = joiner.joinAdjacent(filaments, distanceThreshold);
        List<Point> actual = filamentResult.get(0).getPoints();

        assertEquals(actual.size(), 6);
        assertEquals(actual.get(0), new Point(3, 20));
        assertEquals(actual.get(1), new Point(10, 12));
        assertEquals(actual.get(2), new Point(10, 10));
        assertEquals(actual.get(3), new Point(3, 10));
        assertEquals(actual.get(4), new Point(1, 10));
        assertEquals(actual.get(5), new Point(1,1));
    }
}
