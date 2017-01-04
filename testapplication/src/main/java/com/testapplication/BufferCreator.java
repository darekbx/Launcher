package com.testapplication;

import android.graphics.Color;

/**
 * Created by daba on 2016-12-15.
 */

public class BufferCreator {

    private static final int NOISE_SIZE = 30;

    public float[] createVertexBufferFlat(int[] positions, int[] imageData, int imageSize) {
        final int length = positions.length;
        final int count = (length / 2) * 3;
        final float[] vertexBuffer = new float[count];
        int index = 0;

        for (int i = 0; i < count; i += 3) {
            int positionX = positions[index++];
            int positionY = positions[index++];
            vertexBuffer[i + 0] = positionX;
            vertexBuffer[i + 1] = positionY;

            int colorValue = imageData[positionY * imageSize + positionX];
            int height = translateColorToHeight(colorValue);
            vertexBuffer[i + 2] = height;
        }

        return vertexBuffer;
    }

    public float[] createColorBufferFlat(int[] positions, int[] imageData, int imageSize) {
        final int length = positions.length;
        final int count = length * 2;
        final float[] colorBuffer = new float[count];
        int index = 0;

        for (int i = 0; i < count; i += 4) {
            int positionX = positions[index++];
            int positionY = positions[index++];
            int colorValue = imageData[positionY * imageSize + positionX];
            int[] extractedColor = extractColor(colorValue);

            for (int j = 0; j <= 3; j++) {
                colorBuffer[i + j] = extractedColor[j] / 255f;
            }
        }

        return colorBuffer;
    }

    public int[] extractColor(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = Color.alpha(color);
        return new int[] { red, green, blue, alpha };
    }

    public int translateColorToHeight(int color) {
        int[] colors = extractColor(color);
        return (((colors[0] + colors[1] + colors[2]) / 3) * randomNoise()) / 255;
    }

    public int randomNoise() {
        return (int)(Math.random() * NOISE_SIZE);
    }
}