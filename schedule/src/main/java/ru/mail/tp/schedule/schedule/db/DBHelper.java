package ru.mail.tp.schedule.schedule.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import ru.mail.tp.schedule.schedule.db.entities.Discipline;
import ru.mail.tp.schedule.schedule.db.entities.EventType;
import ru.mail.tp.schedule.schedule.db.entities.LessonType;
import ru.mail.tp.schedule.schedule.db.entities.Place;
import ru.mail.tp.schedule.schedule.db.entities.PlaceType;
import ru.mail.tp.schedule.schedule.db.entities.ScheduleItem;
import ru.mail.tp.schedule.schedule.db.entities.Subgroup;


/**
 * author: grigory51
 * date: 07/10/14
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "TechnoparkSchedule.db";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(Discipline.instance().createTableSQL());
        database.execSQL(LessonType.instance().createTableSQL());
        database.execSQL(Place.instance().createTableSQL());
        database.execSQL(ScheduleItem.instance().createTableSQL());
        database.execSQL(Subgroup.instance().createTableSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i2) {
        database.execSQL(Discipline.instance().dropTableSQL());
        database.execSQL(LessonType.instance().dropTableSQL());
        database.execSQL(Place.instance().dropTableSQL());
        database.execSQL(ScheduleItem.instance().dropTableSQL());
        database.execSQL(Subgroup.instance().dropTableSQL());
        this.onCreate(database);
    }

    public ArrayList<Discipline> getDisciplines() {
        ArrayList<Discipline> result = new ArrayList<Discipline>();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String table = Discipline.instance().getTableName();

        String projection[] = {
        /*00*/    table + "." + Discipline.COLUMN_NAME_ID.getName(),
        /*01*/    table + "." + Discipline.COLUMN_NAME_TITLE.getName(),
        /*02*/    table + "." + Discipline.COLUMN_NAME_SHORT_TITLE.getName(),
        };
        String order = Discipline.COLUMN_NAME_TITLE.getName() + " ASC";

        queryBuilder.setTables(table);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = queryBuilder.query(this.getReadableDatabase(), projection, null, null, null, null, order);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result.add(new Discipline(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();

        return result;
    }

    public ArrayList<LessonType> getLessonTypes() {
        ArrayList<LessonType> result = new ArrayList<LessonType>();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String table = LessonType.instance().getTableName();

        String projection[] = {
        /*00*/    table + "." + LessonType.COLUMN_NAME_ID.getName(),
        /*01*/    table + "." + LessonType.COLUMN_NAME_TITLE.getName(),
        };
        String order = LessonType.COLUMN_NAME_TITLE.getName() + " ASC";

        queryBuilder.setTables(table);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = queryBuilder.query(this.getReadableDatabase(), projection, null, null, null, null, order);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result.add(new LessonType(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();

        return result;
    }

    public ArrayList<Subgroup> getSubgroups() {
        ArrayList<Subgroup> result = new ArrayList<Subgroup>();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String table = Subgroup.instance().getTableName();

        String projection[] = {
        /*00*/    table + "." + Subgroup.COLUMN_NAME_ID.getName(),
        /*01*/    table + "." + Subgroup.COLUMN_NAME_TITLE.getName(),
        };
        String order = Subgroup.COLUMN_NAME_TITLE.getName() + " ASC";

        queryBuilder.setTables(table);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = queryBuilder.query(this.getReadableDatabase(), projection, null, null, null, null, order);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                result.add(new Subgroup(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();

        return result;
    }


    public ArrayList<ScheduleItem> getScheduleItems() {
        ArrayList<ScheduleItem> result = new ArrayList<ScheduleItem>();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        HashMap<String, Subgroup> subgroupsMap = new HashMap<String, Subgroup>();
        for (Subgroup item : this.getSubgroups()) {
            subgroupsMap.put(Integer.valueOf(item.getId()).toString(), item);
        }

        String scheduleItemTable = ScheduleItem.instance().getTableName();
        String disciplineTable = Discipline.instance().getTableName();
        String lessonTypeTable = LessonType.instance().getTableName();
        String placeTable = Place.instance().getTableName();

        String table = scheduleItemTable +
                " LEFT OUTER JOIN " + disciplineTable +
                " ON (" + scheduleItemTable + "." + ScheduleItem.COLUMN_NAME_DISCIPLINE_ID.getName() + "=" + disciplineTable + "." + Discipline.COLUMN_NAME_ID.getName() + ")" +
                " LEFT OUTER JOIN " + lessonTypeTable +
                " ON (" + scheduleItemTable + "." + ScheduleItem.COLUMN_NAME_LESSON_TYPE_ID.getName() + "=" + lessonTypeTable + "." + LessonType.COLUMN_NAME_ID.getName() + ")" +
                " LEFT OUTER JOIN " + placeTable + " AS auditory" +
                " ON (" + scheduleItemTable + "." + ScheduleItem.COLUMN_NAME_AUDITORIUM_ID.getName() + "=" + "auditory." + Place.COLUMN_NAME_PLACE_ID.getName() + " AND " + PlaceType.AUDITORY.ordinal() + "=auditory." + Place.COLUMN_NAME_TYPE.getName() + ")" +
                " LEFT OUTER JOIN " + placeTable + " AS place" +
                " ON (" + scheduleItemTable + "." + ScheduleItem.COLUMN_NAME_PLACE_ID.getName() + "=" + "place." + Place.COLUMN_NAME_PLACE_ID.getName() + " AND " + PlaceType.PLACE.ordinal() + "=place." + Place.COLUMN_NAME_TYPE.getName() + ")";

        String projection[] = {
        /*00*/    scheduleItemTable + "." + ScheduleItem.COLUMN_NAME_ID.getName(),
        /*01*/    scheduleItemTable + "." + ScheduleItem.COLUMN_NAME_EVENT_TYPE.getName(),
        /*02*/    scheduleItemTable + "." + ScheduleItem.COLUMN_NAME_TIME_START.getName(),
        /*03*/    scheduleItemTable + "." + ScheduleItem.COLUMN_NAME_TIME_END.getName(),
        /*04*/    scheduleItemTable + "." + ScheduleItem.COLUMN_NAME_TITLE.getName(),
        /*05*/    disciplineTable + "." + Discipline.COLUMN_NAME_ID.getName(),
        /*06*/    disciplineTable + "." + Discipline.COLUMN_NAME_TITLE.getName(),
        /*07*/    disciplineTable + "." + Discipline.COLUMN_NAME_SHORT_TITLE.getName(),
        /*08*/    lessonTypeTable + "." + LessonType.COLUMN_NAME_ID.getName(),
        /*09*/    lessonTypeTable + "." + LessonType.COLUMN_NAME_TITLE.getName(),
        /*10*/    scheduleItemTable + "." + ScheduleItem.COLUMN_NAME_NUMBER.getName(),
        /*11*/    "auditory." + Place.COLUMN_NAME_ID.getName(),
        /*12*/    "auditory." + Place.COLUMN_NAME_TYPE.getName(),
        /*13*/    "auditory." + Place.COLUMN_NAME_TITLE.getName(),
        /*14*/    scheduleItemTable + "." + ScheduleItem.COLUMN_NAME_SUBGROUP_IDS.getName()
        };
        String order = scheduleItemTable + "." + ScheduleItem.COLUMN_NAME_TIME_START.getName() + " ASC";

        queryBuilder.setTables(table);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = queryBuilder.query(this.getReadableDatabase(), projection, null, null, null, null, order);

        if (cursor.moveToFirst()) {
            do {
                ScheduleItem item = null;
                int typeIndex = cursor.getInt(1);
                Place place = cursor.isNull(11) ? null : new Place(cursor.getInt(11), cursor.getInt(12), cursor.getString(13));
                if (EventType.EVENT.ordinal() == typeIndex) {

                    item = new ScheduleItem(
                            cursor.getInt(0),
                            cursor.getLong(2),
                            cursor.getLong(3),
                            cursor.getString(4),
                            place
                    );
                } else if (EventType.LESSON.ordinal() == typeIndex) {
                    ArrayList<Subgroup> subgroups = new ArrayList<Subgroup>();

                    for (String subgroupId : cursor.getString(14).split(",")) {
                        if (subgroupsMap.containsKey(subgroupId)) {
                            subgroups.add(subgroupsMap.get(subgroupId));
                        }
                    }

                    item = new ScheduleItem(
                            cursor.getInt(0),
                            cursor.getLong(2),
                            cursor.getLong(3),
                            place,
                            subgroups,
                            new Discipline(cursor.getInt(5), cursor.getString(6), cursor.getString(7)),
                            new LessonType(cursor.getInt(8), cursor.getString(9)),
                            cursor.getInt(10)
                    );
                }
                if (item != null) {
                    result.add(item);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();

        return result;
    }
}