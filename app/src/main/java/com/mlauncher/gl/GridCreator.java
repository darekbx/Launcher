package com.mlauncher.gl;

/**
 * Created by daba on 2016-09-27.
 */

public class GridCreator {

    private int positionX = -1;
    private int positionY = 0;
    private int offsetmY = 0;
    private int offsetmX = 1;
    private int index = 0;

    private static final int CELL_SIZE = 1;

    public int[] makeGrid(int width, int height) {

        final int count = width * height * 3;
        int[] points = new int[count * 2];
        boolean isFirst = false;

        offsetmY = -CELL_SIZE;
        offsetmX = CELL_SIZE;

        for (int k = 0; k < height - 1; k++) {
            if (k > 0) {
                offsetmX = offsetmX == CELL_SIZE ? -CELL_SIZE : CELL_SIZE;
                isFirst = true;
            }
            fillPart(points, width, isFirst);
        }

        return points;
    }

    private void fillPart(int[] points, int width, boolean isFirst) {
        offsetmY += CELL_SIZE;
        positionX += offsetmX;

        for (int i = 1; i <= width * 2; i++) {
            if (i % 2 == 0) positionY = offsetmY + CELL_SIZE;
            else positionY = offsetmY;

            if (!isFirst) {
                points[index++] = positionX;
                points[index++] = positionY;
            }

            isFirst = false;

            if (i > 1 && i % 2 == 0)
                positionX += offsetmX;
        }
    }
}