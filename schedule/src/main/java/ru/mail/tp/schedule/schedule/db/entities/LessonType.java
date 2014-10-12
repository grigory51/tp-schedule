package ru.mail.tp.schedule.schedule.db.entities;

import android.content.ContentValues;

import ru.mail.tp.schedule.schedule.filter.IFilterSpinner;

/**
 * author: grigory51
 * date: 28/09/14
 */
public class LessonType extends BaseEntity implements IFilterSpinner {
    public static final TableColumn COLUMN_NAME_TITLE = new TableColumn("title", "TEXT");

    private final int id;
    private final String title;

    private LessonType() {
        this(0, "");
    }

    public LessonType(String id, String title) {
        this(Integer.parseInt(id), title);
    }

    public LessonType(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public static LessonType instance() {
        return new LessonType();
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public String getFilterTitle() {
        return this.getTitle();
    }

    @Override
    public TableColumn[] getColumns() {
        return new TableColumn[]{
                COLUMN_NAME_ID,
                COLUMN_NAME_TITLE
        };
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ID.getName(), this.getId());
        values.put(COLUMN_NAME_TITLE.getName(), this.getTitle());
        return values;
    }

    @Override
    public String getTableName() {
        return "lesson_type";
    }
}
