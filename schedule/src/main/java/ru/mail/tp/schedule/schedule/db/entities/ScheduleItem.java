package ru.mail.tp.schedule.schedule.db.entities;

import android.content.ContentValues;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import ru.mail.tp.schedule.utils.MoscowCalendar;
import ru.mail.tp.schedule.utils.MoscowSimpleDateFormat;


/**
 * author: grigory51
 * date: 07/10/14
 */
public class ScheduleItem extends BaseEntity implements Serializable {
    public static final TableColumn COLUMN_NAME_ID = new TableColumn(_ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
    public static final TableColumn COLUMN_NAME_SCHEDULE_ID = new TableColumn("schedule_id", "INT");
    public static final TableColumn COLUMN_NAME_EVENT_TYPE = new TableColumn("event_type", "INT");
    public static final TableColumn COLUMN_NAME_TIME_START = new TableColumn("time_start", "INT");
    public static final TableColumn COLUMN_NAME_TIME_END = new TableColumn("time_end", "INT");
    public static final TableColumn COLUMN_NAME_TITLE = new TableColumn("title", "TEXT");
    public static final TableColumn COLUMN_NAME_DISCIPLINE_ID = new TableColumn("discipline_id", "INT");
    public static final TableColumn COLUMN_NAME_SUBGROUP_IDS = new TableColumn("subgroup_ids", "TEXT");
    public static final TableColumn COLUMN_NAME_LESSON_TYPE_ID = new TableColumn("lesson_type_id", "INT");
    public static final TableColumn COLUMN_NAME_NUMBER = new TableColumn("number", "INT");
    public static final TableColumn COLUMN_NAME_AUDITORIUM_ID = new TableColumn("auditorium_id", "INT");
    public static final TableColumn COLUMN_NAME_PLACE_ID = new TableColumn("place_id", "INT");

    private static final Comparator<Subgroup> subgroupComparator = new Comparator<Subgroup>() {
        public int compare(Subgroup a, Subgroup b) {
            return a.getTitle().compareTo(b.getTitle());
        }
    };


    private final int id;
    private final EventType eventType;
    private final String title;
    private final Place place;
    private final Discipline discipline;
    private final ArrayList<Subgroup> subgroups;
    private final LessonType lessonType;
    private final int number;
    private Date timeStart;
    private Date timeEnd;

    private ScheduleItem() {
        this(0, 0, 0, "", null);
    }

    public ScheduleItem(int id, long timeStart, long timeEnd, String title, Place place) {
        this(id, EventType.EVENT, timeStart, timeEnd, title, place, null, null, null, 0);
    }

    public ScheduleItem(int id, long timeStart, long timeEnd, Place place, ArrayList<Subgroup> subgroups, Discipline discipline, LessonType lessonType, int number) {
        this(id, EventType.LESSON, timeStart, timeEnd, null, place, subgroups, discipline, lessonType, number);
    }

    private ScheduleItem(int id, EventType eventType, long timeStart, long timeEnd, String title, Place place, ArrayList<Subgroup> subgroups, Discipline discipline, LessonType lessonType, int number) {
        this.id = id;
        this.eventType = eventType;
        this.timeStart = new Date(timeStart);
        this.timeEnd = new Date(timeEnd);
        this.title = title;
        if (subgroups != null) {
            this.subgroups = subgroups;
            Collections.sort(this.subgroups, subgroupComparator);
        } else {
            this.subgroups = new ArrayList<Subgroup>();
        }
        this.discipline = discipline;
        this.lessonType = lessonType;
        this.number = number;
        this.place = place;

        //time fixing
        try {
            Calendar startCalendar = MoscowCalendar.getInstance();
            Calendar endCalendar = MoscowCalendar.getInstance();
            Calendar todayCalendar = MoscowCalendar.getInstance();

            todayCalendar.setTime(new SimpleDateFormat("MM/dd/yy").parse("10/26/14"));
            startCalendar.setTime(this.timeStart);
            endCalendar.setTime(this.timeEnd);

            if (startCalendar.before(todayCalendar)) {
                startCalendar.set(Calendar.MINUTE, startCalendar.get(Calendar.MINUTE) + 30); //Почему если прибавить полчаса будет корректировка на час
                this.timeStart = startCalendar.getTime();                                    //а если прибавить час, то отображаться будет +2 часа??? Wtf??
            }
            if (endCalendar.before(todayCalendar)) {
                endCalendar.set(Calendar.MINUTE, endCalendar.get(Calendar.MINUTE) + 30);
                this.timeEnd = endCalendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static ScheduleItem instance() {
        return new ScheduleItem();
    }

    int getId() {
        return this.id;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public Date getTimeStart() {
        return this.timeStart;
    }

    public Date getTimeEnd() {
        return this.timeEnd;
    }

    public String getPlaceTitle() {
        return this.getPlace() == null ? "" : this.getPlace().getTitle();
    }

    public String getTitle() {
        if (this.getEventType() == EventType.EVENT) {
            return this.title;
        } else if (this.getEventType() == EventType.LESSON) {
            return this.getDiscipline().getTitle();
        }
        return "";
    }

    public Place getPlace() {
        return this.place;
    }

    int getAuditoriumId() {
        if (this.getPlace() != null && this.getPlace().getType() == PlaceType.AUDITORY) {
            return this.getPlace().getId();
        } else {
            return 0;
        }
    }

    int getPlaceId() {
        if (this.getPlace() != null && this.getPlace().getType() == PlaceType.PLACE) {
            return this.getPlace().getId();
        } else {
            return 0;
        }
    }

    Discipline getDiscipline() {
        return this.discipline;
    }

    int getDisciplineId() {
        return this.getDiscipline() != null ? this.getDiscipline().getId() : 0;
    }

    LessonType getLessonType() {
        return this.lessonType;
    }

    int getLessonTypeId() {
        return this.getLessonType() != null ? this.getLessonType().getId() : 0;
    }

    int getNumber() {
        return number;
    }

    ArrayList<Subgroup> getSubgroups() {
        return subgroups;
    }

    public String getShortTitle() {
        if (this.getEventType() == EventType.EVENT) {
            return this.title;
        } else {
            return this.discipline.getShortTitle();
        }
    }

    public String getSubgroupsList() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.getSubgroups().size(); i++) {
            result.append(this.getSubgroups().get(i).getTitle());
            if (i < this.getSubgroups().size() - 1) {
                result.append(", ");
            }
        }
        return result.toString();
    }

    public String getDate() {
        Calendar calendar = MoscowCalendar.getInstance();
        calendar.setTimeInMillis(this.getTimeStart().getTime());
        return MoscowCalendar.getWeekDayTitle(calendar.get(Calendar.DAY_OF_WEEK) - 2) + ", " + calendar.get(Calendar.DAY_OF_MONTH) + " " + MoscowCalendar.getMonthTitle(calendar.get(Calendar.MONTH));
    }

    public String getFormatTimeStart(String format) {
        return new MoscowSimpleDateFormat(format).format(this.getTimeStart());
    }

    @SuppressWarnings("SameParameterValue")
    public String getFormatTimeEnd(String format) {
        return new MoscowSimpleDateFormat(format).format(this.getTimeEnd());
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

    public String getSubtitle() {
        if (this.getEventType() == EventType.LESSON) {
            return this.getLessonType().getTitle() + " " + this.getNumber() + "\n" + this.getSubgroupsList();
        } else {
            return "";
        }
    }

    @Override
    public TableColumn[] getColumns() {
        return new TableColumn[]{
                COLUMN_NAME_ID,
                COLUMN_NAME_SCHEDULE_ID,
                COLUMN_NAME_EVENT_TYPE,
                COLUMN_NAME_TIME_START,
                COLUMN_NAME_TIME_END,
                COLUMN_NAME_TITLE,
                COLUMN_NAME_DISCIPLINE_ID,
                COLUMN_NAME_SUBGROUP_IDS,
                COLUMN_NAME_LESSON_TYPE_ID,
                COLUMN_NAME_NUMBER,
                COLUMN_NAME_AUDITORIUM_ID,
                COLUMN_NAME_PLACE_ID
        };
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        StringBuilder subgroupIds = new StringBuilder();
        for (int i = 0; i < this.getSubgroups().size(); i++) {
            subgroupIds.append(this.getSubgroups().get(i).getId());
            if ((i + 1) < this.getSubgroups().size()) {
                subgroupIds.append(",");
            }
        }

        values.put(COLUMN_NAME_SCHEDULE_ID.getName(), this.getId());
        values.put(COLUMN_NAME_EVENT_TYPE.getName(), this.getEventType().ordinal());
        values.put(COLUMN_NAME_TIME_START.getName(), this.getTimeStart().getTime());
        values.put(COLUMN_NAME_TIME_END.getName(), this.getTimeEnd().getTime());
        values.put(COLUMN_NAME_TITLE.getName(), this.getTitle());
        values.put(COLUMN_NAME_DISCIPLINE_ID.getName(), this.getDisciplineId());
        values.put(COLUMN_NAME_LESSON_TYPE_ID.getName(), this.getLessonTypeId());
        values.put(COLUMN_NAME_NUMBER.getName(), this.getNumber());
        values.put(COLUMN_NAME_AUDITORIUM_ID.getName(), this.getAuditoriumId());
        values.put(COLUMN_NAME_PLACE_ID.getName(), this.getPlaceId());
        values.put(COLUMN_NAME_SUBGROUP_IDS.getName(), subgroupIds.toString());

        return values;
    }

    @Override
    public String getTableName() {
        return "schedule_item";
    }

    @Override
    protected String[] getIndexes() {
        return new String[]{
                "UNIQUE (" + COLUMN_NAME_EVENT_TYPE.getName() + ", " + COLUMN_NAME_SCHEDULE_ID.getName() + ") ON CONFLICT REPLACE"
        };
    }
}