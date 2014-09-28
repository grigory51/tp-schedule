package ru.mail.tp.schedule.schedule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


/**
 * author: grigory51
 * date: 04.07.14
 */
public class ScheduleJSONProcessor {
    private JSONObject auditoriums, types, subgroups, disciplines, places;
    private JSONObject timetable;

    public ScheduleJSONProcessor(JSONObject json) throws JSONException {
        this.auditoriums = json.getJSONObject("auditoriums");
        this.places = json.getJSONObject("places");
        this.types = json.getJSONObject("types");
        this.subgroups = json.getJSONObject("groups");
        this.disciplines = json.getJSONObject("disciplines");
        this.timetable = json.getJSONObject("timetable");
    }

    public ArrayList<ScheduleItem> getScheduleItems() throws JSONException {
        ArrayList<ScheduleItem> result = new ArrayList<ScheduleItem>();

        Iterator<?> keys = this.timetable.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            JSONObject item = this.timetable.getJSONObject(key);

            long timeStart = item.getLong("time_start");
            long timeEnd = item.getLong("time_end");
            String placeTitle = "";
            String title = "";
            String type = null;
            int lessonTypeId = 0;
            int disciplineId = 0;
            String eventType = item.getString("event");
            if (eventType.equals("event")) {
                title = item.getString("title");

                String placeId = item.getString("place");
                String auditoriumId = item.getString("auditorium");
                if (!placeId.equals("0")) {
                    placeTitle = this.places.getString(placeId);
                } else if (!auditoriumId.equals("0")) {
                    placeTitle = this.auditoriums.getString(auditoriumId);
                }
            } else if (eventType.equals("lesson")) {
                disciplineId = item.getInt("discipline");
                title = this.disciplines.getJSONObject(item.getString("discipline")).getString("long_name");
                lessonTypeId = item.getInt("type");
                type = this.types.getString(item.getString("type"));
                String auditoriumId = item.getString("auditorium");
                if (!auditoriumId.equals("0")) {
                    placeTitle = this.auditoriums.getString(auditoriumId);
                }
            }
            result.add(new ScheduleItem(timeStart, timeEnd, title, placeTitle, new String[]{}, eventType, type, disciplineId, new int[]{}, lessonTypeId));
        }

        Collections.sort(result, new Comparator<ScheduleItem>() {
            public int compare(ScheduleItem a, ScheduleItem b) {
                if (a.getTimeStart() == b.getTimeStart()) {
                    return 0;
                } else {
                    return a.getTimeStart() - b.getTimeStart() > 0 ? 1 : -1;
                }
            }
        });

        return result;
    }

    public FilterSpinnerItemsContainer getScheduleFiltersData() throws JSONException {
        return new FilterSpinnerItemsContainer(this.disciplines, this.types, this.subgroups);
    }
}