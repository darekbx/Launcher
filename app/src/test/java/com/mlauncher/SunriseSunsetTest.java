package com.mlauncher;

import com.mlauncher.logic.SunriseSunset;
import com.mlauncher.model.DayItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * Created by daba on 2016-09-30.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SunriseSunsetTest {

    @Test
    public void get_current_day_info() {

        Calendar calendar = generateCalendar();

        DayItem dayItem = SunriseSunset.getCurrentDayInfo(calendar);

        assertEquals(dayItem.sunrise, 336);
        assertEquals(dayItem.sunset, 1034);
        assertEquals(dayItem.calendar, calendar);
    }

    @Test
    public void extract_time() {

        DayItem dayItem = new DayItem(generateCalendar(), 336, 1034);

        int[] time = dayItem.extractTime(dayItem.sunrise);
        assertEquals(time[0], 6);
        assertEquals(time[1], 36);
    }

    @Test
    public void format_time() {

        DayItem dayItem = new DayItem(generateCalendar(), 336, 1034);
        int[] time = dayItem.extractTime(dayItem.sunrise);

        assertEquals(dayItem.formatTime(time), "06:36");
    }

    @Test
    public void get_sunrise() {
        DayItem dayItem = new DayItem(generateCalendar(), 336, 1034);

        assertEquals(dayItem.getSunrise(), "Sunrise: 06:36");
    }

    @Test
    public void get_sunset() {
        DayItem dayItem = new DayItem(generateCalendar(), 336, 1034);

        assertEquals(dayItem.getSunset(), "Sunset: 18:14");
    }

    public Calendar generateCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 8, 30);
        return calendar;
    }
}
