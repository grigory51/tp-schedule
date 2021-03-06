package ru.mail.tp.schedule.schedule.filter;

import java.io.Serializable;

/**
 * author: grigory51
 * date: 06/10/14
 */
class FilterSpinner implements Serializable {
    private final IFilterSpinner item;

    public FilterSpinner(IFilterSpinner item) {
        this.item = item;
    }

    public int getId() {
        return item.getId();
    }

    @Override
    public String toString() {
        return item.getFilterTitle();
    }
}
