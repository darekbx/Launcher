package com.mlauncher.dots;

import com.mlauncher.BuildConfig;
import com.mlauncher.dotpad.Dot;
import com.mlauncher.dotpad.DotManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by daba on 2016-09-30.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DotDrawerTest {

    @Test
    public void load_dots() {
        List<Dot> dots = DotManager.dots();

        assertTrue(dots.size() > 0);
    }

    @Test
    public void count_dots() {
        List<Dot> dots = DotManager.dots();
        int dotsCount = DotManager.countDots();

        assertTrue(dots.size() > 0);
        assertTrue(dots.size() != dotsCount);
    }
}
