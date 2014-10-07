package ru.mail.tp.schedule.schedule.filter;

import java.util.ArrayList;

/**
 * author: grigory51
 * date: 06/10/14
 */
class FilterSpinnerList extends ArrayList<FilterSpinner> {
    public void add(IFilterSpinner item) {
        this.add(new FilterSpinner(item));
    }
}
