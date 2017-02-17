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
            return R.drawable.ring_good_notbad;
        } else if (getMaxState(stateA, stateB, SmokeState.NOTBAD)) {
            return R.drawable.ring_notbad_bad;
        } else if (getMaxState(stateA, stateB, SmokeState.BAD)) {
            return R.drawable.ring_bad_verybad;
        } else if (getMaxState(stateA, stateB, SmokeState.VERYBAD)) {
            return R.drawable.ring_verybad_extremalybad;
        } else if (getMaxState(stateA, stateB, SmokeState.EXTREMELYBAD)) {
            return R.drawable.ring_extremalybad_extremalybad;
        } else if (getMaxState(stateA, stateB, SmokeState.HAZARDOUS)) {
            return R.drawable.ring_extremalybad_hazadrous;
        } else {
            return R.drawable.ring_hazadrous_hazadrous;
        }
    }

    private static boolean getMaxState(
            SmokeState stateA, SmokeState stateB,
            SmokeState compare) {
        return stateA == compare || stateB == compare;
    }
}