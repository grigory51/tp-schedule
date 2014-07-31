package tp.schedule.schedule;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * author: grigory51
 * date: 22.07.14
 */
public class FilterArrayAdapter extends ArrayAdapter<String> {
    public FilterArrayAdapter(Context context) {
        super(context, android.R.layout.simple_spinner_item);
        this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
