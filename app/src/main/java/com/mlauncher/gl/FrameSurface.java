package com.mlauncher.gl;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mlauncher.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by daba on 2016-09-28.
 */

public class FrameSurface {

    private static final int IMAGE_SIZE = 64;
    private static final int FRAME_COUNT = 7;

    private GridCreator gridCreator;
    private NativeBufferCreator bufferCreator;

    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private final int[] positions;
    private final int triangleCount;

    private List<ImageDataWrapper> imagesData = new ArrayList<>();
    private int iterator = 0;

    private class ImageDataWrapper {
        public int[] imageData;

        public ImageDataWrapper(int[] imageData) {
            this.imageData = imageData;
        }
    }

    public FrameSurface(Context context) {
        gridCreator = new GridCreator();
        bufferCreator = new NativeBufferCreator();

        prepareFrames(context);
        iterator = 0;

        positions = gridCreator.makeGrid(IMAGE_SIZE, IMAGE_SIZE);
        float[] vertices = bufferCreator.createVertexBufferFlat(positions, imagesData.get(0).imageData, IMAGE_SIZE);
        float[] colors = bufferCreator.createColorBufferFlat(positions, imagesData.get(0).imageData, IMAGE_SIZE);

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

    private void prepareFrames(Context context) {
        Resources resources = context.getResources();
        int framesCount = FRAME_COUNT;
        imagesData = new ArrayList<>(FRAME_COUNT);
        for (int i = 0; i <= framesCount; i++) {
            int resourceId = resources.getIdentifier("f" + i, "drawable", context.getPackageName());
            Bitmap bitmap =  BitmapFactory.decodeResource(resources, resourceId);
            int[] imageData = ImageUtils.extractFlat(bitmap);
            bitmap.recycle();
            imagesData.add(new ImageDataWrapper(imageData));
        }
    }

    public void refresh() {
        int[] imageData = imagesData.get(iterator).imageData;
        vertexBuffer.clear();
        vertexBuffer.put(bufferCreator.createVertexBufferFlat(positions, imageData, IMAGE_SIZE));
        vertexBuffer.position(0);

        colorBuffer.clear();
        colorBuffer.put(bufferCreator.createColorBufferFlat(positions, imageData, IMAGE_SIZE));
        colorBuffer.position(0);

        if (++iterator > FRAME_COUNT) {
            iterator = 0;
        }
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