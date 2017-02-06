package com.mlauncher.logic;

/**
 * Created by daba on 2017-02-06.
 */

public class SmokeLiveColor {

    public static final float[][] COLOR_STATES = new float[][]{
            {0.75f, 2f, 1f},
            {0.8f, 1.7f, 1f},
            {1.25f, 2.5f, 1f},
            {1.75f, 2f, 1f},
            {1.75f, 1.5f, 1f},
            {2f, 1.25f, 1f},
            {2.25f, 1f, 1f},
            {2.5f, 0.75f, 1f},
            {2f, 0.2f, 1.5f}
    };

    public static float[] takeColorMask(SmokeState stateA, SmokeState stateB) {
        if (hasTwoStates(stateA, stateB, SmokeState.GOOD, SmokeState.GOOD)) {
            return COLOR_STATES[0];
        } else if (hasTwoStates(stateA, stateB, SmokeState.GOOD, SmokeState.NOTBAD)) {
            return COLOR_STATES[1];
        } else if (hasTwoStates(stateA, stateB, SmokeState.NOTBAD, SmokeState.NOTBAD)) {
            return COLOR_STATES[2];
        } else if (hasTwoStates(stateA, stateB, SmokeState.NOTBAD, SmokeState.BAD)) {
            return COLOR_STATES[3];
        } else if (hasTwoStates(stateA, stateB, SmokeState.BAD, SmokeState.BAD)) {
            return COLOR_STATES[4];
        } else if (hasTwoStates(stateA, stateB, SmokeState.BAD, SmokeState.VERYBAD)) {
            return COLOR_STATES[5];
        } else if (hasTwoStates(stateA, stateB, SmokeState.VERYBAD, SmokeState.VERYBAD)) {
            return COLOR_STATES[6];
        } else if (hasTwoStates(stateA, stateB, SmokeState.VERYBAD, SmokeState.HAZARDOUS)) {
            return COLOR_STATES[7];
        } else {
            return COLOR_STATES[8];
        }
    }

    private static boolean hasTwoStates(
            SmokeState stateA, SmokeState stateB,
            SmokeState compareA, SmokeState compareB) {
        return
                (stateA == compareA && stateB == compareB) ||
                        (stateA == compareB && stateB == compareA);
    }
}