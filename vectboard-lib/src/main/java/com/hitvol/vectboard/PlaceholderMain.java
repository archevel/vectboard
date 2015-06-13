package com.hitvol.vectboard;

import boofcv.alg.feature.detect.edge.CannyEdge;
import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.factory.feature.detect.edge.FactoryEdgeDetectors;
import boofcv.struct.ConnectRule;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;

import java.util.List;

public class PlaceholderMain {
    public static void main(String[] args) {
        System.out.println("Hello!");
        ImageFloat32 input = null;
        ImageUInt8 binary = new ImageUInt8(input.width,input.height);

        // Finds edges inside the image
        CannyEdge<ImageFloat32,ImageFloat32> canny =
                FactoryEdgeDetectors.canny(2, false, true, ImageFloat32.class, ImageFloat32.class);

        canny.process(input, 0.1f, 0.3f, binary);

        List<Contour> contours = BinaryImageOps.contour(binary, ConnectRule.EIGHT, null);
    }
}
