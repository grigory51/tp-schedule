package ru.mail.tp.schedule.schedule;

import java.io.Serializable;

/**
 * author: grigory51
 * date: 27/09/14
 */
public class FilterSpinnerItem implements Serializable {
    private int id;
    private String title;

    public FilterSpinnerItem(String id, String title) {
        this(Integer.parseInt(id), title);
    }

    public FilterSpinnerItem(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return this.getTitle();
    }
}
