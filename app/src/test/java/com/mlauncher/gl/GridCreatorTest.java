package com.mlauncher.gl;

import android.graphics.Color;

import com.mlauncher.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by daba on 2016-09-30.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class GridCreatorTest {

    @Test
    public void makeGrid() throws Exception {

        int[] position = new GridCreator().makeGrid(2, 2);
    }
}
