package ru.mail.tp.schedule.schedule.entities;

import java.io.Serializable;

/**
 * author: grigory51
 * date: 28/09/14
 */
public class Place implements Serializable {
    private final String title;

    public Place(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
