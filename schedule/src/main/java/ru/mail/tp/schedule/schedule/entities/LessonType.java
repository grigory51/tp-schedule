package ru.mail.tp.schedule.schedule.entities;

import ru.mail.tp.schedule.schedule.filter.FilterSpinner;

/**
 * author: grigory51
 * date: 28/09/14
 */
public class LessonType extends FilterSpinner {
    public LessonType(String id, String title) {
        super(id, title);
    }

    public LessonType(int id, String title) {
        super(id, title);
    }
}
