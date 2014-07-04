package tp.schedule.schedule;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.TimeZone;

import tp.schedule.utils.Filter;

public class ScheduleItem {
    private long timeStart, timeEnd;
    private String title;
    private String placeTitle;
    private String[] subgroupTitles;
    private String type; //event или lesson

    private static String[] weekDays = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
    private static String[] month = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};

    public ScheduleItem(long timeStart, long timeEnd, String title, String placeTitle, String[] subgroupTitles, String type) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.title = title;
        this.placeTitle = placeTitle;
        this.subgroupTitles = subgroupTitles;
        this.type = type;
    }

    private void fillTime(long timeStart, long timeEnd) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
        this.timeStart = timeStart;

        calendar.setTimeInMillis(timeStart);
        /*date = initDate(calendar);
        weekDay = weekDays[(calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7];*/

        StringBuilder sb = new StringBuilder();
        sb.append(initTime(calendar));
        sb.append("–");
        calendar.setTimeInMillis(timeEnd);
        sb.append(initTime(calendar));
        /*time = sb.toString();*/
    }

    private String intToStr(int time) {
        if (time < 10) {
            return "0" + Integer.toString(time);
        } else {
            return Integer.toString(time);
        }
    }

    private String initDate(Calendar calendar) {
        return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " + month[calendar.get(Calendar.MONTH)];
    }

    private String initTime(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY), minute = calendar.get(Calendar.MINUTE);
        return intToStr(hour) + ":" + intToStr(minute);
    }

    private void fillTutors(JSONObject json) throws JSONException {
        /*tutors = new Integer[json.length()];
        for (int count = 0; count < tutors.length; count++) {
            tutors[count] = json.getInt(Integer.toString(count));
        }*/
    }

    private void fillSubgroups(JSONObject json) throws JSONException {
        /*subgroups = new Integer[json.length()];
        for (int count = 0; count < subgroups.length; count++) {
            subgroups[count] = json.getInt(Integer.toString(count));
        }*/
    }

    public String getDate() {
        /*return date;*/
        return null;
    }

    public String getTime() {
        /*return time;*/
        return null;
    }

    public String getWeekDay() {
        /*return weekDay;*/
        return null;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public String getTutor() {
        /*StringBuilder sb = new StringBuilder();
        for (int count = 0; count < tutors.length; count++) {
            if (count > 0) {
                sb.append(", ");
            }
            try {
                sb.append(Dater.instance().getTutor(tutors[count]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();*/
        return null;
    }

    public String getSubgroups() {
        /*StringBuilder sb = new StringBuilder();
        for (int count = 0; count < subgroups.length; count++) {
            if (count > 0) {
                sb.append(", ");
            }
            try {
                sb.append(Dater.instance().getGroup(subgroups[count]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();*/
        return null;
    }

    public String getDiscipline() {
        /*return Dater.instance().getDiscipline(discipline);*/
        return null;
    }

    public String getType() {
        try {
            /*if (type < 2) {
                return Dater.instance().getType(type) + " №" + Integer.toString(number);
            } else {
                return Dater.instance().getType(type);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAuditory() {
        /*return Dater.instance().getAuditory(auditory);*/
        return null;
    }

    public boolean isSatisfy(Filter filter) {
        return isSubgroupSatisfy(filter.getSubgroup()) && isDisciplineSatisfy(filter.getDiscipline()) && isTypeSatisfy(filter.getType());
    }

    private boolean isSubgroupSatisfy(int subgroup) {
        if (subgroup == -1) {
            return true;
        }
        /*for (int group : subgroups) {
            if (group == subgroup) {
                return true;
            }
        }*/
        return false;
    }

    private boolean isDisciplineSatisfy(int discipline) {
        return (discipline == -1);
    }

    private boolean isTypeSatisfy(int type) {
        return (type == -1);
    }
}
