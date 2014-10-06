package ru.mail.tp.schedule.utils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * author: grigory51
 * date: 30/09/14
 */
public abstract class MoscowCalendar extends Calendar {
    private static final String[] weekDays = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
    private static final String[] month = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};

    public static String getWeekDayTitle(int i) {
        if (0 <= i && i < weekDays.length) {
            return weekDays[i];
        } else {
            return "";
        }
    }

    public static String getMonthTitle(int i) {
        if (0 <= i && i < month.length) {
            return month[i];
        } else {
            return "";
        }
    }

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
