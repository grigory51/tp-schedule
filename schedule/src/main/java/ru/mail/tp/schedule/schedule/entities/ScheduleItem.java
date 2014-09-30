package ru.mail.tp.schedule.schedule.entities;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.TimeZone;

import ru.mail.tp.schedule.schedule.ScheduleFilter;

public class ScheduleItem implements Serializable {
    private static String[] weekDays = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
    private static String[] month = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};

    private static Comparator<Subgroup> subgroupComparator = new Comparator<Subgroup>() {
        public int compare(Subgroup a, Subgroup b) {
            return a.getTitle().compareTo(b.getTitle());
        }
    };

    private long timeStart, timeEnd;
    private Type type;
    private String title;

    private Place place;
    private ArrayList<Subgroup> subgroups;
    private Discipline discipline;
    private LessonType lessonType;

    public ScheduleItem(Type type, long timeStart, long timeEnd, String title, Place place) {
        this(type, timeStart, timeEnd, title, place, null, null, null);
    }

    public ScheduleItem(Type type, long timeStart, long timeEnd, Place place, ArrayList<Subgroup> subgroups, Discipline discipline, LessonType lessonType) {
        this(type, timeStart, timeEnd, null, place, subgroups, discipline, lessonType);
    }

    private ScheduleItem(Type type, long timeStart, long timeEnd, String title, Place place, ArrayList<Subgroup> subgroups, Discipline discipline, LessonType lessonType) {
        this.type = type;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;

        this.title = StringHelper.quotesFormat(title);
        this.place = place;

        if (subgroups != null) {
            this.subgroups = subgroups;
            Collections.sort(this.subgroups, subgroupComparator);
        } else {
            this.subgroups = new ArrayList<Subgroup>();
        }

        this.discipline = discipline;
        this.lessonType = lessonType;
    }

    public long getTimeStart() {
        return this.timeStart;
    }

    public long getTimeEnd() {
        return this.timeEnd;
    }

    public String getTitle() {
        if (this.type == Type.EVENT) {
            return this.title;
        } else {
            return this.discipline.getTitle();
        }
    }

    public Type getType() {
        return this.type;
    }

    public Discipline getDiscipline() {
        return this.discipline;
    }

    public LessonType getLessonType() {
        return this.lessonType;
    }

    public ArrayList<Subgroup> getSubgroups() {
        return this.subgroups;
    }

    public Place getPlace() {
        return place;
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

        return weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 2] + ", " + calendar.get(Calendar.DAY_OF_MONTH) + " " + month[calendar.get(Calendar.MONTH)];
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

    public String getSubtitle() {
        if (this.getType() == Type.EVENT) {
            return this.getPlace().getTitle();
        } else {
            StringBuilder result = new StringBuilder();
            result.append(this.getLessonType().getTitle());
            result.append("\n");
            for (int i = 0; i < this.getSubgroups().size(); i++) {
                result.append(this.getSubgroups().get(i).getTitle());
                if (i < this.getSubgroups().size() - 1) {
                    result.append(", ");
                }
            }
            result.append("\n");
            result.append(this.getPlace().getTitle());

            return result.toString();
        }
    }

    public boolean isFilterMatch(ScheduleFilter filter) {
        if (!filter.isShowPassed() && MoscowCalendar.getTodayInstance().getTimeInMillis() > this.getDayStart()) {
            return false;
        }
        if (filter.getDisciplineId() != 0 && (this.getDiscipline() == null || this.getDiscipline().getId() != filter.getDisciplineId())) {
            return false;
        }
        if (filter.getSubgroupId() != 0) {
            boolean match = false;
            for (Subgroup subgroup : this.getSubgroups()) {
                if (filter.getSubgroupId() == subgroup.getId()) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }
        //noinspection RedundantIfStatement
        if (filter.getLessonTypeId() != 0 && (this.getLessonType() == null || this.getLessonType().getId() != filter.getLessonTypeId())) {
            return false;
        }
        return true;
    }


}
