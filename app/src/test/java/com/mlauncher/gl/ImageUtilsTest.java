package com.mlauncher.gl;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.mlauncher.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by daba on 2016-09-30.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ImageUtilsTest {

    @Test
    public void extract_image() {
        Bitmap bitmap = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888);
        bitmap.setPixel(0, 0, Color.RED);
        bitmap.setPixel(1, 1, Color.BLUE);
        bitmap.setPixel(2, 2, Color.YELLOW);
        bitmap.setPixel(3, 3, Color.BLACK);

        int[][] imageData = ImageUtils.extract(bitmap);

        assertEquals(imageData[0][0], Color.RED);
        assertEquals(imageData[1][1], Color.BLUE);
        assertEquals(imageData[2][2], Color.YELLOW);
        assertEquals(imageData[3][3], Color.BLACK);
    }

    @Test
    public void extract_image_flat() {
        int imageSize = 4;
        Bitmap bitmap = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888);
        bitmap.setPixel(0, 0, Color.RED);
        bitmap.setPixel(1, 1, Color.BLUE);
        bitmap.setPixel(2, 2, Color.YELLOW);
        bitmap.setPixel(3, 3, Color.BLACK);

        int[] imageData = ImageUtils.extractFlat(bitmap);

        assertEquals(imageData[0], Color.RED);
        assertEquals(imageData[1 * imageSize + 1], Color.BLUE);
        assertEquals(imageData[2 * imageSize + 2], Color.YELLOW);
        assertEquals(imageData[3 * imageSize + 3], Color.BLACK);
    }
}
