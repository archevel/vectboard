package com.hitvol.vectboard;

import boofcv.core.image.border.ImageBorderValue;
import boofcv.core.image.border.ImageBorder_S32;
import boofcv.struct.image.ImageUInt8;
import org.ddogleg.struct.GrowQueue_I32;

/**
 * Based of of boofcv.alg.filter.binary.iml.BinaryThinning.
 * The modifications of the filters are made so they match
 * the ones described at http://homepages.inf.ed.ac.uk/rbf/HIPR2/thin.htm
 * Both the border filters (i.e. the byte[] masks) and Mask subclasses
 * have been changed accordingly.
 */
class BinaryThinningWithFixedMasks {

    // -1 means any pixel value is ok
    public static byte mask0[] = new byte[]{
            0, 0, 0,
            -1, 1, -1,
            1, 1, 1};
    public static byte mask1[] = new byte[]{
            -1, 0, 0,
            1, 1, 0,
            -1, 1, -1};
    public static byte mask2[] = new byte[]{
            1, -1, 0,
            1, 1, 0,
            1, -1, 0};
    public static byte mask3[] = new byte[]{
            -1, 1, -1,
            1, 1, 0,
            -1, 0, 0};
    public static byte mask4[] = new byte[]{
            1, 1, 1,
            -1, 1, -1,
            0, 0, 0};
    public static byte mask5[] = new byte[]{
            -1, 1, -1,
            0, 1, 1,
            0, 0, -1};
    public static byte mask6[] = new byte[]{
            0, -1, 1,
            0, 1, 1,
            0, -1, 1};
    public static byte mask7[] = new byte[]{
            0, 0, -1,
            0, 1, 1,
            -1, 1, -1};

    Mask masks[] = new Mask[]{
            new Mask0(), new Mask1(), new Mask2(), new Mask3(),
            new Mask4(), new Mask5(), new Mask6(), new Mask7()};

    // reference to input image
    ImageUInt8 binary;
    // all pixels outside the image are set to 0
    ImageBorder_S32<ImageUInt8> inputBorder = ImageBorderValue.wrap(binary, 0);
    // list of one valued pixels, input
    GrowQueue_I32 ones0 = new GrowQueue_I32();
    GrowQueue_I32 ones1 = new GrowQueue_I32();

    // list of pixels which need to be set to zero
    GrowQueue_I32 zerosOut = new GrowQueue_I32();

    /**
     * Applies the thinning algorithm.  Runs for the specified number of loops or until no change is detected.
     *
     * @param binary   Input binary image which is to be thinned.  This is modified
     * @param maxLoops Maximum number of thinning loops.  Set to -1 to run until the image is no longer modified.
     */
    public void apply(ImageUInt8 binary, int maxLoops) {
        this.binary = binary;
        inputBorder.setImage(binary);

        ones0.reset();
        zerosOut.reset();

        findOnePixels(ones0);

        for (int loop = 0; (loop < maxLoops || maxLoops == -1) && ones0.size > 0; loop++) {
            // do one cycle through all the masks

            int numZero = 0;
            for (int i = 0; i < masks.length; i++) {
                zerosOut.reset();
                Mask mask = masks[i];
                mask.apply(ones0, zerosOut);

                if (zerosOut.size() == 0) {
                    numZero++;
                } else {
                    // mark all the pixels that need to be set to 0 as 0
                    for (int j = 0; j < zerosOut.size(); j++) {
                        int index = zerosOut.get(j);
                        binary.data[index] = 0;
                    }
                }
            }

            if (numZero == masks.length)
                break;

            // update the ones list
            ones1.reset();
            for (int i = 0; i < ones0.size(); i++) {
                if (binary.data[ones0.get(i)] == 1) {
                    ones1.add(ones0.get(i));
                }
            }

            // swap the lists
            GrowQueue_I32 tmp = ones0;
            ones0 = ones1;
            ones1 = tmp;
        }
    }

