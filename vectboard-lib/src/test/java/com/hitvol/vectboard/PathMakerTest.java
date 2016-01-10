package com.hitvol.vectboard;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by archevel on 2015-11-15.
 */
public class PathMakerTest {

    private PathMaker maker = new PathMaker();

    @Test
    public void should_make_an_empty_filament_into_a_empty_path_tag() {
        Filament f = new Filament();
        String pathTag = maker.asPathTag(f);

        assertThat(pathTag, is("<path fill=\"none\" stroke=\"black\"/>"));
    }

    @Test
    public void should_start_path_at_first_filament_point() {
        Filament f = new Filament();
        f.add(10, 10);
        String pathTag = maker.asPathTag(f);

        assertThat(pathTag, startsWith("<path "));
        assertThat(pathTag, endsWith("/>"));
        assertThat(pathTag, containsString("d=\"M 10 10\""));
    }

    @Test
    public void should_end_path_at_last_filament_point() {
        Filament f = new Filament();
        f.add(10, 10);
        f.add(90, 90);
        String pathTag = maker.asPathTag(f);

        assertThat(pathTag, startsWith("<path "));
        assertThat(pathTag, endsWith("/>"));
        assertThat(pathTag, containsString("90 90\""));
    }

    @Test
    public void should_include_all_intermediary_points_of_filament() {
        Filament f = new Filament();
        f.add(10, 10);
        f.add(50, 50);
        f.add(60, 60);
        f.add(70, 70);
        f.add(80, 80);
        f.add(90, 90);
        String pathTag = maker.asPathTag(f);

        assertThat(pathTag, startsWith("<path "));
        assertThat(pathTag, endsWith("/>"));
        assertThat(pathTag, containsString("50 50 60 60 70 70 80 80"));
    }

    @Test
    public void should_create_path_with_no_fill_and_black_stroke() {
        Filament f = new Filament();
        f.add(10, 10);
        f.add(50, 50);
        f.add(60, 60);
        f.add(70, 70);
        f.add(80, 80);
        f.add(90, 90);
        String pathTag = maker.asPathTag(f);

        assertThat(pathTag, startsWith("<path "));
        assertThat(pathTag, endsWith("/>"));
        assertThat(pathTag, containsString("fill=\"none\""));
        assertThat(pathTag, containsString("stroke=\"black\""));
    }

}

