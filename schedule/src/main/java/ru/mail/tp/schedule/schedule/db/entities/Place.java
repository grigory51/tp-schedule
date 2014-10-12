package ru.mail.tp.schedule.schedule.db.entities;

import android.content.ContentValues;

/**
 * author: grigory51
 * date: 07/10/14
 */
public class Place extends BaseEntity {
    public static final TableColumn COLUMN_NAME_ID = new TableColumn(_ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
    public static final TableColumn COLUMN_NAME_PLACE_ID = new TableColumn("place_id", "INT");
    public static final TableColumn COLUMN_NAME_TYPE = new TableColumn("type", "INT");
    public static final TableColumn COLUMN_NAME_TITLE = new TableColumn("title", "TEXT");

    private final int id;
    private final PlaceType type;
    private final String title;

    private Place() {
        this(0, PlaceType.AUDITORY, "");
    }

    public Place(int id, int type, String title) {
        this(id, PlaceType.values()[type], title);
    }

    public Place(String id, PlaceType type, String title) {
        this(Integer.parseInt(id), type, title);
    }

    private Place(int id, PlaceType type, String title) {
        this.id = id;
        this.type = type;
        this.title = title;
    }

    public static Place instance() {
        return new Place();
    }

    public int getId() {
        return this.id;
    }

    public PlaceType getType() {
        return this.type;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public TableColumn[] getColumns() {
        return new TableColumn[]{
                COLUMN_NAME_ID,
                COLUMN_NAME_PLACE_ID,
                COLUMN_NAME_TYPE,
                COLUMN_NAME_TITLE
        };
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_PLACE_ID.getName(), this.getId());
        values.put(COLUMN_NAME_TYPE.getName(), this.getType().ordinal());
        values.put(COLUMN_NAME_TITLE.getName(), this.getTitle());
        return values;
    }

    @Override
    public String getTableName() {
        return "place";
    }

    @Override
    protected String[] getIndexes() {
        return new String[]{
                "UNIQUE (" + COLUMN_NAME_PLACE_ID.getName() + ", " + COLUMN_NAME_TYPE.getName() + ") ON CONFLICT REPLACE"
        };
    }
}
