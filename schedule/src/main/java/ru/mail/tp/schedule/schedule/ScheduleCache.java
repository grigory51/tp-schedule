package ru.mail.tp.schedule.schedule;

import java.io.Serializable;
import java.util.Date;

import ru.mail.tp.schedule.schedule.filter.FilterSpinnerItemsContainer;
import ru.mail.tp.schedule.utils.MoscowCalendar;

/**
 * author: grigory51
 * date: 30/09/14
 */
public class ScheduleCache implements Serializable {
    private final FilterSpinnerItemsContainer filterContainer;
    private final Date lastModified;

    public ScheduleCache(FilterSpinnerItemsContainer filterContainer) {

        this.filterContainer = filterContainer;
        this.lastModified = MoscowCalendar.getInstance().getTime();
    }

    public FilterSpinnerItemsContainer getFilterContainer() {
        return filterContainer;
    }

    public Date getLastModified() {
        return lastModified;
    }
}
