package ru.mail.tp.schedule.schedule;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class FilterSpinnerItemsContainer implements Serializable, Parcelable {
    public static final Parcelable.Creator<FilterSpinnerItemsContainer> CREATOR = new Parcelable.Creator<FilterSpinnerItemsContainer>() {
        public FilterSpinnerItemsContainer createFromParcel(Parcel in) {
            return new FilterSpinnerItemsContainer(in);
        }

        public FilterSpinnerItemsContainer[] newArray(int size) {
            return new FilterSpinnerItemsContainer[size];
        }
    };

    private ArrayList subgroupItems = new ArrayList<FilterSpinnerItem>();
    private ArrayList disciplineItems = new ArrayList<FilterSpinnerItem>();
    private ArrayList typeItems = new ArrayList<FilterSpinnerItem>();

    private int subgroupPosition, disciplinePosition, typePosition;
    private boolean showPassed = false;

    public FilterSpinnerItemsContainer(Parcel in) {
        ClassLoader filterSpinnerItemClassLoader = FilterSpinnerItem.class.getClassLoader();

        this.subgroupItems = in.readArrayList(filterSpinnerItemClassLoader);
        this.disciplineItems = in.readArrayList(filterSpinnerItemClassLoader);
        this.typeItems = in.readArrayList(filterSpinnerItemClassLoader);

        this.subgroupPosition = in.readInt();
        this.disciplinePosition = in.readInt();
        this.typePosition = in.readInt();
        this.showPassed = in.readInt() != 0;
    }

    public FilterSpinnerItemsContainer(JSONObject disciplines, JSONObject types, JSONObject subgroups) throws JSONException {
        String key;
        Comparator<FilterSpinnerItem> comparator = new Comparator<FilterSpinnerItem>() {
            public int compare(FilterSpinnerItem a, FilterSpinnerItem b) {
                if (a.getId() == 0) {
                    return -1;
                }
                if (b.getId() == 0) {
                    return 1;
                }
                return a.getTitle().compareTo(b.getTitle());
            }
        };

        this.subgroupItems.add(new FilterSpinnerItem(0, "Все"));
        Iterator<?> subgroupIds = subgroups.keys();
        while (subgroupIds.hasNext()) {
            key = (String) subgroupIds.next();
            this.subgroupItems.add(new FilterSpinnerItem(key, subgroups.getString(key)));
        }

        this.disciplineItems.add(new FilterSpinnerItem(0, "Все"));
        Iterator<?> disciplineIds = disciplines.keys();
        while (disciplineIds.hasNext()) {
            key = (String) disciplineIds.next();
            JSONObject discipline = disciplines.getJSONObject(key);
            this.disciplineItems.add(new FilterSpinnerItem(key, discipline.getString("short_name")));
        }

        this.typeItems.add(new FilterSpinnerItem(0, "Все"));
        Iterator<?> typeIds = types.keys();
        while (typeIds.hasNext()) {
            key = (String) typeIds.next();
            this.typeItems.add(new FilterSpinnerItem(key, types.getString(key)));
        }

        Collections.sort(this.subgroupItems, comparator);
        Collections.sort(this.disciplineItems, comparator);
        Collections.sort(this.typeItems, comparator);

        this.subgroupPosition = this.disciplinePosition = this.typePosition = 0;
    }

    public ArrayList getSubgroupItems() {
        return this.subgroupItems;
    }

    public ArrayList getDisciplineItems() {
        return this.disciplineItems;
    }

    public ArrayList getTypeItems() {
        return this.typeItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(this.subgroupItems);
        parcel.writeTypedList(this.disciplineItems);
        parcel.writeTypedList(this.typeItems);

        parcel.writeInt(this.subgroupPosition);
        parcel.writeInt(this.disciplinePosition);
        parcel.writeInt(this.typePosition);
        parcel.writeInt(this.showPassed ? 1 : 0);
    }

    public int getSubgroupPosition() {
        return subgroupPosition;
    }

    public void setSubgroupPosition(int subgroupPosition) {
        this.subgroupPosition = subgroupPosition;
    }

    public int getDisciplinePosition() {
        return disciplinePosition;
    }

    public void setDisciplinePosition(int disciplinePosition) {
        this.disciplinePosition = disciplinePosition;
    }

    public boolean isShowPassed() {
        return showPassed;
    }

    public int getTypePosition() {
        return typePosition;
    }

    public void setTypePosition(int typePosition) {
        this.typePosition = typePosition;
    }
}
