package com.mlauncher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

import com.mlauncher.R;

/**
 * Created by INFOR PL on 2015-07-16.
 */
public class FilterButton extends Button {

    private static final float RADIUS = 3f;

    private Paint paint;
    private int count;

    public FilterButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(context.getResources().getColor(R.color.font_default));
    }

    public void setCount(int value) {
        count = value;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(2f, 2f);
        float padding = (int)(RADIUS + RADIUS / 2f);
        for (int i = 0; i < count; i++) {
            canvas.drawCircle((padding + padding) * i + padding, padding, RADIUS, paint);
        }
    }
}