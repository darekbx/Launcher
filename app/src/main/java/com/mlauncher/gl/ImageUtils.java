package com.mlauncher.gl;

import android.graphics.Bitmap;

public class ImageUtils {

	public static int[][] extract(Bitmap image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] colors = new int[width][height];

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				colors[x][y] = image.getPixel(x, y);

		return colors;
	}

	public static int[] extractFlat(Bitmap image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] colors = new int[width * height];
		int index = 0;

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				colors[index++] = image.getPixel(x, y);

		return colors;
	}
}