package ru.mail.tp.schedule.schedule;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

public class ScheduleItem implements Serializable {
    private static String[] weekDays = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
    private static String[] month = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
    private long timeStart, timeEnd;
    private String title;
    private String placeTitle;
    private String[] subgroupTitles;
    private String type; //event или lesson
    private String lessonType; //если type == event, то lessonType == null

    private int disciplineId;
    private int[] subgroupsId;
    private int lessonTypeId;

    public ScheduleItem(long timeStart, long timeEnd, String title, String placeTitle, String[] subgroupTitles, String type, String lessonType, int disciplineId, int[] subgroupsId, int lessonTypeId) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.title = title;
        this.placeTitle = placeTitle;
        this.subgroupTitles = subgroupTitles;
        this.type = type;
        this.lessonType = lessonType;

        this.disciplineId = disciplineId;
        this.subgroupsId = subgroupsId;
        this.lessonTypeId = lessonTypeId;
    }

    public long getTimeStart() {
        return this.timeStart;
    }

    public long getTimeEnd() {
        return this.timeEnd;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean isToday() {
        Calendar todayCalendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));

        todayCalendar.set(Calendar.AM_PM, 0);
        todayCalendar.set(Calendar.HOUR, 0);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.SECOND, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);

        return todayCalendar.getTimeInMillis() == this.getDayStart();
    }

    public long getDayStart() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));

        calendar.setTimeInMillis(this.getTimeStart());
        calendar.set(Calendar.AM_PM, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public String getDate() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
        calendar.setTimeInMillis(this.getTimeStart());

        return calendar.get(Calendar.DAY_OF_MONTH) + " " + month[calendar.get(Calendar.MONTH)];
    }

    public String getTimeInterval() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        StringBuilder result = new StringBuilder();

        calendar.setTimeInMillis(this.getTimeStart());
        result.append(dateFormat.format(calendar.getTime()));
        result.append("\n");

        calendar.setTimeInMillis(this.getTimeEnd());
        result.append(dateFormat.format(calendar.getTime()));

        return result.toString();
    }

    public String getType() {
        return type;
    }

    public String getPlaceTitle() {
        return placeTitle;
    }

    public String getSubtitle() {
        if (this.getType().equals("event")) {
            return this.getPlaceTitle();
        } else {
            return this.getLessonType() + "\n" + this.getPlaceTitle();
        }
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

    public String getTime() {
        /*return time;*/
        return null;
    }

    public String getWeekDay() {
        /*return weekDay;*/
        return null;
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

    public String getLessonType() {
        return this.lessonType;
    }



    public boolean isSatisfy(FilterSpinnerItemsContainer filter) {
        //  return isSubgroupSatisfy(filter.getSubgroup()) && isDisciplineSatisfy(filter.getDiscipline()) && isTypeSatisfy(filter.getType());
        return false;
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

    public boolean isFilterMatch(ScheduleFilter filter) {
        if (filter.getDisciplineId() != 0 && this.disciplineId != filter.getDisciplineId()) {
            return false;
        }
        if (filter.getSubgroupId() != 0 && !Arrays.asList(this.subgroupsId).contains(filter.getSubgroupId())) {
            return false;
        }
        if (filter.getLessonTypeId() != 0 && this.lessonTypeId != filter.getLessonTypeId()) {
            return false;
        }
        return true;
    }
}
