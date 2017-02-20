package com.mlauncher.logic;

import android.support.annotation.DrawableRes;

import com.mlauncher.R;

/**
 * Created by daba on 2017-02-06.
 */

public class SmokeLiveColor {

    @DrawableRes
    public static int takeImageByState(SmokeState stateA, SmokeState stateB) {
        if (getMaxState(stateA, stateB, SmokeState.GOOD)) {
            return R.drawable.a_notbad;
        } else if (getMaxState(stateA, stateB, SmokeState.NOTBAD)) {
            return R.drawable.a_bad;
        } else if (getMaxState(stateA, stateB, SmokeState.BAD)) {
            return R.drawable.a_verybad;
        } else if (getMaxState(stateA, stateB, SmokeState.VERYBAD)) {
            return R.drawable.a_extremalybad;
        } else if (getMaxState(stateA, stateB, SmokeState.EXTREMELYBAD)) {
            return R.drawable.a_hazardous;
        } else if (getMaxState(stateA, stateB, SmokeState.HAZARDOUS)) {
            return R.drawable.a_ex_hazardous;
        } else {
            return R.drawable.a_dead_hazadrous;
        }
    }

    private static boolean getMaxState(
            SmokeState stateA, SmokeState stateB,
            SmokeState compare) {
        if (stateA == compare || stateB == compare) {
            return stateB.getValue() == compare.getValue();
        }
        return false;
    }
}