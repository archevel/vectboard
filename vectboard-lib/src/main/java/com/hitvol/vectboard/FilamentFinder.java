package com.hitvol.vectboard;

import boofcv.core.image.border.ImageBorderValue;
import boofcv.core.image.border.ImageBorder_S32;
import boofcv.struct.image.ImageUInt8;
import com.google.common.primitives.Bytes;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by archevel on 2015-11-14.
 */
public class FilamentFinder {
    public static final byte FOREGROUND = 1;
    public static final byte BACKGROUND = 0;
    public List<Filament> process(ImageUInt8 binary) {
        List<Filament> result = new LinkedList<>();

        Filament f = findFilament(binary, 0);
        while(f != null) {
            result.add(f);
            f = findFilament(binary, 0);
        }
        return result;
    }

    private Filament findFilament(ImageUInt8 binary, int scanFrom) {
        byte[] data = binary.getData();

        ImageBorder_S32<ImageUInt8> inputBorder = ImageBorderValue.wrap(binary, 0);

        int idx = Bytes.indexOf(binary.getData(), FOREGROUND);
        if(idx >= 0) {
            Filament f = new Filament();
            do {
                data[idx] = BACKGROUND;
                int x = idx % binary.width;
                int y = idx / binary.width;
                f.add(x, y);
                if (x == 0 || x == binary.width - 1|| y == 0 || y == binary.height - 1) {
                    idx = findNeighbourInBorder(inputBorder, idx, x, y);
                } else {
                    idx = findNeighbour(data, idx, binary.stride);
                }
            } while (idx != -1 && idx < data.length);
            return f;
        } else {
            return null;
        }
    }

    private int findNeighbourInBorder(ImageBorder_S32<ImageUInt8> inputBorder, int center, int x, int y) {
        int stride = inputBorder.getImage().stride;
        int topCenter = center - stride;

        if(inputBorder.get(x, y - 1) == FOREGROUND) {
            return topCenter;
        }
        if(inputBorder.get(x + 1, y - 1) == FOREGROUND) {
            return topCenter + 1;
        }


        if(inputBorder.get(x + 1, y) == FOREGROUND) {
            return center + 1;
        }

        int bottomCenter = center + stride;

        if(inputBorder.get(x + 1, y + 1) == FOREGROUND) {
            return bottomCenter + 1;
        }
        if(inputBorder.get(x, y + 1) == FOREGROUND) {
            return bottomCenter;
        }
        if(inputBorder.get(x - 1, y + 1) == FOREGROUND) {
            return bottomCenter - 1;
        }

        if(inputBorder.get(x - 1, y) == FOREGROUND) {
            return center - 1;
        }

        if(inputBorder.get(x - 1, y - 1) == FOREGROUND) {
            return topCenter - 1;
        }

        return -1;
    }

    private int findNeighbour(byte[] data, int center, int stride) {
        int topCenter = center - stride;

        if (data[topCenter] == FOREGROUND) {
            return topCenter;
        }
        if(data[topCenter + 1] == FOREGROUND) {
            return topCenter + 1;
        }


        if(data[center + 1] == FOREGROUND) {
            return center + 1;
        }

        int bottomCenter = center + stride;
        if(data[bottomCenter + 1] == FOREGROUND) {
            return bottomCenter + 1;
        }
        if(data[bottomCenter] == FOREGROUND) {
            return bottomCenter;
        }
        if(data[bottomCenter - 1] == FOREGROUND) {
            return bottomCenter - 1;
        }

        if(data[center - 1] == FOREGROUND) {
            return center - 1;
        }
        if(data[topCenter - 1] == FOREGROUND) {
            return topCenter - 1;
        }

        return -1;
    }
}
