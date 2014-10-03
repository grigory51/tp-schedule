package ru.mail.tp.schedule.schedule.entities;

import ru.mail.tp.schedule.schedule.filter.FilterSpinner;
import ru.mail.tp.schedule.utils.StringHelper;

/**
 * author: grigory51
 * date: 28/09/14
 */
public class Discipline extends FilterSpinner {
    private final String shortTitle;

    public Discipline(int id, String title) {
        this(id, title, "");
    }

    public Discipline(String id, String title, String shortTitle) {
        this(Integer.parseInt(id), title, shortTitle);
    }

    private Discipline(int id, String title, String shortTitle) {
        super(id, title);
        this.shortTitle = StringHelper.quotesFormat(shortTitle);
    }

    public String getShortTitle() {
        return this.shortTitle;
    }

    @Override
    public String toString() {
        return !this.getShortTitle().equals("") ? this.getShortTitle() : this.getTitle();
    }
}
