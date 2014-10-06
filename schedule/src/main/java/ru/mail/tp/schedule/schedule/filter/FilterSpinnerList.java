package ru.mail.tp.schedule.schedule.filter;

import java.util.ArrayList;

/**
 * author: grigory51
 * date: 06/10/14
 */
public class FilterSpinnerList extends ArrayList<FilterSpinner> {
    public static FilterSpinnerList createFromArrayList(ArrayList<IFilterSpinner> list) {
        FilterSpinnerList result = new FilterSpinnerList();
        for(IFilterSpinner item:list) {
            result.add(new FilterSpinner(item));
        }
        return result;
    }
}
