package ru.mail.tp.schedule.schedule.db.entities;

import android.content.ContentValues;

import ru.mail.tp.schedule.schedule.filter.IFilterSpinner;

/**
 * author: grigory51
 * date: 07/10/14
 */
public class Discipline extends BaseEntity implements IFilterSpinner {
    public static final TableColumn COLUMN_NAME_TITLE = new TableColumn("title", "TEXT");
    public static final TableColumn COLUMN_NAME_SHORT_TITLE = new TableColumn("short_title", "TEXT");

    private final int id;
    private final String title;
    private final String shortTitle;

    private Discipline() {
        this(0, "", "");
    }

    public Discipline(String id, String title, String shortTitle) {
        this(Integer.parseInt(id), title, shortTitle);
    }

    @SuppressWarnings("SameParameterValue")
    public Discipline(String title) {
        this(0, title, title);
    }

    public Discipline(int id, String title, String shortTitle) {
        this.id = id;
        this.title = title;
        this.shortTitle = shortTitle;
    }

    public static Discipline instance() {
        return new Discipline();
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getShortTitle() {
        return this.shortTitle;
    }

    @Override
    public String getFilterTitle() {
        return !this.getShortTitle().equals("") ? this.getShortTitle() : this.getTitle();
    }

    @Override
    public TableColumn[] getColumns() {
        return new TableColumn[]{
                COLUMN_NAME_ID,
                COLUMN_NAME_TITLE,
                COLUMN_NAME_SHORT_TITLE
        };
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ID.getName(), this.getId());
        values.put(COLUMN_NAME_TITLE.getName(), this.getTitle());
        values.put(COLUMN_NAME_SHORT_TITLE.getName(), this.getShortTitle());
        return values;
    }

    @Override
    public String getTableName() {
        return "discipline";
    }
}