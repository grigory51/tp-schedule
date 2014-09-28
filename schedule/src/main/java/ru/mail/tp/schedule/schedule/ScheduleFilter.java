package ru.mail.tp.schedule.schedule;

/**
 * author: grigory51
 * date: 27/09/14
 */
public class ScheduleFilter {
    private int subgroupId;
    private int disciplineId;
    private int lessonTypeId;

    public ScheduleFilter(int subgroupId, int disciplineId, int lessonTypeId) {
        this.subgroupId = subgroupId;
        this.disciplineId = disciplineId;
        this.lessonTypeId = lessonTypeId;
    }

    public int getSubgroupId() {
        return this.subgroupId;
    }

    public int getDisciplineId() {
        return this.disciplineId;
    }

    public int getLessonTypeId() {
        return this.lessonTypeId;
    }
}