    /**
     * Scans through the image and record the array index of all marked pixels
     */
    protected void findOnePixels(GrowQueue_I32 ones) {
        for (int y = 0; y < binary.height; y++) {
            int index = binary.startIndex + y * binary.stride;
            for (int x = 0; x < binary.width; x++, index++) {
                if (binary.data[index] != 0) {
                    ones.add(index);
                }
            }
        }
    }

    /**
     * Abstract class for applying the mask.  Uses sparse list of black pixels.  Determines if the pixel is along
     * the border or not.  If border then generic code is code, otherwise opimized code is called.
     */
    protected abstract class Mask {

        byte mask[];

        public Mask(byte[] mask) {
            this.mask = mask;
        }

        /**
         * @param onesIn   (input) Indexes of pixels with a value of 1
         * @param zerosOut (output) Indexes of pixels whose values have changed form 1 to 0
         */
        public void apply(GrowQueue_I32 onesIn, GrowQueue_I32 zerosOut) {
            int w = binary.width - 1;
            int h = binary.height - 1;

            for (int i = 0; i < onesIn.size; i++) {
                int indexIn = onesIn.get(i);
                int x = (indexIn - binary.startIndex) % binary.stride;
                int y = (indexIn - binary.startIndex) / binary.stride;

                boolean staysAsOne;
                if (x == 0 || x == w || y == 0 || y == h) {
                    staysAsOne = borderMask(x, y);
                } else {
                    staysAsOne = innerMask(indexIn);
                }
                if (!staysAsOne) {
                    // yes some pixels will be marked more than once.  less memory and maybe faster than
                    // having  second binary image to keep track of what was already marked
                    zerosOut.add(indexIn);
                }
            }
        }

        /**
         * Slower code which uses a generic mask to handle image border
         *
         * @param cx x-coordinate of center pixels, always 1
         * @param cy y-coordinate of center pixels, always 1
         * @return true means the pixels keeps the value of one, otherwise it is set to zero
         */
        protected boolean borderMask(int cx, int cy) {

            int maskIndex = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int m = mask[maskIndex++];
                    if (m == -1) continue;
                    int pixel = inputBorder.get(cx + j, cy + i);
                    if (m == 0) {
                        if (pixel != 0)
                            return true;
                    } else if (pixel != 1)
                        return true;
                }
            }

