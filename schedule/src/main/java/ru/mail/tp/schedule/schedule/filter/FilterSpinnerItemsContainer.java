package ru.mail.tp.schedule.schedule.filter;

import java.io.Serializable;

import ru.mail.tp.schedule.schedule.ScheduleFilter;
import ru.mail.tp.schedule.schedule.db.DBHelper;
import ru.mail.tp.schedule.schedule.db.entities.Discipline;
import ru.mail.tp.schedule.schedule.db.entities.LessonType;
import ru.mail.tp.schedule.schedule.db.entities.Subgroup;

public class FilterSpinnerItemsContainer implements Serializable {
    private final FilterSpinnerList subgroupItems;
    private final FilterSpinnerList disciplineItems;
    private final FilterSpinnerList lessonTypeItems;

    private final FilterState filterState;

    public FilterSpinnerItemsContainer(DBHelper db, FilterState filterState) {
        this.subgroupItems = new FilterSpinnerList();
        this.disciplineItems = new FilterSpinnerList();
        this.lessonTypeItems = new FilterSpinnerList();

        this.subgroupItems.add(new FilterSpinner(new Subgroup(0, "Все")));
        this.disciplineItems.add(new FilterSpinner(new Discipline("Все")));
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

        if (filterState != null) {
            this.filterState = filterState;
        } else {
            this.filterState = new FilterState();
        }
    }

    public ScheduleFilter getScheduleFilter() {
        int subgroupPosition = this.getFilterState().getSubgroupPosition();
        int disciplinePosition = this.getFilterState().getDisciplinePosition();
        int typePosition = this.getFilterState().getLessonTypePosition();

        FilterSpinner subgroupItem = subgroupItems.get(subgroupPosition < subgroupItems.size() ? subgroupPosition : 0);
        FilterSpinner disciplineItem = disciplineItems.get(disciplinePosition < disciplineItems.size() ? disciplinePosition : 0);
        FilterSpinner typeItem = lessonTypeItems.get(typePosition < lessonTypeItems.size() ? typePosition : 0);

        return new ScheduleFilter(subgroupItem.getId(), disciplineItem.getId(), typeItem.getId(), this.getFilterState().isShowPassed());
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

    public FilterState getFilterState() {
        return this.filterState;
    }
}
