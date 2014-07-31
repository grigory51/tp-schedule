package tp.schedule.schedule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class ScheduleFilter implements Serializable {
    ArrayList<String> subgroupTitles = new ArrayList<String>();
    ArrayList<String> disciplineTitles = new ArrayList<String>();
    ArrayList<String> typeTitles = new ArrayList<String>();

    ArrayList<String> subgroupIds = new ArrayList<String>();
    ArrayList<String> disciplineIds = new ArrayList<String>();
    ArrayList<String> typeIds = new ArrayList<String>();

    ScheduleFilter(JSONObject disciplines, JSONObject types, JSONObject subgroups) throws JSONException {
        String key;

        Iterator<?> subgroupIds = subgroups.keys();
        while (subgroupIds.hasNext()) {
            key = (String) subgroupIds.next();
            this.subgroupTitles.add(subgroups.getString(key));
            this.subgroupIds.add(key);
        }

        Iterator<?> disciplineIds = disciplines.keys();
        while (disciplineIds.hasNext()) {
            key = (String) disciplineIds.next();
            JSONObject discipline = disciplines.getJSONObject(key);
            this.disciplineTitles.add(discipline.getString("short_name"));
            this.disciplineIds.add(key);
        }

        Iterator<?> typeIds = types.keys();
        while (typeIds.hasNext()) {
            key = (String) typeIds.next();
            this.typeTitles.add(types.getString(key));
            this.typeIds.add(key);
        }
    }

    public ArrayList<String> getSubgroupTitles() {
        return this.subgroupTitles;
    }

    public ArrayList<String> getDisciplineTitles() {
        return this.disciplineTitles;
    }

    public ArrayList<String> getTypeTitles() {
        return this.typeTitles;
    }
}
