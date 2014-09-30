package ru.mail.tp.schedule.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;


public class MoscowSimpleDateFormat extends SimpleDateFormat {
    private static final TimeZone moscowTimeZone = TimeZone.getTimeZone("Europe/Moscow");

    public MoscowSimpleDateFormat(String pattern) {
        super(pattern, Locale.US);
        this.setTimeZone(MoscowSimpleDateFormat.moscowTimeZone);
    }
}
