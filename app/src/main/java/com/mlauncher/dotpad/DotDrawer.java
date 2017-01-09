package com.mlauncher.dotpad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.mlauncher.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by daba on 2016-09-30.
 */
public class DotDrawer {

    private static final int BITMAP_SIZE = 80;

    public static Bitmap drawDots(Context context, List<Dot> dots) {
        Bitmap bitmap = Bitmap.createBitmap(BITMAP_SIZE, BITMAP_SIZE, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bitmap);

        canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ring), 0, 0, paint);
        canvas.rotate(180, BITMAP_SIZE / 2, BITMAP_SIZE / 2);

        Collections.reverse(dots);
        int xPadding = BITMAP_SIZE - 6;
        int xPosition = 0;
        int step = 2;

        for (Dot dot : dots) {

            paint.setColor(dot.color);
            paint.setAlpha(110);
            canvas.drawRect(xPadding - xPosition, 0, xPadding - xPosition + step, 0 + dot.size / 2, paint);

            xPosition += step;
            xPosition += 1;

            if (xPosition >= xPadding) {
                break;
            }
        }

        return bitmap;
    }
}