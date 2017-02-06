package com.mlauncher.gl;

import android.graphics.Color;

import com.mlauncher.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by daba on 2016-12-15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class BufferCreatorTest {

    private static final float[] VERTEX_RESULT = new float[] {
            0.0F,0.0F,1.0F,0.0F,1.0F,1.0F,1.0F,0.0F,1.0F,1.0F,
            1.0F,1.0F,0.0F,0.0F,1.0F,0.0F,0.0F,1.0F,0.0F,0.0F,
            1.0F,0.0F,0.0F,1.0F,0.0F,0.0F,1.0F,0.0F,0.0F,1.0F,
            0.0F,0.0F,1.0F,0.0F,0.0F,1.0F };
    
    private static final int[] VERTEX_RESULT_INT = new int[] {
            0,0,1,0,1,1,1,0,1,1,
            1,1,0,0,1,0,0,1,0,0,
            1,0,0,1,0,0,1,0,0,1,
            0,0,1,0,0,1 };

    @Test
    public void createVertexBuffer() {
        BufferCreator bufferCreator = spy(new BufferCreator());
        when(bufferCreator.randomNoise()).thenReturn(1);
        when(bufferCreator.translateColorToHeight(anyInt())).thenReturn(1);

        int[] grid = new GridCreator().makeGrid(2, 2);
        int[][] imageData = new int [][] { { 1, 2 }, { 2, 3 } };

        float[] result = bufferCreator.createVertexBuffer(grid, imageData);

        assertArrayEquals(VERTEX_RESULT, result, 0f);
    }

    @Test
    public void createVertexBuffer_flat() {
        BufferCreator bufferCreator = spy(new BufferCreator());
        when(bufferCreator.randomNoise()).thenReturn(1);
        when(bufferCreator.translateColorToHeight(anyInt())).thenReturn(1);

        int[] grid = new GridCreator().makeGrid(2, 2);
        int[] imageDataFlat = new int [] {  1, 2, 2, 3  };

        float[] result = bufferCreator.createVertexBufferFlat(grid, imageDataFlat, 2);

        assertArrayEquals(VERTEX_RESULT, result, 0f);
    }

    @Test
    public void extract_color() {
        BufferCreator bufferCreator = spy(new BufferCreator());
        int color = Color.argb(100, 250, 200, 150);
        int[] colorData = bufferCreator.extractColor(color);

        assertEquals(colorData[0], 250);
        assertEquals(colorData[1], 200);
        assertEquals(colorData[2], 150);
        assertEquals(colorData[3], 100);
    }

    @Test
    public void translate_color_to_height() {
        BufferCreator bufferCreator = spy(new BufferCreator());
        int color = Color.argb(100, 250, 200, 150);
        int height = bufferCreator.translateColorToHeight(color);

        assertTrue(height > 6 && height < 19);
    }

    @Test
    public void random_noise() {
        BufferCreator bufferCreator = spy(new BufferCreator());
        int noise = bufferCreator.randomNoise();

        assertTrue(noise > 0 && noise <= 30);
    }
}