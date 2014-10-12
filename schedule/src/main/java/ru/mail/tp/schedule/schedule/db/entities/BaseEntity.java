package ru.mail.tp.schedule.schedule.db.entities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * author: grigory51
 * date: 07/10/14
 */
public abstract class BaseEntity implements BaseColumns, Serializable {
    public static final TableColumn COLUMN_NAME_ID = new TableColumn(_ID, "INTEGER PRIMARY KEY");

    protected abstract TableColumn[] getColumns();

    protected abstract ContentValues getContentValues();

    protected abstract String getTableName();

    String[] getIndexes() {
        return new String[]{};
    }

    public long insert(SQLiteDatabase db) {
        return db.insert(this.getTableName(), null, this.getContentValues());
    }

    public void truncate(SQLiteDatabase db) {
        db.execSQL(this.dropTableSQL());
        db.execSQL(this.createTableSQL());
    }

    public String createTableSQL() {
        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(this.getTableName());
        sb.append(" (");
        for (int i = 0; i < this.getColumns().length; i++) {
            sb.append(this.getColumns()[i].toString());
            if (i + 1 < (this.getColumns().length + this.getIndexes().length)) {
                sb.append(", ");
            }
        }

        for (int i = 0; i < this.getIndexes().length; i++) {
            sb.append(this.getIndexes()[i]);
            if (i + 1 < this.getIndexes().length) {
                sb.append(", ");
            }
        }
        sb.append(") ");

        return sb.toString();
    }

    public String dropTableSQL() {
        return "DROP TABLE IF EXISTS " + this.getTableName();
    }

    public static class TableColumn {
        private final String name;
        private final String type;

        public TableColumn(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return this.name;
        }

        public String getType() {
            return this.type;
        }

        @Override
        public String toString() {
            return this.getName() + " " + this.getType();
        }
    }
}
