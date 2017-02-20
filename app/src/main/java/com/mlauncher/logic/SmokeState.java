package com.mlauncher.logic;

/**
 * Created by daba on 2017-02-06.
 */

public enum SmokeState {
    GOOD(0),
    NOTBAD(1),
    BAD(2),
    VERYBAD(3),
    EXTREMELYBAD(4),
    HAZARDOUS(5);

    private int value;

    SmokeState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SmokeState fromString(String value) {
        return Enum.valueOf(SmokeState.class, value.toUpperCase());
    }
}
