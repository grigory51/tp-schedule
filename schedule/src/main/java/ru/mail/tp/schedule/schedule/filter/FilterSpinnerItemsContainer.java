package ru.mail.tp.schedule.schedule.filter;

import java.io.Serializable;

import ru.mail.tp.schedule.schedule.db.DBHelper;
import ru.mail.tp.schedule.schedule.db.entities.Discipline;
import ru.mail.tp.schedule.schedule.db.entities.LessonType;
import ru.mail.tp.schedule.schedule.db.entities.Subgroup;

public class FilterSpinnerItemsContainer implements Serializable {
    private final FilterSpinnerList subgroupItems;
    private final FilterSpinnerList disciplineItems;
    private final FilterSpinnerList lessonTypeItems;

    private int subgroupPosition, disciplinePosition, lessonTypePosition;
    private boolean showPassed;

    public FilterSpinnerItemsContainer(DBHelper db) {
        this.subgroupItems = new FilterSpinnerList();
        this.disciplineItems = new FilterSpinnerList();
        this.lessonTypeItems = new FilterSpinnerList();

        this.subgroupItems.add(new FilterSpinner(new Subgroup(0, "Все")));
        this.disciplineItems.add(new FilterSpinner(new Discipline(0, "Все")));
        this.lessonTypeItems.add(new FilterSpinner(new LessonType(0, "Все")));

        for (Subgroup item : db.getSubgroups()) {
            this.subgroupItems.add(item);
        }
        for (Discipline item : db.getDisciplines()) {
            this.disciplineItems.add(item);
        }
        for (LessonType item : db.getLessonTypes()) {
            this.lessonTypeItems.add(item);
        }

        this.subgroupPosition = this.disciplinePosition = this.lessonTypePosition = 0;
        this.showPassed = false;
    }

    public FilterSpinnerList getSubgroupItems() {
        return this.subgroupItems;
    }

    public FilterSpinnerList getDisciplineItems() {
        return this.disciplineItems;
    }

    public FilterSpinnerList getLessonTypeItems() {
        return this.lessonTypeItems;
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

    public boolean getShowPassed() {
        return this.showPassed;
    }

    public void setShowPassed(boolean showPassed) {
        this.showPassed = showPassed;
    }
}
