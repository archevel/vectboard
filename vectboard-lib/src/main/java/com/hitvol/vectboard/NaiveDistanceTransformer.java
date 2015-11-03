package com.hitvol.vectboard;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.misc.PixelMath;
import boofcv.core.image.ConvertImage;
import boofcv.struct.image.ImageUInt16;
import boofcv.struct.image.ImageUInt8;

public class NaiveDistanceTransformer {

    public ImageUInt8 process(ImageUInt8 binary) {
        ImageUInt8 eroded = new ImageUInt8(binary.width, binary.height);
        ImageUInt16 tmp = new ImageUInt16(binary.width, binary.height);

        ImageUInt8 tmp2 = binary.clone();

        int i = 1;
        do {
            BinaryImageOps.erode8(binary, i++, eroded);
            PixelMath.add(eroded, tmp2, tmp);
            ConvertImage.convert(tmp, tmp2);
        } while (shouldKeepEroding(eroded.data) && i < Byte.MAX_VALUE);

        return tmp2;
    }

    private boolean shouldKeepEroding(byte[] erodedData) {
        for(int i = 0; i < erodedData.length; i++) {
            if(erodedData[i] > 0) {
                return true;
            }
        }

        return false;
    }
}
