package ru.mail.tp.schedule.schedule.entities;

import android.os.Parcel;

import ru.mail.tp.schedule.schedule.filter.FilterSpinner;

/**
 * author: grigory51
 * date: 28/09/14
 */
public class Subgroup extends FilterSpinner {

    public Subgroup(String id, String title) {
        super(id, title);
    }

    public Subgroup(int id, String title) {
        super(id, title);
    }
}
