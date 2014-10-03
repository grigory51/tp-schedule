package ru.mail.tp.schedule.schedule;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: grigory51
 * date: 27/09/14
 */
public class ScheduleFilter implements Parcelable {
    public static final Parcelable.Creator<ScheduleFilter> CREATOR = new Parcelable.Creator<ScheduleFilter>() {
        public ScheduleFilter createFromParcel(Parcel in) {
            return new ScheduleFilter(in);
        }

        public ScheduleFilter[] newArray(int size) {
            return new ScheduleFilter[size];
        }
    };
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

    public ScheduleFilter(Parcel in) {
        this.subgroupId = in.readInt();
        this.disciplineId = in.readInt();
        this.lessonTypeId = in.readInt();
        this.showPassed = (in.readInt() == 0);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.subgroupId);
        parcel.writeInt(this.disciplineId);
        parcel.writeInt(this.lessonTypeId);
        parcel.writeInt(this.showPassed ? 1 : 0);
    }
}
