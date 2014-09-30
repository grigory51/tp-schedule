package ru.mail.tp.schedule.schedule.entities;

import java.io.Serializable;

/**
 * author: grigory51
 * date: 28/09/14
 */
public class Subgroup implements Serializable {
    private final int id;
    private final String title;

    public Subgroup(String id, String title) {
        this(Integer.parseInt(id), title);
    }

    private Subgroup(int id, String title) {
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
