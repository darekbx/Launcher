package com.mlauncher;

import com.mlauncher.logic.TimeManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by daba on 2016-09-30.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TimeManagerTest {

    @Test
    public void diff() {
        Calendar start = Calendar.getInstance();
        start.set(2014, 10, 10);
        Calendar end = Calendar.getInstance();
        end.set(2017, 12, 23);


        long days = TimeUnit.MILLISECONDS.toDays(end.getTimeInMillis() - start.getTimeInMillis());
        assertEquals(1, days);
    }

    @Test
    public void collect_time() throws Exception {

        TimeManager timeManager = new TimeManager(RuntimeEnvironment.application);

        timeManager.notifyScreenOn();

        Thread.sleep(2000);

        timeManager.notifyScreenOff();

        assertEquals(timeManager.getTime() / 1000, 2);
    }

    @Test
    public void time_format() throws Exception {

        TimeManager timeManager = new TimeManager(RuntimeEnvironment.application);

        timeManager.appendTime(
                TimeUnit.HOURS.toMillis(3)
                + TimeUnit.MINUTES.toMillis(20)
                + TimeUnit.SECONDS.toMillis(30));

        assertEquals(timeManager.getTodayTime(), "03:20:30");
    }

    @Test
    public void reset() throws Exception {

        TimeManager timeManager = new TimeManager(RuntimeEnvironment.application);

        timeManager.appendTime(TimeUnit.HOURS.toMillis(3));
        assertEquals(timeManager.getTodayTime(), "03:00:00");

        timeManager.reset();
        assertEquals(timeManager.getTime(), 0);
        assertEquals(timeManager.getTodayTime(), "00:00:00");;
    }
}
