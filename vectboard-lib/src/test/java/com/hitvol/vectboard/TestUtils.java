package com.hitvol.vectboard;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.ImageFloat32;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class TestUtils {

    public static ImageFloat32 imageFromResource(String name) {
        try {
            InputStream is = TestUtils.class.getClassLoader().getResourceAsStream(name);
            BufferedImage bufferedImage = ImageIO.read(is);
            return ConvertBufferedImage.convertFromSingle(bufferedImage, null, ImageFloat32.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
