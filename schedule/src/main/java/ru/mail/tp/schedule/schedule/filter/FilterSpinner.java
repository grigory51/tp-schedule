package ru.mail.tp.schedule.schedule.filter;

import java.io.Serializable;

import ru.mail.tp.schedule.utils.StringHelper;

/**
 * author: grigory51
 * date: 03/10/14
 */
public class FilterSpinner implements Serializable {
    private int id;
    private String title;

    protected FilterSpinner(String id, String title) {
        this(Integer.parseInt(id), title);
    }

    protected FilterSpinner(int id, String title) {
        this.id = id;
        this.title = StringHelper.quotesFormat(title);
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public String toString() {
        return this.getTitle();
    }
}
