package ru.mail.tp.schedule.schedule.filter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * author: grigory51
 * date: 22.07.14
 */
public class FilterArrayAdapter extends ArrayAdapter<FilterSpinner> {
    private FilterArrayAdapter(Context context) {
        super(context, android.R.layout.simple_spinner_item);
        this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public FilterArrayAdapter(Context context, FilterSpinnerList list) {
        this(context);
        this.addAll(list);
    }

    void addAll(ArrayList<FilterSpinner> list) {
        for (FilterSpinner item : list) {
            this.add(item);
        }
    }
}
