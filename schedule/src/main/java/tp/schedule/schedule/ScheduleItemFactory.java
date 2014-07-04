package tp.schedule.schedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tp.schedule.utils.Filter;

/**
 * author: grigory51
 * date: 04.07.14
 */
public class ScheduleItemFactory {
    private JSONObject auditories, types, groups, disciplines, places;
    private JSONObject timetable;

    public ScheduleItemFactory(JSONObject json) throws JSONException {
        this.auditories = json.getJSONObject("auditoriums");
        this.places = json.getJSONObject("places");
        this.types = json.getJSONObject("types");
        this.groups = json.getJSONObject("groups");
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
            String eventType = item.getString("event");
            if (eventType.equals("event")) {
                title = item.getString("title");

                String placeId = item.getString("place");
                String auditoriumId = item.getString("place");
                if (!placeId.equals("0")) {
                    placeTitle = this.places.getString(placeId);
                } else if (!auditoriumId.equals("0")) {
                    placeTitle = this.auditories.getString(auditoriumId);
                }
            } else if (eventType.equals("lesson")) {
                String disciplineId = item.getString("discipline");
                title = this.disciplines.getString(disciplineId);
                String auditoriumId = item.getString("place");
                if (!auditoriumId.equals("0")) {
                    placeTitle = this.auditories.getString(auditoriumId);
                }
            }
            result.add(new ScheduleItem(timeStart, timeEnd, title, placeTitle, new String[]{}, eventType));
        }
        return result;
    }
}
