package ru.mail.tp.schedule.schedule.entities;

import ru.mail.tp.schedule.schedule.filter.IFilterSpinner;
import ru.mail.tp.schedule.utils.StringHelper;

/**
 * author: grigory51
 * date: 28/09/14
 */
public class Discipline implements IFilterSpinner {
    private final String shortTitle;
    private int id;
    private String title;

    public Discipline(int id, String title) {
        this(id, title, "");
    }

    public Discipline(String id, String title, String shortTitle) {
        this(Integer.parseInt(id), title, shortTitle);
    }

    private Discipline(int id, String title, String shortTitle) {
        this.id = id;
        this.title = StringHelper.quotesFormat(title);
        this.shortTitle = StringHelper.quotesFormat(shortTitle);
    }

    public String getShortTitle() {
        return this.shortTitle;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getFilterTitle() {
        return !this.getShortTitle().equals("") ? this.getShortTitle() : this.getTitle();
    }

    public String getTitle() {
        return this.title;
    }
}
