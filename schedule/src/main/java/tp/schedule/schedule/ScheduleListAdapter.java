package tp.schedule.schedule;

import java.util.Calendar;
import java.util.TimeZone;

import ru.mail.tp.schedule.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ScheduleListAdapter extends ArrayAdapter<ScheduleItem> {
    private final Activity context;
    private final ScheduleItem[] schedule;
    private static boolean[] withDate;

    public ScheduleListAdapter(Activity context, ScheduleItem[] schedule) {
        super(context, R.layout.row_schedule, schedule);
        this.context = context;
        this.schedule = schedule;
        withDate = new boolean[schedule.length];
        initWithDate();
    }

    private static class ViewHolder {
        public TextView discipline, type, time, date, today;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;
        LayoutInflater inflater = context.getLayoutInflater();

        rowView = inflater.inflate(R.layout.row_schedule, null, true);

        holder = new ViewHolder();
        initHolder(holder, rowView);
        rowView.setTag(holder);

        fillHolder(holder, schedule[position]);
        initDate(holder, rowView, schedule[position]);

        return rowView;
    }

    private void initDate(ViewHolder holder, View rowView, ScheduleItem scheduleCell) {
        holder.date = (TextView) rowView.findViewById(R.id.dateTextView);
        holder.date.setText(getDate(scheduleCell.getWeekDay(), scheduleCell.getDate()));
        if (isToday(scheduleCell.getTimeStart())) {
            holder.today = (TextView) rowView.findViewById(R.id.todayTextView);
            holder.today.setText("Сегодня");
        }
    }

    private void initHolder(ViewHolder holder, View rowView) {
        holder.time = (TextView) rowView.findViewById(R.id.timeTextView);
        holder.discipline = (TextView) rowView.findViewById(R.id.disciplineTextView);
        holder.type = (TextView) rowView.findViewById(R.id.typeTextView);
    }

    private void fillHolder(ViewHolder holder, ScheduleItem scheduleItem) {
        holder.time.setText(scheduleItem.getTime());
        holder.discipline.setText(scheduleItem.getDiscipline());
        holder.type.setText(getType(scheduleItem.getType(), scheduleItem.getAuditory()));
    }

    private String getType(String type, String auditory) {
        StringBuilder sb = new StringBuilder();
        sb.append(type);
        sb.append(", ");
        if (!auditory.equals("МЗДК")) {
            sb.append("ауд. ");
        }
        sb.append(auditory);
        return sb.toString();
    }

    private String getDate(String weekDay, String date) {
        return weekDay + ", " + date;
    }

    private boolean isSameDate(Calendar lastCalendar, long time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
        calendar.setTimeInMillis(time);
        return (calendar.get(Calendar.DAY_OF_YEAR) == lastCalendar.get(Calendar.DAY_OF_YEAR)) &&
                (calendar.get(Calendar.YEAR) == lastCalendar.get(Calendar.YEAR));
    }

    private boolean isToday(long time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
        calendar.setTimeInMillis(System.currentTimeMillis());
        return isSameDate(calendar, time);
    }

    private void initWithDate() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
        calendar.setTimeInMillis(0);
        for (int count = 0; count < schedule.length; count++) {
            withDate[count] = !isSameDate(calendar, schedule[count].getTimeStart());
            calendar.setTimeInMillis(schedule[count].getTimeStart());
        }
    }
}
