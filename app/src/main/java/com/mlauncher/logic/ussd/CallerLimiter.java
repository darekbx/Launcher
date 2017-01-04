package com.mlauncher.logic.ussd;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by daba on 2016-12-13.
 */

public class CallerLimiter {

    private static final String DATE_KEY = "limiter_date";

    private Context context;

    public CallerLimiter(Context context) {
        this.context = context;
    }

    public boolean canCall() {
        long date = getDate();
        if (date == 0) {
            storeCallDate(Calendar.getInstance().getTimeInMillis());
            return true;
        }
        boolean value = isNextCallTime();
        if (value) {
            storeCallDate(Calendar.getInstance().getTimeInMillis());
        }
        return value;
    }

    protected long getNextCallTime(long lastCallTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(lastCallTime));
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.add(Calendar.MILLISECOND, (int)getCallInterval());
        return calendar.getTimeInMillis();
    }

    protected long getCallInterval() {
        return TimeUnit.DAYS.toMillis(1);
    }

    protected boolean isNextCallTime() {
        long date = getDate();
        long nextDate = getNextCallTime(date);
        return Calendar.getInstance().getTimeInMillis() > nextDate;
    }

    protected void storeCallDate(long date) {
        getPreferences()
                .edit()
                .remove(DATE_KEY)
                .putLong(DATE_KEY, date)
                .apply();
    }

    protected long getDate() {
        SharedPreferences preferences = getPreferences();
        if (!preferences.contains(DATE_KEY)) {
            return 0;
        }
        return getPreferences().getLong(DATE_KEY, 0);
    }

    private SharedPreferences getPreferences() {
        return context.getSharedPreferences(CallerLimiter.class.getName(), Context.MODE_PRIVATE);
    }
}
