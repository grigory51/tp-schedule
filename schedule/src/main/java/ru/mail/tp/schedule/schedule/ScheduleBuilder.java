package ru.mail.tp.schedule.schedule;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import ru.mail.tp.schedule.schedule.entities.ScheduleItem;

/**
 * author: grigory51
 * date: 27/09/14
 */
public class ScheduleBuilder implements Serializable, Parcelable {
    public static final Parcelable.Creator<ScheduleBuilder> CREATOR = new Parcelable.Creator<ScheduleBuilder>() {
        public ScheduleBuilder createFromParcel(Parcel in) {
            return new ScheduleBuilder(in);
        }

        public ScheduleBuilder[] newArray(int size) {
            return new ScheduleBuilder[size];
        }
    };

    private final ArrayList<ScheduleItem> list;

    public ScheduleBuilder(ArrayList<ScheduleItem> list) {
        this.list = list;
    }

    public ScheduleBuilder(Parcel in) {
        this.list = new ArrayList<ScheduleItem>(Arrays.asList((ScheduleItem[]) in.readArray(ScheduleItem.class.getClassLoader())));
    }

    public ArrayList<ScheduleItem> getScheduleItems() {
        return this.getScheduleItems(new ScheduleFilter());
    }

    public ArrayList<ScheduleItem> getScheduleItems(ScheduleFilter filter) {
        if (filter != null) {
            ArrayList<ScheduleItem> result = new ArrayList<ScheduleItem>();

            for (ScheduleItem item : this.list) {
                if (item.isFilterMatch(filter)) {
                    result.add(item);
                }
            }
            return result;
        } else {
            return this.list;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeArray(this.list.toArray());
    }
}
