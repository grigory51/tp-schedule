package ru.mail.tp.schedule.schedule.entities;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import ru.mail.tp.schedule.schedule.filter.ScheduleFilter;
import ru.mail.tp.schedule.utils.MoscowCalendar;
import ru.mail.tp.schedule.utils.MoscowSimpleDateFormat;
import ru.mail.tp.schedule.utils.StringHelper;

public class ScheduleItem implements Serializable {
    private static final Comparator<Subgroup> subgroupComparator = new Comparator<Subgroup>() {
        public int compare(Subgroup a, Subgroup b) {
            return a.getTitle().compareTo(b.getTitle());
        }
    };

    private final Date timeStart;
    private final Date timeEnd;
    private final Type type;
    private final String title;
    private final int number;

    private final Place place;
    private final ArrayList<Subgroup> subgroups;
    private final ArrayList<Tutor> tutors;
    private final Discipline discipline;
    private final LessonType lessonType;


    public ScheduleItem(long timeStart, long timeEnd, String title, Place place) {
        this(Type.EVENT, timeStart, timeEnd, title, place, null, null, null, null, 0);
    }

    public ScheduleItem(long timeStart, long timeEnd, Place place, ArrayList<Tutor> tutors, ArrayList<Subgroup> subgroups, Discipline discipline, LessonType lessonType, int number) {
        this(Type.LESSON, timeStart, timeEnd, null, place, tutors, subgroups, discipline, lessonType, number);
    }

    private ScheduleItem(Type type, long timeStart, long timeEnd, String title, Place place, ArrayList<Tutor> tutors, ArrayList<Subgroup> subgroups, Discipline discipline, LessonType lessonType, int number) {
        this.type = type;
        this.timeStart = new Date(timeStart);
        this.timeEnd = new Date(timeEnd);

        this.title = StringHelper.quotesFormat(title);
        if (place != null) {
            this.place = place;
        } else {
            this.place = new Place();
        }

        if (subgroups != null) {
            this.subgroups = subgroups;
            Collections.sort(this.subgroups, subgroupComparator);
        } else {
            this.subgroups = new ArrayList<Subgroup>();
        }

        if (tutors != null) {
            this.tutors = tutors;
        } else {
            this.tutors = new ArrayList<Tutor>();
        }

        this.discipline = discipline;
        this.lessonType = lessonType;
        this.number = number;
    }

    public Date getTimeStart() {
        return this.timeStart;
    }

    public Date getTimeEnd() {
        return this.timeEnd;
    }

    public String getTitle() {
        if (this.type == Type.EVENT) {
            return this.title;
        } else {
            return this.discipline.getTitle();
        }
    }

    public String getShortTitle() {
        if (this.type == Type.EVENT) {
            return this.title;
        } else {
            return this.discipline.getShortTitle();
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

    public ArrayList<Tutor> getTutors() {
        return tutors;
    }

    public int getNumber() {
        return this.number;
    }

    Place getPlace() {
        return this.place;
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

    public String getDate() {
        Calendar calendar = MoscowCalendar.getInstance();
        calendar.setTimeInMillis(this.getTimeStart().getTime());

        return MoscowCalendar.getWeekDayTitle(calendar.get(Calendar.DAY_OF_WEEK) - 2) + ", " + calendar.get(Calendar.DAY_OF_MONTH) + " " + MoscowCalendar.getMonthTitle(calendar.get(Calendar.MONTH));
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
            result.append(this.getLessonType().getTitle()).append(" ").append(this.getNumber());
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
