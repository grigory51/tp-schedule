package ru.mail.tp.schedule.schedule;

import java.io.Serializable;
import java.util.Date;

import ru.mail.tp.schedule.utils.MoscowCalendar;

/**
 * author: grigory51
 * date: 30/09/14
 */
public class ScheduleCache implements Serializable {
    private final ScheduleBuilder scheduleBuilder;
    private final FilterSpinnerItemsContainer filterContainer;
    private final Date lastModified;

    public ScheduleCache(ScheduleBuilder scheduleBuilder, FilterSpinnerItemsContainer filterContainer) {
        this.scheduleBuilder = scheduleBuilder;
        this.filterContainer = filterContainer;
        this.lastModified = MoscowCalendar.getInstance().getTime();
    }

    public ScheduleBuilder getScheduleBuilder() {
        return scheduleBuilder;
    }

    public FilterSpinnerItemsContainer getFilterContainer() {
        return filterContainer;
    }

    public Date getLastModified() {
        return lastModified;
    }
}
