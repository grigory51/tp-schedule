package ru.mail.tp.schedule.schedule;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * author: grigory51
 * date: 22.07.14
 */
public class FilterArrayAdapter extends ArrayAdapter<FilterSpinnerItem> {
    public FilterArrayAdapter(Context context) {
        super(context, android.R.layout.simple_spinner_item);
        this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void addAll(ArrayList<FilterSpinnerItem> list) {
        for (FilterSpinnerItem item : list) {
            this.add(item);
        }
    }
}
