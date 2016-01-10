package com.hitvol.vectboard;

import boofcv.struct.image.ImageUInt8;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;

/**
 * Created by archevel on 2015-11-14.
 */
public class FilamentFinderTest {

    ImageUInt8 binary = new ImageUInt8(5, 5);

    @Test
    public void should_find_horizontal_line() {

        binary.setData(new byte[] {
                0,0,0,0,0,
                0,0,0,0,0,
                0,1,1,1,0,
                0,0,0,0,0,
                0,0,0,0,0
        });

        FilamentFinder finder = new FilamentFinder();
        List<Filament> filaments = finder.process(binary);

        Filament actual = filaments.get(0);
        List<Filament.Point> points = actual.getPoints();

        assertThat(points.get(0), equalTo(new Filament.Point(1, 2)));
        assertThat(points.get(points.size() - 1), equalTo(new Filament.Point(3, 2)));
    }

    @Test
    public void should_find_vertical_line() {
        binary.setData(new byte[] {
                0,0,0,0,0,
                0,0,1,0,0,
                0,0,1,0,0,
                0,0,1,0,0,
                0,0,0,0,0
        });

        FilamentFinder finder = new FilamentFinder();
        List<Filament> filaments = finder.process(binary);

        Filament actual = filaments.get(0);
        List<Filament.Point> points = actual.getPoints();

        assertThat(points.get(0), equalTo(new Filament.Point(2, 1)));
        assertThat(points.get(points.size() - 1), equalTo(new Filament.Point(2, 3)));
    }


    @Test
    public void should_find_diagonal_going_down() {
        binary.setData(new byte[] {
                0,0,0,0,0,
                0,1,0,0,0,
                0,0,1,0,0,
                0,0,0,1,0,
                0,0,0,0,0
        });

        FilamentFinder finder = new FilamentFinder();
        List<Filament> filaments = finder.process(binary);

        Filament actual = filaments.get(0);
        List<Filament.Point> points = actual.getPoints();

        assertThat(points.get(0), equalTo(new Filament.Point(1, 1)));
        assertThat(points.get(points.size() - 1), equalTo(new Filament.Point(3, 3)));
    }

    @Test
    public void should_find_diagonal_going_up() {
        binary.setData(new byte[] {
                0,0,0,0,0,
                0,0,0,1,0,
                0,0,1,0,0,
                0,1,0,0,0,
                0,0,0,0,0
        });

        FilamentFinder finder = new FilamentFinder();
        List<Filament> filaments = finder.process(binary);

        Filament actual = filaments.get(0);
        List<Filament.Point> points = actual.getPoints();

        assertThat(points.get(0), equalTo(new Filament.Point(3, 1)));
        assertThat(points.get(points.size() - 1), equalTo(new Filament.Point(1, 3)));
    }

    @Test
    public void should_find_vertical_lines_on_the_border() {
        binary.setData(new byte[] {
                1,0,0,0,0,
                1,0,0,0,0,
                1,0,0,0,0,
                1,0,0,0,0,
                1,0,0,0,0
        });

        FilamentFinder finder = new FilamentFinder();
        List<Filament> filaments = finder.process(binary);

        Filament actual = filaments.get(0);
        List<Filament.Point> points = actual.getPoints();

        assertThat(points.get(0), equalTo(new Filament.Point(0, 0)));
        assertThat(points.get(points.size() - 1), equalTo(new Filament.Point(0, 4)));
    }


    @Test
    public void should_find_horizontal_lines_on_the_border() {
        binary.setData(new byte[] {
                1,1,1,1,1,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0
        });

        FilamentFinder finder = new FilamentFinder();
        List<Filament> filaments = finder.process(binary);

        Filament actual = filaments.get(0);
        List<Filament.Point> points = actual.getPoints();

        assertThat(points.get(0), equalTo(new Filament.Point(0, 0)));
        assertThat(points.get(points.size() - 1), equalTo(new Filament.Point(4, 0)));
    }

