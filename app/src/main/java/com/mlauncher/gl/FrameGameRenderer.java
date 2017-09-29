package com.mlauncher.gl;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by daba on 2016-09-28.
 */

public class FrameGameRenderer extends GameRenderer {

    private FrameSurface surface;

    public FrameGameRenderer(Context context, Listener listener) {
        super(context, listener);
    }

    @Override
    public void createSurface(Context context) {
        surface = new FrameSurface(context);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // for wallpaper
        gl.glTranslatef(-42f, -55.0f, -350.0f);

        // for dots
        //gl.glTranslatef(-55f, -45.0f, -270.0f);

        gl.glRotatef(getTiltX(), 1, 0, 0);

        surface.draw(gl);
        increaseFrames();
    }
}