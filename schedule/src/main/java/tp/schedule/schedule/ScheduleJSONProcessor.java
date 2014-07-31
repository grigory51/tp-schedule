package tp.schedule.schedule;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Comparator;


/**
 * author: grigory51
 * date: 04.07.14
 */
public class ScheduleJSONProcessor {
    private JSONObject auditories, types, subgroups, disciplines, places;
    private JSONObject timetable;

    public ScheduleJSONProcessor(JSONObject json) throws JSONException {
        this.auditories = json.getJSONObject("auditoriums");
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
            String eventType = item.getString("event");
            if (eventType.equals("event")) {
                title = item.getString("title");

                String placeId = item.getString("place");
                String auditoriumId = item.getString("auditorium");
                if (!placeId.equals("0")) {
                    placeTitle = this.places.getString(placeId);
                } else if (!auditoriumId.equals("0")) {
                    placeTitle = this.auditories.getString(auditoriumId);
                }
            } else if (eventType.equals("lesson")) {
                title = this.disciplines.getJSONObject(item.getString("discipline")).getString("long_name");
                String auditoriumId = item.getString("auditorium");
                if (!auditoriumId.equals("0")) {
                    placeTitle = this.auditories.getString(auditoriumId);
                }
            }
            result.add(new ScheduleItem(timeStart, timeEnd, title, placeTitle, new String[]{}, eventType));
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

    public ScheduleFilter getScheduleFilter() throws JSONException {
        return new ScheduleFilter(this.disciplines, this.types, this.subgroups);
    }
}
