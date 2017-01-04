package com.mlauncher.logic;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by INFOR PL on 2015-11-03.
 */
public class TimeManager {

    private static final String TIME_KEY = "TIME_MANAGER_TIME_KEY";
    private static final String DAY_KEY = "TIME_MANAGER_DAY_KEY";

    private Context context;
    private long mstartTime;

    public TimeManager(Context context) {
        this.context = context;
        //reset();
    }

    public void reset() {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putLong(TIME_KEY, 0);
        editor.putInt(DAY_KEY, 0);
        editor.apply();
    }

    public void notifyScreenOn() {
        mstartTime = Calendar.getInstance().getTimeInMillis();
    }

    public void notifyScreenOff() {
        if (mstartTime > 0) {
            long diff = Calendar.getInstance().getTimeInMillis() - mstartTime;
            if (diff > 1000) {
                appendTime(diff);
            }
        }
    }

    public String getTodayTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(new Date(getTime() - 1000 * 60 * 60));
    }

    public void appendTime(long time) {
        SharedPreferences preferences = getPreferences();
        final int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

        long currentTime = time;
        if (preferences.getInt(DAY_KEY, 0) == currentDay) {
            currentTime += preferences.getLong(TIME_KEY, 0);
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(TIME_KEY, currentTime);
        editor.putInt(DAY_KEY, currentDay);
        editor.apply();
    }

    public long getTime() {
        SharedPreferences preferences = getPreferences();
        return preferences.getLong(TIME_KEY, 0);
    }

    public SharedPreferences getPreferences() {
        return context.getSharedPreferences(TimeManager.class.getName(), Context.MODE_PRIVATE);
    }
}