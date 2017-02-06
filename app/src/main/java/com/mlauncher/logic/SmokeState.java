package com.mlauncher.logic;

/**
 * Created by daba on 2017-02-06.
 */

public enum SmokeState {
    GOOD,
    NOTBAD,
    BAD,
    VERYBAD,
    EXTREMELYBAD,
    HAZARDOUS;

    public static SmokeState fromString(String value) {
        return Enum.valueOf(SmokeState.class, value.toUpperCase());
    }
}