    @Test
    public void should_find_multiple_lines() {
        binary.setData(new byte[]{
                1, 1, 1, 1, 1,
                0, 0, 0, 0, 0,
                1, 1, 1, 1, 1,
                0, 0, 0, 0, 0,
                1, 1, 1, 1, 1,
        });

        FilamentFinder finder = new FilamentFinder();
        List<Filament> filaments = finder.process(binary);

        assertThat(filaments.size(), is(3));
        Filament actual1 = filaments.get(0);
        List<Filament.Point> points1 = actual1.getPoints();

        assertThat(points1.get(0), equalTo(new Filament.Point(0, 0)));
        assertThat(points1.get(points1.size() - 1), equalTo(new Filament.Point(4, 0)));

        Filament actual2 = filaments.get(1);
        List<Filament.Point> points2 = actual2.getPoints();

        assertThat(points2.get(0), equalTo(new Filament.Point(0, 2)));
        assertThat(points2.get(points2.size() - 1), equalTo(new Filament.Point(4, 2)));

        Filament actual3 = filaments.get(2);
        List<Filament.Point> points3 = actual3.getPoints();

        assertThat(points3.get(0), equalTo(new Filament.Point(0, 4)));
        assertThat(points3.get(points3.size() - 1), equalTo(new Filament.Point(4, 4)));
    }

    @Test
    public void should_find_square_as_single_filament() {
        binary.setData(new byte[]{
                1, 1, 1, 1, 1,
                1, 0, 0, 0, 1,
                1, 0, 0, 0, 1,
                1, 0, 0, 0, 1,
                1, 1, 1, 1, 1,
        });

        FilamentFinder finder = new FilamentFinder();
        List<Filament> filaments = finder.process(binary);

        assertThat(filaments.size(), is(1));
        Filament actual1 = filaments.get(0);
        List<Filament.Point> points1 = actual1.getPoints();

        assertThat(points1.get(0), equalTo(new Filament.Point(0, 0)));
        assertThat(points1.get(points1.size() - 1), equalTo(new Filament.Point(0, 1)));

    }

    @Test
    public void should_find_two_lines_for_a_reverse_E() {
        binary.setData(new byte[]{
                1, 1, 1, 1, 1,
                0, 0, 0, 0, 1,
                1, 1, 1, 1, 1,
                0, 0, 0, 0, 1,
                1, 1, 1, 1, 1,
        });

        FilamentFinder finder = new FilamentFinder();
        List<Filament> filaments = finder.process(binary);

        assertThat(filaments.size(), is(2));
        Filament actual1 = filaments.get(0);
        List<Filament.Point> points1 = actual1.getPoints();

        assertThat(points1.get(0), equalTo(new Filament.Point(0, 0)));
        assertThat(points1.get(points1.size() - 1), equalTo(new Filament.Point(0, 4)));

        Filament actual2 = filaments.get(1);
        List<Filament.Point> points2 = actual2.getPoints();

        assertThat(points2.get(0), equalTo(new Filament.Point(0, 2)));
        assertThat(points2.get(points2.size() - 1), equalTo(new Filament.Point(3, 2)));
    }

    @Test
    public void should_find_one_line_for_a_tilted_U() {
        binary.setData(new byte[]{
                0, 0, 1, 0, 0,
                0, 0, 0, 1, 0,
                1, 0, 0, 0, 1,
                0, 1, 0, 1, 0,
                0, 0, 1, 0, 0,
        });

        FilamentFinder finder = new FilamentFinder();
        List<Filament> filaments = finder.process(binary);

        assertThat(filaments.size(), is(1));
        Filament actual1 = filaments.get(0);
        List<Filament.Point> points1 = actual1.getPoints();

        assertThat(points1.get(0), equalTo(new Filament.Point(2, 0)));
        assertThat(points1.get(points1.size() - 1), equalTo(new Filament.Point(0, 2)));


    }

    @Test
    public void should_find_three_line_for_a_X() {
        binary.setData(new byte[]{
                1, 0, 0, 0, 1,
                0, 1, 0, 1, 0,
                0, 0, 1, 0, 0,
                0, 1, 0, 1, 0,
                1, 0, 0, 0, 1,
        });

        FilamentFinder finder = new FilamentFinder();
        List<Filament> filaments = finder.process(binary);

        assertThat(filaments.size(), is(3));
        Filament actual1 = filaments.get(0);
        List<Filament.Point> points1 = actual1.getPoints();

        assertThat(points1.get(0), equalTo(new Filament.Point(0, 0)));
        assertThat(points1.get(points1.size() - 1), equalTo(new Filament.Point(4, 0)));

        Filament actual2 = filaments.get(1);
        List<Filament.Point> points2 = actual2.getPoints();
        assertThat(points2.get(0), equalTo(new Filament.Point(1, 3)));
        assertThat(points2.get(points2.size() - 1), equalTo(new Filament.Point(0, 4)));

        Filament actual3 = filaments.get(2);
        List<Filament.Point> points3 = actual3.getPoints();
        assertThat(points3.get(0), equalTo(new Filament.Point(3, 3)));
        assertThat(points3.get(points3.size() - 1), equalTo(new Filament.Point(4, 4)));
    }
}
