package ru.mail.tp.schedule.schedule.filter;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ru.mail.tp.schedule.schedule.entities.Discipline;
import ru.mail.tp.schedule.schedule.entities.LessonType;
import ru.mail.tp.schedule.schedule.entities.Subgroup;

public class FilterSpinnerItemsContainer implements Serializable {
    private ArrayList<Subgroup> subgroupItems;
    private ArrayList<Discipline> disciplineItems;
    private ArrayList<LessonType> lessonTypeItems;

    private int subgroupPosition, disciplinePosition, lessonTypePosition;
    private boolean showPassed;

    public FilterSpinnerItemsContainer(ArrayList<Subgroup> subgroupItems, ArrayList<Discipline> disciplineItems, ArrayList<LessonType> lessonTypeItems) throws JSONException {
        Comparator<FilterSpinner> comparator = new Comparator<FilterSpinner>() {
            public int compare(FilterSpinner a, FilterSpinner b) {
                if (a.getId() == 0) {
                    return -1;
                }
                if (b.getId() == 0) {
                    return 1;
                }
                return a.getTitle().compareTo(b.getTitle());
            }
        };

        this.subgroupItems = subgroupItems;
        this.disciplineItems = disciplineItems;
        this.lessonTypeItems = lessonTypeItems;

        this.subgroupItems.add(new Subgroup(0, "Все"));
        this.disciplineItems.add(new Discipline(0, "Все"));
        this.lessonTypeItems.add(new LessonType(0, "Все"));

        Collections.sort(this.subgroupItems, comparator);
        Collections.sort(this.disciplineItems, comparator);
        Collections.sort(this.lessonTypeItems, comparator);

        this.subgroupPosition = this.disciplinePosition = this.lessonTypePosition = 0;
        this.showPassed = false;
    }

    public ArrayList<Subgroup> getSubgroupItems() {
        return this.subgroupItems;
    }

    public ArrayList<Discipline> getDisciplineItems() {
        return this.disciplineItems;
    }

    public ArrayList<LessonType> getLessonTypeItems() {
        return this.lessonTypeItems;
    }

    public int getSubgroupPosition() {
        return subgroupPosition;
    }

    public void setSubgroupPosition(int subgroupPosition) {
        this.subgroupPosition = subgroupPosition;
    }

    public int getDisciplinePosition() {
        return disciplinePosition;
    }

    public void setDisciplinePosition(int disciplinePosition) {
        this.disciplinePosition = disciplinePosition;
    }

    public boolean isShowPassed() {
        return this.showPassed;
    }

    public int getLessonTypePosition() {
        return this.lessonTypePosition;
    }

    public void setLessonTypePosition(int lessonTypePosition) {
        this.lessonTypePosition = lessonTypePosition;
    }
}
