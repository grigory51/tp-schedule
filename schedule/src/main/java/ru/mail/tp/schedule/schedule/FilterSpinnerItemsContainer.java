package ru.mail.tp.schedule.schedule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class FilterSpinnerItemsContainer implements Serializable {
    private final ArrayList<FilterSpinnerItem> subgroupItems = new ArrayList<FilterSpinnerItem>();
    private final ArrayList<FilterSpinnerItem> disciplineItems = new ArrayList<FilterSpinnerItem>();
    private final ArrayList<FilterSpinnerItem> typeItems = new ArrayList<FilterSpinnerItem>();

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