            return false;
        }

        /**
         * Specialized code optimized for the inner image
         *
         * @param indexIn Index of center pixels, always 1
         * @return true means the pixels keeps the value of one, otherwise it is set to zero
         */
        protected abstract boolean innerMask(int indexIn);

    }

    public class Mask0 extends Mask {

        public Mask0() {
            super(mask0);
        }

        @Override
        protected boolean innerMask(int indexIn) {
            int rowTop = indexIn - binary.stride;
            if (binary.data[rowTop - 1] != 0 || binary.data[rowTop] != 0 || binary.data[rowTop + 1] != 0) {
                return true;
            }
            int rowBottom = indexIn + binary.stride;
            if (binary.data[indexIn] != 1 ||
                    binary.data[rowBottom - 1] != 1 ||
                    binary.data[rowBottom] != 1 ||
                    binary.data[rowBottom + 1] != 1) {
                return true;
            }
            return false;
        }
    }

    public class Mask1 extends Mask {

        public Mask1() {
            super(mask1);
        }

        @Override
        protected boolean innerMask(int indexIn) {
            int rowTop = indexIn - binary.stride;
            int rowBottom = indexIn + binary.stride;

            if (binary.data[indexIn - 1] != 1 ||
                    binary.data[indexIn] != 1 ||
                    binary.data[rowBottom] != 1) {
                return true;
            }

            if (binary.data[rowTop] != 0 || binary.data[rowTop + 1] != 0 || binary.data[indexIn + 1] != 0) {
                return true;
            }

            return false;
        }
    }

    public class Mask2 extends Mask {

        public Mask2() {
            super(mask2);
        }

        @Override
        protected boolean innerMask(int indexIn) {
            int rowTop = indexIn - binary.stride;
            if (binary.data[indexIn - 1] != 1 || binary.data[rowTop - 1] != 1 || binary.data[rowTop + 1] != 0) {
                return true;
            }

            int rowBottom = indexIn + binary.stride;
            if (binary.data[indexIn] != 1 || binary.data[indexIn + 1] != 0 ||
                    binary.data[rowBottom - 1] != 1 || binary.data[rowBottom + 1] != 0) {
                return true;
            }

            return false;
        }
    }

    public class Mask3 extends Mask {

        public Mask3() {
            super(mask3);
        }

        @Override
        protected boolean innerMask(int indexIn) {
            int rowTop = indexIn - binary.stride;
            if (binary.data[indexIn] != 1 || binary.data[indexIn - 1] != 1 || binary.data[rowTop] != 1) {
                return true;
            }

            int rowBottom = indexIn + binary.stride;
            if (binary.data[indexIn + 1] != 0 || binary.data[rowBottom] != 0 || binary.data[rowBottom + 1] != 0) {
                return true;
            }

            return false;
        }
    }

    public class Mask4 extends Mask {

        public Mask4() {
            super(mask4);
        }

        @Override
        protected boolean innerMask(int indexIn) {
            int rowTop = indexIn - binary.stride;
            if (binary.data[indexIn] != 1 ||
                    binary.data[rowTop - 1] != 1 ||
                    binary.data[rowTop] != 1 ||
                    binary.data[rowTop + 1] != 1) {
                return true;
            }

            int rowBottom = indexIn + binary.stride;
            if (binary.data[rowBottom - 1] != 0 || binary.data[rowBottom] != 0 || binary.data[rowBottom + 1] != 0) {
                return true;
            }

            return false;
        }
    }

    public class Mask5 extends Mask {

        public Mask5() {
            super(mask5);
        }

        @Override
        protected boolean innerMask(int indexIn) {
            int rowTop = indexIn - binary.stride;
            if (binary.data[indexIn] != 1 ||
                    binary.data[indexIn - 1] != 0 ||
                    binary.data[indexIn + 1] != 1 ||
                    binary.data[rowTop] != 1) {
                return true;
            }

            int rowBottom = indexIn + binary.stride;
            if (binary.data[rowBottom - 1] != 0 || binary.data[rowBottom] != 0) {
                return true;
            }

            return false;
        }
    }

    public class Mask6 extends Mask {

        public Mask6() {
            super(mask6);
        }

        @Override
        protected boolean innerMask(int indexIn) {
            int rowTop = indexIn - binary.stride;
            if (binary.data[indexIn] != 1 ||
                    binary.data[indexIn - 1] != 0 ||
                    binary.data[indexIn + 1] != 1 ||
                    binary.data[rowTop - 1] != 0 ||
                    binary.data[rowTop + 1] != 1) {
                return true;
            }

            int rowBottom = indexIn + binary.stride;
            if (binary.data[rowBottom - 1] != 0 || binary.data[rowBottom + 1] != 1) {
                return true;
            }

            return false;
        }
    }

    public class Mask7 extends Mask {

        public Mask7() {
            super(mask7);
        }

        @Override
        protected boolean innerMask(int indexIn) {
            int rowTop = indexIn - binary.stride;
            if (binary.data[indexIn] != 1 ||
                    binary.data[rowTop - 1] != 0 ||
                    binary.data[rowTop] != 0 ||
                    binary.data[indexIn - 1] != 0 ||
                    binary.data[indexIn + 1] != 1) {
                return true;
            }

            int rowBottom = indexIn + binary.stride;
            if (binary.data[rowBottom] != 1) {
                return true;
            }

            return false;
        }
    }
}
