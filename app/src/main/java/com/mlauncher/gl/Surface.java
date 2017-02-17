package com.mlauncher.gl;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by daba on 2016-09-28.
 */

public class Surface {

    private GridCreator gridCreator;
    private NativeBufferCreator bufferCreator;

    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private final int[] positions;
    private final int triangleCount;
    private int[] imageData;
    private int imageSize;

    public Surface(Bitmap bitmap) {
        gridCreator = new GridCreator();
        bufferCreator = new NativeBufferCreator();

        imageSize = bitmap.getWidth();
        positions = gridCreator.makeGrid(bitmap.getWidth(), bitmap.getHeight());
        imageData = ImageUtils.extractFlat(bitmap);

        float[] vertices = bufferCreator.createVertexBufferFlat(positions, imageData, imageSize);
        float[] colors = bufferCreator.createColorBufferFlat(positions, imageData, imageSize);

        triangleCount = vertices.length / 3;

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();

        ByteBuffer byteColorBuffer = ByteBuffer.allocateDirect(colors.length * 4);
        byteColorBuffer.order(ByteOrder.nativeOrder());
        colorBuffer = byteColorBuffer.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
    }

    public void updateImage(Bitmap bitmap) {
        imageData = ImageUtils.extractFlat(bitmap);
        colorBuffer.clear();
        colorBuffer.put(bufferCreator.createColorBufferFlat(positions, imageData, imageSize));
        colorBuffer.position(0);
    }

    public void refresh() {
        vertexBuffer.clear();
        vertexBuffer.put(bufferCreator.createVertexBufferFlat(positions, imageData, imageSize));
        vertexBuffer.position(0);
    }

    public void draw(GL10 gl) {

        refresh();

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, triangleCount);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }
}