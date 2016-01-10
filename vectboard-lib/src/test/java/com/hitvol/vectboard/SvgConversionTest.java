package com.hitvol.vectboard;

import boofcv.abst.distort.FDistort;
import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.alg.misc.ImageMiscOps;
import boofcv.alg.misc.PixelMath;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.ImageBase;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by archevel on 2015-11-15.
 */
public class SvgConversionTest {
    @Test
    public void should_create_paths_for_piglet() {
        ImageFloat32 input = TestUtils.imageFromResource("piglet.png");
        ImageUInt8 binary = new ImageUInt8(input.width, input.height);
        ImageUInt8 output = new ImageUInt8(input.width, input.height);

        double threshold = GThresholdImageOps.computeOtsu(input, 0, 256);
        ThresholdImageOps.threshold(input, binary, (float) threshold, true);

        BinaryImageOps.thin(binary, -1, output);
        //BinaryThinningWithFixedMasks thinner = new BinaryThinningWithFixedMasks();
        //thinner.apply(binary, -1);
        FilamentFinder finder = new FilamentFinder();
        List<Filament> filaments = finder.process(output);

        FilamentJoiner joiner = new FilamentJoiner();
        List<Filament> joinedFilaments = joiner.joinAdjacent(filaments, 10);
        List<Filament> outFilaments = new LinkedList<Filament>();
        for(Filament f : joinedFilaments) {
            outFilaments.add(RamerDouglasPeuckerFilamentSimplifier.simplifyFilament(f, 10));
        }
        print(outFilaments, input);
    }

    @Test
    public void should_create_paths_for_test09() {
        ImageFloat32 raw = TestUtils.imageFromResource("test00.jpg");

        // 8000 / 8 = 1000
        // 1000 / 8000 = 1/8
        // 8000 * (1/8) = 1000

        double scaleFactor = 1000d / raw.width;
        int scaledWidth = (int) Math.round(raw.width * scaleFactor);
        int scaledHeight = (int) Math.round(raw.height * scaleFactor);
        ImageFloat32 input = new ImageFloat32(scaledWidth, scaledHeight);

        new FDistort(raw, input).scaleExt().apply();

        ImageUInt8 binary = new ImageUInt8(input.width, input.height);
        ImageUInt8 output = new ImageUInt8(input.width, input.height);


        ThresholdImageOps.threshold(input, binary, 150f, true);
        //BinaryImageOps.erode8(binary, 1, output);
        //BinaryImageOps.dilate8(output, 1, binary);
        BinaryImageOps.removePointNoise(binary, output);

        //BinaryImageOps.thin(binary, -1, output);
        //BinaryThinningWithFixedMasks thinner = new BinaryThinningWithFixedMasks();
        //thinner.apply(binary, -1);
        BinaryImageOps.thin(output, -1, binary);
        FilamentFinder finder = new FilamentFinder();
        List<Filament> filaments = finder.process(binary);
        FilamentJoiner joiner = new FilamentJoiner();
        List<Filament> joinedFilaments = joiner.joinAdjacent(filaments, 10);
        List<Filament> outFilaments = new LinkedList<Filament>();
        for(Filament f : joinedFilaments) {
            outFilaments.add(RamerDouglasPeuckerFilamentSimplifier.simplifyFilament(f, 10));
        }
        print(outFilaments, input);

        //PixelMath.multiply(output, 255, output);

        //BufferedImage dst = new BufferedImage(output.width,output.height,BufferedImage.TYPE_BYTE_GRAY);
        //ConvertBufferedImage.convertTo(output, dst);

        //UtilImageIO.saveImage(dst, "test09_out.png");
    }

    void print(List<Filament> filaments, ImageBase input) {
        PathMaker maker = new PathMaker();
        System.out.println("<?xml version=\"1.0\" standalone=\"no\"?>\n" +
                "\n" +
                "<svg width=\""+input.width + "px\" height=\"" + input.height + "px\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">");
        for(Filament filament : filaments) {
            System.out.println(maker.asPathTag(filament));
        }
        System.out.println("</svg>");
    }
}
