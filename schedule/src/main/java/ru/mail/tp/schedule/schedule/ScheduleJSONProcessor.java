package ru.mail.tp.schedule.schedule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import ru.mail.tp.schedule.schedule.db.entities.Discipline;
import ru.mail.tp.schedule.schedule.db.entities.LessonType;
import ru.mail.tp.schedule.schedule.db.entities.Place;
import ru.mail.tp.schedule.schedule.db.entities.PlaceType;
import ru.mail.tp.schedule.schedule.db.entities.ScheduleItem;
import ru.mail.tp.schedule.schedule.db.entities.Subgroup;
import ru.mail.tp.schedule.utils.StringHelper;


/**
 * author: grigory51
 * date: 04.07.14
 */
public class ScheduleJSONProcessor {
    private final HashMap<String, Place> auditoriums = new HashMap<String, Place>();
    private final HashMap<String, Place> places = new HashMap<String, Place>();

    private final HashMap<String, Subgroup> subgroups = new HashMap<String, Subgroup>();
    private final HashMap<String, Discipline> disciplines = new HashMap<String, Discipline>();
    private final HashMap<String, LessonType> lessonTypes = new HashMap<String, LessonType>();

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
            this.auditoriums.put(key, new Place(key, PlaceType.AUDITORY, auditoriums.getString(key)));
        }

        keys = places.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            this.places.put(key, new Place(key, PlaceType.PLACE, places.getString(key)));
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
            this.disciplines.put(key, new Discipline(
                    key,
                    StringHelper.quotesFormat(discipline.getString("long_name")),
                    StringHelper.quotesFormat(discipline.getString("short_name"))
            ));
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
        int id = item.getInt("id");
        long timeStart = item.getLong("time_start");
        long timeEnd = item.getLong("time_end");
        String title;

        String eventType = item.getString("event");
        if (eventType.equals("event")) {
            title = StringHelper.quotesFormat(item.getString("title"));
            Place place = null;
            String placeId = item.getString("place");
            String auditoriumId = item.getString("auditorium");
            if (!placeId.equals("0")) {
                place = this.places.get(placeId);
            } else if (!auditoriumId.equals("0")) {
                place = this.auditoriums.get(auditoriumId);
            }
            return new ScheduleItem(id, timeStart, timeEnd, title, place);
        } else if (eventType.equals("lesson")) {
            Discipline discipline = this.disciplines.get(item.getString("discipline"));
            LessonType lessonType = this.lessonTypes.get(item.getString("type"));
            int number = item.getInt("number");

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

            Place place = null;
            String auditoriumId = item.getString("auditorium");
            if (!auditoriumId.equals("0")) {
                place = this.auditoriums.get(auditoriumId);
            }

            return new ScheduleItem(id, timeStart, timeEnd, place, subgroups, discipline, lessonType, number);
        }
        return null;
    }

    public ArrayList<Place> getPlaces() {
        final ArrayList<Place> placesList = new ArrayList<Place>(this.places.values());
        final ArrayList<Place> auditoriumList = new ArrayList<Place>(this.auditoriums.values());

        return new ArrayList<Place>() {{
            addAll(placesList);
            addAll(auditoriumList);
        }};
    }

    public ArrayList<Subgroup> getSubgroups() {
        return new ArrayList<Subgroup>(this.subgroups.values());
    }

    public ArrayList<Discipline> getDisciplines() {
        return new ArrayList<Discipline>(this.disciplines.values());
    }

    public ArrayList<LessonType> getLessonTypes() {
        return new ArrayList<LessonType>(this.lessonTypes.values());
    }
}