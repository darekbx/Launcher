package com.testapplication;

/**
 * Created by daba on 2016-12-15.
 */

public class NativeBufferCreator {

    public native int[] extractColor(int color);
    public native float translateColorToHeight(int color);
    public native int randomNoise();
    public native float[] createVertexBufferFlat(int[] positions, int[] imageData, int imageSize);
    public native float[] createColorBufferFlat(int[] positions, int[] imageData, int imageSize);

    static {
        System.loadLibrary("native-lib");
    }
}
