package ru.mail.tp.schedule.schedule.filter;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

/**
 * author: grigory51
 * date: 12/10/14
 */
public class FilterState implements Serializable {
    private static final String PREFS_NAME = "ru.mail.tp.schedule.FilterState";

    private int subgroupPosition, disciplinePosition, lessonTypePosition;
    private boolean showPassed;

    public FilterState() {
        this(0, 0, 0, false);
    }

    @SuppressWarnings("WeakerAccess")
    public FilterState(int subgroupPosition, int disciplinePosition, int lessonTypePosition, boolean showPassed) {
        this.subgroupPosition = subgroupPosition;
        this.disciplinePosition = disciplinePosition;
        this.lessonTypePosition = lessonTypePosition;
        this.showPassed = showPassed;
    }

    public static FilterState createFromSharedPreferences(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (preferences != null) {
            int subgroupPosition = preferences.getInt("subgroupPosition", 0);
            int disciplinePosition = preferences.getInt("disciplinePosition", 0);
            int lessonTypePosition = preferences.getInt("lessonTypePosition", 0);
            boolean showPassed = preferences.getBoolean("showPassed", false);

            return new FilterState(subgroupPosition, disciplinePosition, lessonTypePosition, showPassed);
        } else {
            return new FilterState();
        }
    }

    public void saveToSharedPreferences(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("subgroupPosition", this.getSubgroupPosition());
        editor.putInt("disciplinePosition", this.getDisciplinePosition());
        editor.putInt("lessonTypePosition", this.getLessonTypePosition());
        editor.putBoolean("showPassed", this.isShowPassed());

        editor.commit();
    }

    public int getSubgroupPosition() {
        return this.subgroupPosition;
    }

    public void setSubgroupPosition(int subgroupPosition) {
        this.subgroupPosition = subgroupPosition;
    }

    public int getDisciplinePosition() {
        return this.disciplinePosition;
    }

    public void setDisciplinePosition(int disciplinePosition) {
        this.disciplinePosition = disciplinePosition;
    }

    public int getLessonTypePosition() {
        return this.lessonTypePosition;
    }

    public void setLessonTypePosition(int lessonTypePosition) {
        this.lessonTypePosition = lessonTypePosition;
    }

    public boolean isShowPassed() {
        return this.showPassed;
    }

    public void setShowPassed(boolean showPassed) {
        this.showPassed = showPassed;
    }
}
