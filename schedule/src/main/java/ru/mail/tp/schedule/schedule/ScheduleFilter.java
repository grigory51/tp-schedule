package ru.mail.tp.schedule.schedule;

/**
 * author: grigory51
 * date: 27/09/14
 */
public class ScheduleFilter {
    private final int subgroupId;
    private final int disciplineId;
    private final int lessonTypeId;
    private final boolean showPassed;

    public ScheduleFilter() {
        this(0, 0, 0, false);
    }

    public ScheduleFilter(int subgroupId, int disciplineId, int lessonTypeId, boolean showPassed) {
        this.subgroupId = subgroupId;
        this.disciplineId = disciplineId;
        this.lessonTypeId = lessonTypeId;
        this.showPassed = showPassed;
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

    public boolean isShowPassed() {
        return this.showPassed;
    }
}
