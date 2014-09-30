package ru.mail.tp.schedule.schedule.entities;

import java.io.Serializable;

/**
 * author: grigory51
 * date: 28/09/14
 */
public class Discipline implements Serializable {
    private final int id;
    private final String title;
    private final String shortTitle;

    public Discipline(String id, String title, String shortTitle) {
        this(Integer.parseInt(id), title, shortTitle);
    }

    private Discipline(int id, String title, String shortTitle) {
        this.id = id;
        this.title = title;
        this.shortTitle = shortTitle;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getShortTitle() {
        return this.shortTitle;
    }
}
