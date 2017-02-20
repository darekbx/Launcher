package com.mlauncher.logic;

import com.mlauncher.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

/**
 * Created by daba on 2017-02-20.
 */
@RunWith(RobolectricTestRunner.class)
public class SmokeLiveColorTest {

    @Test
    public void takeImageByState() throws Exception {
        assertEquals(R.drawable.a_notbad, SmokeLiveColor.takeImageByState(SmokeState.GOOD, SmokeState.GOOD));
        assertEquals(R.drawable.a_bad, SmokeLiveColor.takeImageByState(SmokeState.GOOD, SmokeState.NOTBAD));
        assertEquals(R.drawable.a_verybad, SmokeLiveColor.takeImageByState(SmokeState.VERYBAD, SmokeState.BAD));
        assertEquals(R.drawable.a_extremalybad, SmokeLiveColor.takeImageByState(SmokeState.HAZARDOUS, SmokeState.VERYBAD));
        assertEquals(R.drawable.a_hazardous, SmokeLiveColor.takeImageByState(SmokeState.NOTBAD, SmokeState.EXTREMELYBAD));
        assertEquals(R.drawable.a_ex_hazardous, SmokeLiveColor.takeImageByState(SmokeState.VERYBAD, SmokeState.HAZARDOUS));
        assertEquals(R.drawable.a_notbad, SmokeLiveColor.takeImageByState(SmokeState.BAD, SmokeState.GOOD));
    }
}