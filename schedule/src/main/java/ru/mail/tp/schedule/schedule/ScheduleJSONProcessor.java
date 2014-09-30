package ru.mail.tp.schedule.schedule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

import ru.mail.tp.schedule.schedule.entities.Discipline;
import ru.mail.tp.schedule.schedule.entities.LessonType;
import ru.mail.tp.schedule.schedule.entities.Place;
import ru.mail.tp.schedule.schedule.entities.ScheduleItem;
import ru.mail.tp.schedule.schedule.entities.Subgroup;


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
        String placeTitle = "";
        String title;

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
            return new ScheduleItem(timeStart, timeEnd, title, new Place(placeTitle));
        } else if (eventType.equals("lesson")) {
            String disciplineId = item.getString("discipline");
            JSONObject disciplineJSON = this.disciplines.getJSONObject(disciplineId);

            Discipline discipline = new Discipline(disciplineId, disciplineJSON.getString("long_name"), disciplineJSON.getString("short_name"));

            JSONObject subgroupsIds = item.getJSONObject("subgroups");
            ArrayList<Subgroup> subgroups = new ArrayList<Subgroup>();
            Iterator<?> keys = subgroupsIds.keys();

            HashSet<String> uniqueIds = new HashSet<String>(); //фиксит баг в API (дублирующиеся id учебных групп), временный костыль
            while (keys.hasNext()) {
                String subgroupId = subgroupsIds.getString((String) keys.next());
                if (!uniqueIds.contains(subgroupId)) {
                    uniqueIds.add(subgroupId);
                    String subgroupTitle = this.subgroups.getString(subgroupId);
                    subgroups.add(new Subgroup(subgroupId, subgroupTitle));
                }
            }

            String lessonTypeId = item.getString("type");
            LessonType lessonType = new LessonType(lessonTypeId, this.types.getString(lessonTypeId));

            Place place = null;
            String auditoriumId = item.getString("auditorium");
            if (!auditoriumId.equals("0")) {
                place = new Place(this.auditoriums.getString(auditoriumId));
            }

            return new ScheduleItem(timeStart, timeEnd, place, subgroups, discipline, lessonType);
        }
        return null;
    }

    public FilterSpinnerItemsContainer getScheduleFiltersData() throws JSONException {
        return new FilterSpinnerItemsContainer(this.disciplines, this.types, this.subgroups);
    }
}