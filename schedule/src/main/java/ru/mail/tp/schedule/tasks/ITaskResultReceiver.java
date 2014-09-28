package ru.mail.tp.schedule.tasks;

/**
 * author: grigory51
 * date: 26/09/14
 */
public interface ITaskResultReceiver {
    public void onPostExecute(TaskResult result);
}