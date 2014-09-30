package ru.mail.tp.schedule.schedule.entities;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import ru.mail.tp.schedule.schedule.ScheduleFilter;
import ru.mail.tp.schedule.utils.MoscowCalendar;
import ru.mail.tp.schedule.utils.MoscowSimpleDateFormat;
import ru.mail.tp.schedule.utils.StringHelper;

public class ScheduleItem implements Serializable {
    private static final String[] weekDays = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
    private static final String[] month = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};

    private static final Comparator<Subgroup> subgroupComparator = new Comparator<Subgroup>() {
        public int compare(Subgroup a, Subgroup b) {
            return a.getTitle().compareTo(b.getTitle());
        }
    };

    private final Date timeStart;
    private final Date timeEnd;
    private final Type type;
    private final String title;

    private final Place place;
    private final ArrayList<Subgroup> subgroups;
    private final Discipline discipline;
    private final LessonType lessonType;

    public ScheduleItem(long timeStart, long timeEnd, String title, Place place) {
        this(Type.EVENT, timeStart, timeEnd, title, place, null, null, null);
    }

    public ScheduleItem(long timeStart, long timeEnd, Place place, ArrayList<Subgroup> subgroups, Discipline discipline, LessonType lessonType) {
        this(Type.LESSON, timeStart, timeEnd, null, place, subgroups, discipline, lessonType);
    }

    private ScheduleItem(Type type, long timeStart, long timeEnd, String title, Place place, ArrayList<Subgroup> subgroups, Discipline discipline, LessonType lessonType) {
        this.type = type;
        this.timeStart = new Date(timeStart);
        this.timeEnd = new Date(timeEnd);

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

    public Date getTimeStart() {
        return this.timeStart;
    }

    Date getTimeEnd() {
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

    Discipline getDiscipline() {
        return this.discipline;
    }

    LessonType getLessonType() {
        return this.lessonType;
    }

    ArrayList<Subgroup> getSubgroups() {
        return this.subgroups;
    }

    Place getPlace() {
        return place;
    }

    public boolean isToday() {
        return MoscowCalendar.getTodayInstance().getTimeInMillis() == this.getDayStart();
    }

    public long getDayStart() {
        Calendar calendar = MoscowCalendar.getInstance();

        calendar.setTimeInMillis(this.getTimeStart().getTime());
        MoscowCalendar.resetTime(calendar);

        return calendar.getTimeInMillis();
    }

    long getDayEnd() {
        Calendar calendar = MoscowCalendar.getInstance();

        calendar.setTimeInMillis(this.getTimeEnd().getTime());
        MoscowCalendar.resetTime(calendar);

        return calendar.getTimeInMillis();
    }

    public String getDate() {
        Calendar calendar = MoscowCalendar.getInstance();
        calendar.setTimeInMillis(this.getTimeStart().getTime());

        return weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 2] + ", " + calendar.get(Calendar.DAY_OF_MONTH) + " " + month[calendar.get(Calendar.MONTH)];
    }

    @SuppressWarnings("SameParameterValue")
    public String getFormatTimeStart(String format) {
        return new MoscowSimpleDateFormat(format).format(this.getTimeStart());
    }

    @SuppressWarnings("SameParameterValue")
    public String getFormatTimeEnd(String format) {
        return new MoscowSimpleDateFormat(format).format(this.getTimeEnd());
    }

    public String getSubtitle() {
        if (this.getType() == Type.LESSON) {
            StringBuilder result = new StringBuilder();
            result.append(this.getLessonType().getTitle());
            result.append("\n");
            for (int i = 0; i < this.getSubgroups().size(); i++) {
                result.append(this.getSubgroups().get(i).getTitle());
                if (i < this.getSubgroups().size() - 1) {
                    result.append(", ");
                }
            }
            return result.toString();
        } else {
            return "";
        }
    }

    public String getLocation() {
        return this.getPlace().getTitle();
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
