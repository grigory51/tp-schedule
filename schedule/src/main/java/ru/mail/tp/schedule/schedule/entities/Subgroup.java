package ru.mail.tp.schedule.schedule.entities;

import java.io.Serializable;

/**
 * author: grigory51
 * date: 28/09/14
 */
public class Subgroup implements Serializable {
    private int id;
    private String title;

    public Subgroup() {
        this(0, null);
    }

    public Subgroup(String id, String title) {
        this(Integer.parseInt(id), title);
    }

    public Subgroup(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }
}
