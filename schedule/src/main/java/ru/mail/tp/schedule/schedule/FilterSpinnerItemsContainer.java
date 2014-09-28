package ru.mail.tp.schedule.schedule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class FilterSpinnerItemsContainer implements Serializable {
    ArrayList<FilterSpinnerItem> subgroupItems = new ArrayList<FilterSpinnerItem>();
    ArrayList<FilterSpinnerItem> disciplineItems = new ArrayList<FilterSpinnerItem>();
    ArrayList<FilterSpinnerItem> typeItems = new ArrayList<FilterSpinnerItem>();

    FilterSpinnerItemsContainer(JSONObject disciplines, JSONObject types, JSONObject subgroups) throws JSONException {
        String key;

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
    }

    public ArrayList<FilterSpinnerItem> getSubgroupItems() {
        return this.subgroupItems;
    }

    public ArrayList<FilterSpinnerItem> getDisciplineItems() {
        return this.disciplineItems;
    }

    public ArrayList<FilterSpinnerItem> getTypeItems() {
        return this.typeItems;
    }
}
