package com.mlauncher.logic.sojp;

import com.mlauncher.model.SmokeItem;

/**
 * Created by daba on 2016-09-14.
 */
public class Entry {
    public String date;
    public String station;
    public String parameter;
    public String unit;
    public double value;

    public SmokeItem toSmokeItem() {
        SmokeItem smokeItem = new SmokeItem();
        smokeItem.probe = station;
        smokeItem.time = smokeItemDate(date);
        smokeItem.value = value + " " + unit;
        smokeItem.state = determineState(isPM10() ? 50 : 25);
        smokeItem.trend = SmokeItem.Trend.STABLE;
        return smokeItem;
    }

    public static String smokeItemDate(String date) {
        return new StringBuilder(date.replace("/", "-")).insert(10, ' ').toString();
    }

    public String determineState(final int step) {
        if (value <= step) {
            return "good";
        } else if (value > step && value <= step * 2) {
            return "notbad";
        } else if (value > step * 2 && value <= step * 3) {
            return "bad";
        } else if (value > step * 3 && value <= step * 4) {
            return "verybad";
        } else if (value > step * 4) {
            return "extremelybad";
        }
        return null;
    }

    public boolean isPM10() {
        return parameter.equals("PM10");
    }
}