package com.mlauncher.logic;

import android.support.annotation.DrawableRes;

import com.mlauncher.R;

/**
 * Created by daba on 2017-02-06.
 */

public class SmokeLiveColor {

    @DrawableRes
    public static int takeImageByState(SmokeState stateA, SmokeState stateB) {
        if (hasTwoStates(stateA, stateB, SmokeState.GOOD, SmokeState.GOOD)) {
            return R.drawable.ring_good_good;
        } else if (hasTwoStates(stateA, stateB, SmokeState.GOOD, SmokeState.NOTBAD)) {
            return R.drawable.ring_good_notbad;
        } else if (hasTwoStates(stateA, stateB, SmokeState.NOTBAD, SmokeState.NOTBAD)) {
            return R.drawable.ring_notbad_notbad;
        } else if (hasTwoStates(stateA, stateB, SmokeState.NOTBAD, SmokeState.BAD)) {
            return R.drawable.ring_notbad_bad;
        } else if (hasTwoStates(stateA, stateB, SmokeState.BAD, SmokeState.BAD)) {
            return R.drawable.ring_bad_bad;
        } else if (hasTwoStates(stateA, stateB, SmokeState.BAD, SmokeState.VERYBAD)) {
            return R.drawable.ring_bad_verybad;
        } else if (hasTwoStates(stateA, stateB, SmokeState.VERYBAD, SmokeState.VERYBAD)) {
            return R.drawable.ring_verybad_verybad;
        } else if (hasTwoStates(stateA, stateB, SmokeState.VERYBAD, SmokeState.EXTREMELYBAD)) {
            return R.drawable.ring_verybad_extremalybad;
        } else if (hasTwoStates(stateA, stateB, SmokeState.EXTREMELYBAD, SmokeState.EXTREMELYBAD)) {
            return R.drawable.ring_extremalybad_extremalybad;
        } else if (hasTwoStates(stateA, stateB, SmokeState.EXTREMELYBAD, SmokeState.HAZARDOUS)) {
            return R.drawable.ring_extremalybad_hazadrous;
        } else {
            return R.drawable.ring_hazadrous_hazadrous;
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