package com.mlauncher.view;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import com.mlauncher.logic.ussd.model.AccountBalance;
import com.mlauncher.logic.ussd.model.InternetBalance;
import com.mlauncher.logic.weather.model.WeatherConditions;
import com.mlauncher.model.DayItem;
import com.mlauncher.model.SmokeItem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class DayTimeView extends View {

    private final Paint commonPaint;
    private final Paint airQualityPaint;
    private final Paint warningPaint;

    private DayItem currentDay;

    private SmokeItem pM10;
    private SmokeItem pM2_5;

    private AccountBalance accountBalance;
    private InternetBalance internetBalance;
    private WeatherConditions weatherConditions;

    private int wallpaperFPS;

    private String screenOn;
    private boolean isMobileDataOn;

    public DayTimeView(Context context, AttributeSet attrs) {

        super(context, attrs);

        commonPaint = new Paint();
        commonPaint.setColor(Color.WHITE);
        commonPaint.setAntiAlias(true);
        commonPaint.setTextSize(22);
        commonPaint.setTypeface(Typeface.MONOSPACE);

        airQualityPaint = new Paint(commonPaint);
        airQualityPaint.setTextAlign(Paint.Align.RIGHT);

        warningPaint = new Paint();
        warningPaint.setAntiAlias(true);
        warningPaint.setColor(Color.RED);
        warningPaint.setTextSize(35);
        warningPaint.setTypeface(Typeface.MONOSPACE);
    }

    public void setWeather(WeatherConditions weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public void setCurrentDay(DayItem currentDay) {
        this.currentDay = currentDay;
        invalidate();
    }

    public void setSmokeItems(SmokeItem pm10, SmokeItem pm2_5) {
        pM10 = pm10;
        pM2_5 = pm2_5;
        invalidate();
    }

    public void setScreenOn(String screenOn) {
        this.screenOn = screenOn;
        invalidate();
    }

    public void setWallpaperFPS(int wallpaperFPS) {
        this.wallpaperFPS = wallpaperFPS;
        invalidate();
    }

    public void setIsMobileDataOn(boolean isOn) {
        this.isMobileDataOn = isOn;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (currentDay == null)
            return;
        drawLegend(canvas, currentDay);
        drawAirQualityInfo(canvas);
        drawMobileDataWarning(canvas);
        drawWeather(canvas);
    }

    private void drawMobileDataWarning(Canvas canvas) {
        if (!isMobileDataOn) {
            return;
        }
        canvas.drawText("WARNING! Mobile data is turned ON", 10, 170, warningPaint);
    }

    private void drawLegend(Canvas canvas, DayItem item) {
        int left = 10;

        canvas.drawText("Screen on: " + (screenOn == null ? "00:00:00" : screenOn), left, 40, commonPaint);
        canvas.drawText(item.getSunrise(), left, 70, commonPaint);
        canvas.drawText(item.getSunset(), left, 100, commonPaint);
        canvas.drawText("Seattle: " + getSeattleTime(), left, 130, commonPaint);

        if (accountBalance != null) {
            String message = "Balance: " + accountBalance.amount + " (to " + accountBalance.toDate + ")";
            canvas.drawText(message, left, 160, commonPaint);
        }
        if (internetBalance != null) {
            String message = "Internet: " + internetBalance.dataAmount + " (to " + internetBalance.toDate + ")";
            canvas.drawText(message, left, 190, commonPaint);
        }

        canvas.drawText("FPS: " + wallpaperFPS, left, 1130, commonPaint);

        commonPaint.setColor(Color.WHITE);
    }

    private String getSeattleTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("US/Pacific"));
        return simpleDateFormat.format(calendar.getTime());
    }

    private void drawAirQualityInfo(Canvas canvas) {
        if (pM10 != null && pM2_5 != null) {

            airQualityPaint.setColor(pM10.getColor());
            String mp10Value = "PM 10: " + pM10.value + pM10.getArrow();
            canvas.drawText(mp10Value, getWidth() - 10, 40, airQualityPaint);

            airQualityPaint.setColor(pM2_5.getColor());
            String pm2_5Value = "PM 2.5: " + pM2_5.value + pM2_5.getArrow();
            canvas.drawText(pm2_5Value, getWidth() - 10, 70, airQualityPaint);

            airQualityPaint.setColor(Color.WHITE);
            canvas.drawText(pM10.time, getWidth() - 20, 100, airQualityPaint);
            canvas.drawText(pM10.probe, getWidth() - 20, 130, airQualityPaint);
        }
    }

    private void drawWeather(Canvas canvas) {
        if (weatherConditions == null)
            return;

        DecimalFormat decimalFormat = new DecimalFormat("##.#");
        canvas.drawText(decimalFormat.format(weatherConditions.temperature) + "°", getWidth() - 20, 160, airQualityPaint);
    }

    public void setAccountBalance(AccountBalance accountBalance) {
        this.accountBalance = accountBalance;
        invalidate();
    }

    public void setInternetBalance(InternetBalance internetBalance) {
        this.internetBalance = internetBalance;
        invalidate();
    }
}