package ru.mail.tp.schedule.utils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * author: grigory51
 * date: 30/09/14
 */
public abstract class MoscowCalendar extends Calendar {
    public static synchronized Calendar getInstance() {
        return MoscowCalendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
    }

    public static synchronized Calendar getTodayInstance() {
        Calendar todayCalendar = MoscowCalendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
        MoscowCalendar.resetTime(todayCalendar);

        return todayCalendar;
    }

    public static void resetTime(Calendar calendar) {
        calendar.set(Calendar.AM_PM, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
