package ru.mail.tp.schedule.schedule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import ru.mail.tp.schedule.schedule.entities.Discipline;
import ru.mail.tp.schedule.schedule.entities.LessonType;
import ru.mail.tp.schedule.schedule.entities.Place;
import ru.mail.tp.schedule.schedule.entities.ScheduleItem;
import ru.mail.tp.schedule.schedule.entities.Subgroup;
import ru.mail.tp.schedule.schedule.filter.FilterSpinnerItemsContainer;


/**
 * author: grigory51
 * date: 04.07.14
 */
public class ScheduleJSONProcessor {
    HashMap<String, Place> auditoriums = new HashMap<String, Place>();
    HashMap<String, Place> places = new HashMap<String, Place>();

    HashMap<String, Subgroup> subgroups = new HashMap<String, Subgroup>();
    HashMap<String, Discipline> disciplines = new HashMap<String, Discipline>();
    HashMap<String, LessonType> lessonTypes = new HashMap<String, LessonType>();


    private JSONObject timetable;

    public ScheduleJSONProcessor(JSONObject json) throws JSONException {
        Iterator<?> keys;
        JSONObject auditoriums = json.getJSONObject("auditoriums");
        JSONObject places = json.getJSONObject("places");
        JSONObject types = json.getJSONObject("types");
        JSONObject subgroups = json.getJSONObject("groups");
        JSONObject disciplines = json.getJSONObject("disciplines");


        keys = auditoriums.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            this.auditoriums.put(key, new Place(auditoriums.getString(key)));
        }

        keys = places.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            this.places.put(key, new Place(places.getString(key)));
        }

        keys = types.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            this.lessonTypes.put(key, new LessonType(key, types.getString(key)));
        }

        keys = subgroups.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            this.subgroups.put(key, new Subgroup(key, subgroups.getString(key)));
        }

        keys = disciplines.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            JSONObject discipline = disciplines.getJSONObject(key);
            this.disciplines.put(key, new Discipline(key, discipline.getString("long_name"), discipline.getString("short_name")));
        }

        this.timetable = json.getJSONObject("timetable");
    }

    public ArrayList<ScheduleItem> getScheduleItems() throws JSONException {
        ArrayList<ScheduleItem> result = new ArrayList<ScheduleItem>();

        Iterator<?> keys = this.timetable.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            JSONObject item = this.timetable.getJSONObject(key);
            result.add(this.getScheduleItemFromJSON(item));
        }

        Collections.sort(result, new Comparator<ScheduleItem>() {
            public int compare(ScheduleItem a, ScheduleItem b) {
                if (a.getTimeStart().getTime() == b.getTimeStart().getTime()) {
                    return 0;
                } else {
                    return a.getTimeStart().getTime() - b.getTimeStart().getTime() > 0 ? 1 : -1;
                }
            }
        });

        return result;
    }

    private ScheduleItem getScheduleItemFromJSON(JSONObject item) throws JSONException {
        long timeStart = item.getLong("time_start");
        long timeEnd = item.getLong("time_end");
        String title;

        String eventType = item.getString("event");
        if (eventType.equals("event")) {
            title = item.getString("title");
            Place place = null;
            String placeId = item.getString("place");
            String auditoriumId = item.getString("auditorium");
            if (!placeId.equals("0")) {
                place = this.places.get(placeId);
            } else if (!auditoriumId.equals("0")) {
                place = this.auditoriums.get(auditoriumId);
            }
            return new ScheduleItem(timeStart, timeEnd, title, place);
        } else if (eventType.equals("lesson")) {
            Discipline discipline = this.disciplines.get(item.getString("discipline"));

            JSONObject subgroupsIds = item.getJSONObject("subgroups");
            ArrayList<Subgroup> subgroups = new ArrayList<Subgroup>();
            Iterator<?> keys = subgroupsIds.keys();

            HashSet<String> uniqueIds = new HashSet<String>(); //фиксит баг в API (дублирующиеся id учебных групп), временный костыль
            while (keys.hasNext()) {
                String subgroupId = subgroupsIds.getString((String) keys.next());
                if (!uniqueIds.contains(subgroupId)) {
                    uniqueIds.add(subgroupId);
                    subgroups.add(this.subgroups.get(subgroupId));
                }
            }

            LessonType lessonType = this.lessonTypes.get(item.getString("type"));

            Place place = null;
            String auditoriumId = item.getString("auditorium");
            if (!auditoriumId.equals("0")) {
                place = this.auditoriums.get(auditoriumId);
            }

            return new ScheduleItem(timeStart, timeEnd, place, subgroups, discipline, lessonType);
        }
        return null;
    }

    public FilterSpinnerItemsContainer getScheduleFiltersData() throws JSONException {
        ArrayList<Subgroup> subgroups = new ArrayList<Subgroup>(this.subgroups.values());
        ArrayList<Discipline> disciplines = new ArrayList<Discipline>(this.disciplines.values());
        ArrayList<LessonType> lessonTypes = new ArrayList<LessonType>(this.lessonTypes.values());

        return new FilterSpinnerItemsContainer(subgroups, disciplines, lessonTypes);
    }
}