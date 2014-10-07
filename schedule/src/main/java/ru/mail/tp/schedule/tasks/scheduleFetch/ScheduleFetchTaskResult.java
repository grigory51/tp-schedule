package ru.mail.tp.schedule.tasks.scheduleFetch;

import ru.mail.tp.schedule.tasks.TaskResult;

/**
 * author: grigory51
 * date: 26/09/14
 */
public class ScheduleFetchTaskResult extends TaskResult {
    public static final int STATUS_OK = 0;
    public static final int STATUS_NETWORK_ERROR = 1;
    public static final int STATUS_DATA_PROCESS_ERROR = 2;

    private final int status;

    public ScheduleFetchTaskResult(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
