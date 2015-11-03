package com.hitvol.vectboard;

import boofcv.struct.image.ImageUInt8;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class NaiveDistanceTransformerTest {


    @Test
    public void should_calculate_distance_to_background_for_simple_case() {
        ImageUInt8 binary = new ImageUInt8(1, 2);
        binary.setData(new byte[]{1, 0});

        NaiveDistanceTransformer dt = new NaiveDistanceTransformer();

        ImageUInt8 output = dt.process(binary);

        assertEquals(1, output.getData()[0]);
        assertEquals(0, output.getData()[1]);
    }

    @Test
    public void should_calculate_distance_to_background_if_there_is_several_steps() {

        ImageUInt8 binary = new ImageUInt8(1, 3);
        binary.setData(new byte[] {1,1,0});

        NaiveDistanceTransformer dt = new NaiveDistanceTransformer();

        ImageUInt8 output = dt.process(binary);

        assertEquals(2,output.getData()[0]);
        assertEquals(1,output.getData()[1]);
        assertEquals(0,output.getData()[2]);
    }

    @Test
    public void should_calculate_distance_to_background_horizontally() {

        ImageUInt8 binary = new ImageUInt8(3,1);
        binary.setData(new byte[] {1,1,0});

        NaiveDistanceTransformer dt = new NaiveDistanceTransformer();

        ImageUInt8 output = dt.process(binary);

        assertEquals(2,output.getData()[0]);
        assertEquals(1,output.getData()[1]);
        assertEquals(0,output.getData()[2]);
    }

    @Test
    public void should_calculate_distance_to_background_diagonally() {
        ImageUInt8 binary = new ImageUInt8(3, 3);
        binary.setData(new byte[] {
                1,1,1,
                1,1,1,
                1,1,0
        });

        NaiveDistanceTransformer dt = new NaiveDistanceTransformer();

        ImageUInt8 output = dt.process(binary);
        byte[] expected = new byte[] {
                2,2,2,
                2,1,1,
                2,1,0
        };
        assertArrayEquals(expected, output.data);
    }

    @Test
    public void should_calculate_distance_to_background_iteratively() {

        ImageUInt8 binary = new ImageUInt8(4, 3);
        binary.setData(new byte[] {
                1,1,1,1,
                1,1,1,1,
                1,1,1,0
        });

        NaiveDistanceTransformer dt = new NaiveDistanceTransformer();

        ImageUInt8 output = dt.process(binary);
        byte[] expected = new byte[] {
                3,2,2,2,
                3,2,1,1,
                3,2,1,0
        };
        assertArrayEquals(expected, output.data);
    }

    @Test
    public void should_handle_inputs_that_never_fully_erodes() {
        ImageUInt8 binary = new ImageUInt8(3, 3);
        binary.setData(new byte[] {
                1,1,1,
                1,1,1,
                1,1,1
        });

        NaiveDistanceTransformer dt = new NaiveDistanceTransformer();

        ImageUInt8 output = dt.process(binary);
        byte b = Byte.MAX_VALUE;
        byte[] expected = new byte[] {
                b,b,b,
                b,b,b,
                b,b,b
        };
        assertArrayEquals(expected, output.data);

    }

}
