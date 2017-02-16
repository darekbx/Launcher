package com.mlauncher.gl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.support.annotation.DrawableRes;

import com.mlauncher.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by daba on 2016-09-28.
 */

public class GameRenderer implements Renderer {

    private static final int FPS_DELAY = 1;
    private static boolean DISABLE_TILT = true;

    public interface Listener {
        void onFramesPerSecond(int framesPerSecond);
    }

    private Subscription fpsSubscription;
    private Bitmap texture;
    private Surface surface;
    private Context context;
    private float tiltX = -60f;
    private int frames = 0;

    public GameRenderer(Context context, final Listener listener) {
        this.context = context;
        createSurface(context);
        fpsSubscription = Observable
                .just(0)
                .delay(FPS_DELAY, TimeUnit.SECONDS)
                .repeat()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer _) {
                        listener.onFramesPerSecond(frames);
                        frames = 0;
                    }
                });
    }

    public void applySmoke(@DrawableRes int resource) {
       if (surface != null) {
           surface.updateImage(BitmapFactory.decodeResource(context.getResources(), resource));
       }
    }

    protected void createSurface(Context context) {
        texture = BitmapFactory.decodeResource(context.getResources(), R.drawable.ring);
        surface = new Surface(texture);
    }

    public void destroy() {
        if (fpsSubscription!= null) {
            fpsSubscription.unsubscribe();
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        gl.glEnable(GL10.GL_ALPHA_TEST);
        gl.glEnable(GL10.GL_DEPTH_TEST);

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        GLU.gluPerspective(gl, 22.0f, (float) width / (float) height, 0.1f, 1000.0f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // for wallpaper
        gl.glTranslatef(-42f, -55.0f, -300.0f);

        // for dots
        //gl.glTranslatef(-55f, -45.0f, -270.0f);

        gl.glRotatef(getTiltX(), 1, 0, 0);

        surface.draw(gl);
        increaseFrames();
    }

    protected void increaseFrames() {
        frames++;
    }

    protected float getTiltX() {
        return tiltX;
    }

    public void addXTilt(float tiltX) {
        if (DISABLE_TILT) {
            return;
        }
        this.tiltX -= tiltX;
        this.tiltX = Math.max(this.tiltX, -70f);
        this.tiltX = Math.min(this.tiltX, -20f);
    }
}