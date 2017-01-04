package com.mlauncher.logic;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;
import com.mlauncher.model.DayItem;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by daba on 2016-09-29.
 */
public class SunriseSunset {

    private static final Location LOCATION = new Location(52.232222, 21.008333);
    public static TimeZone TIMEZONE = TimeZone.getTimeZone("Europe/Warsaw");

    public static DayItem getCurrentDayInfo(Calendar current) {

        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(LOCATION, TIMEZONE);

        Calendar sunrise = calculator.getOfficialSunriseCalendarForDate(current);
        Calendar sunset = calculator.getOfficialSunsetCalendarForDate(current);

        int sunriseMinutes = sunrise.get(Calendar.HOUR_OF_DAY) * 60 + sunrise.get(Calendar.MINUTE);
        int sunsetMinutes = sunset.get(Calendar.HOUR_OF_DAY) * 60 + sunset.get(Calendar.MINUTE);

        if (TIMEZONE.inDaylightTime(current.getTime())) {
            sunriseMinutes -= 60;
            sunsetMinutes -= 60;
        }

        return new DayItem(current, sunriseMinutes, sunsetMinutes);
    }
}
