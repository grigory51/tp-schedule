package ru.mail.tp.schedule.schedule.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ru.mail.tp.schedule.schedule.entities.Discipline;
import ru.mail.tp.schedule.schedule.entities.LessonType;
import ru.mail.tp.schedule.schedule.entities.Subgroup;

public class FilterSpinnerItemsContainer implements Serializable {
    private FilterSpinnerList subgroupItems;
    private FilterSpinnerList disciplineItems;
    private FilterSpinnerList lessonTypeItems;

    private int subgroupPosition, disciplinePosition, lessonTypePosition;
    private boolean showPassed;

    public FilterSpinnerItemsContainer(ArrayList<IFilterSpinner> subgroupItems, ArrayList<IFilterSpinner> disciplineItems, ArrayList<IFilterSpinner> lessonTypeItems) {
        Comparator<FilterSpinner> comparator = new Comparator<FilterSpinner>() {
            public int compare(FilterSpinner a, FilterSpinner b) {
                if (a.getId() == 0) {
                    return -1;
                }
                if (b.getId() == 0) {
                    return 1;
                }
                return a.toString().compareTo(b.toString());
            }
        };

        this.subgroupItems = FilterSpinnerList.createFromArrayList(subgroupItems);
        this.disciplineItems = FilterSpinnerList.createFromArrayList(disciplineItems);
        this.lessonTypeItems = FilterSpinnerList.createFromArrayList(lessonTypeItems);

        this.subgroupItems.add(new FilterSpinner(new Subgroup(0, "Все")));
        this.disciplineItems.add(new FilterSpinner(new Discipline(0, "Все")));
        this.lessonTypeItems.add(new FilterSpinner(new LessonType(0, "Все")));

        Collections.sort(this.subgroupItems, comparator);
        Collections.sort(this.disciplineItems, comparator);
        Collections.sort(this.lessonTypeItems, comparator);

        this.subgroupPosition = this.disciplinePosition = this.lessonTypePosition = 0;
        this.showPassed = false;
    }

    public ArrayList<FilterSpinner> getSubgroupItems() {
        return this.subgroupItems;
    }

    public ArrayList<FilterSpinner> getDisciplineItems() {
        return this.disciplineItems;
    }

    public ArrayList<FilterSpinner> getLessonTypeItems() {
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
