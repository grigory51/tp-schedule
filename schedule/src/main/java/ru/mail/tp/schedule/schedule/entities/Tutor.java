package ru.mail.tp.schedule.schedule.entities;

/**
 * author: grigory51
 * date: 06/10/14
 */
public class Tutor {
    private int id;
    private String name;

    public Tutor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
