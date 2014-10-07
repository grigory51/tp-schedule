package ru.mail.tp.schedule.fragments;

import ru.mail.tp.schedule.schedule.db.entities.ScheduleItem;

/**
 * author: grigory51
 * date: 03/10/14
 */
public interface OnScheduleItemClick {
    public void onScheduleItemClick(ScheduleItem scheduleItem);
}
