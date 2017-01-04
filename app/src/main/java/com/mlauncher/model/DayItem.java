package com.mlauncher.model;

import com.mlauncher.logic.SunriseSunset;

import java.util.Calendar;

public class DayItem {

	public Calendar calendar;
	public int sunrise;
	public int sunset;

	public DayItem(Calendar calendar, int sunrise, int sunset) {
		
		super();
		
		this.calendar = calendar;
		this.sunrise = sunrise;
		this.sunset = sunset;
	}

    public int[] extractTime(int value) {

        if (SunriseSunset.TIMEZONE.inDaylightTime(calendar.getTime()))
            value += 60;

        int hours = value / 60;
        int minutes = value - hours * 60;

        return new int[]{hours, minutes};
    }

    public String formatTime(int[] time) {
        return String.format("%02d:%02d", time[0], time[1]);
    }

    public String getSunrise() {
        int[] time = extractTime(sunrise);
        String timeString = formatTime(time);
        return String.format("Sunrise: %s", timeString);
    }

    public String getSunset() {
        int[] time = extractTime(sunset);
        String timeString = formatTime(time);
        return String.format("Sunset: %s", timeString);
    }
}