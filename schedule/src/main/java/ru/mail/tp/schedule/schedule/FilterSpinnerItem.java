package ru.mail.tp.schedule.schedule;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * author: grigory51
 * date: 27/09/14
 */
public class FilterSpinnerItem implements Serializable, Parcelable {
    public static final Parcelable.Creator<FilterSpinnerItem> CREATOR = new Parcelable.Creator<FilterSpinnerItem>() {
        public FilterSpinnerItem createFromParcel(Parcel in) {
            return new FilterSpinnerItem(in);
        }

        public FilterSpinnerItem[] newArray(int size) {
            return new FilterSpinnerItem[size];
        }
    };

    private final int id;
    private final String title;

    public FilterSpinnerItem(String id, String title) {
        this(Integer.parseInt(id), title);
    }

    public FilterSpinnerItem(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public FilterSpinnerItem(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return this.getTitle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.title);
    }
}
