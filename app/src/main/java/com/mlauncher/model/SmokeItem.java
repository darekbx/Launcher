package com.mlauncher.model;

import android.graphics.Color;

/**
 * Created by INFOR PL on 2015-08-19.
 */
public class SmokeItem {

    public enum Type {
        PM10,
        PM2_5
    }

    public enum Trend {
        UP,
        STABLE,
        DOWN
    }

    public Type type;
    public Trend trend;
    public String value;
    public String state;
    public String time;
    public String probe;

    public int getColor() {
        if (state.equals("good")) {
            return Color.rgb(76, 175, 80);
        } else if (state.equals("notbad")) {
            return Color.rgb(255, 235, 59);
        } else if (state.equals("bad")) {
            return Color.rgb(255, 152, 0);
        } else if (state.equals("verybad")) {
            return Color.rgb(255, 87, 34);
        } else if (state.equals("extremelybad")) {
            return Color.rgb(186, 0, 0);
        }

        return Color.rgb(76, 175, 80);
    }

    public String getArrow() {
        switch (trend) {
            case UP: return "\u2191";
            case DOWN: return "\u2193";
            default: case STABLE: return "-";
        }
    }
}