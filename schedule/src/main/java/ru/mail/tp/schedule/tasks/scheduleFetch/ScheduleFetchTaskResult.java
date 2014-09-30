package ru.mail.tp.schedule.tasks.scheduleFetch;

import ru.mail.tp.schedule.schedule.FilterSpinnerItemsContainer;
import ru.mail.tp.schedule.schedule.ScheduleBuilder;
import ru.mail.tp.schedule.tasks.TaskResult;

/**
 * author: grigory51
 * date: 26/09/14
 */
public class ScheduleFetchTaskResult extends TaskResult {

    public static final int STATUS_OK = 0;
    public static final int STATUS_NETWORK_ERROR = 1;
    public static final int STATUS_DATA_PROCESS_ERROR = 2;

    private final ScheduleBuilder scheduleItems;
    private final FilterSpinnerItemsContainer filterSpinnerItemsContainer;
    private final int status;

    public ScheduleFetchTaskResult(int status) {
        this(status, null, null);
    }

    public ScheduleFetchTaskResult(int status, ScheduleBuilder scheduleBuilder, FilterSpinnerItemsContainer filterSpinnerItemsContainer) {
        this.status = status;
        this.scheduleItems = scheduleBuilder;
        this.filterSpinnerItemsContainer = filterSpinnerItemsContainer;
    }

    public int getStatus() {
        return this.status;
    }

    public ScheduleBuilder getScheduleBuilder() {
        return this.scheduleItems;
    }

    public FilterSpinnerItemsContainer getFilterSpinnerItemsContainer() {
        return this.filterSpinnerItemsContainer;
    }
}
