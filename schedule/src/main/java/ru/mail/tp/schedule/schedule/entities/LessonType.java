package ru.mail.tp.schedule.schedule.entities;

import ru.mail.tp.schedule.schedule.filter.IFilterSpinner;

/**
 * author: grigory51
 * date: 28/09/14
 */
public class LessonType implements IFilterSpinner {
    private int id;
    private String title;

    public LessonType(String id, String title) {
        this(Integer.parseInt(id), title);
    }

    public LessonType(int id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getFilterTitle() {
        return this.getTitle();
    }


    public String getTitle() {
        return this.title;
    }
}
