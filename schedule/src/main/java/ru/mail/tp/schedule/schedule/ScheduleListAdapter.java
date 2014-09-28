package ru.mail.tp.schedule.schedule;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import ru.mail.tp.schedule.R;
import ru.mail.tp.schedule.schedule.entities.ScheduleItem;

public class ScheduleListAdapter extends ArrayAdapter<ScheduleItem> {
    private final Activity context;
    private final ArrayList<ScheduleItem> schedule;

    public ScheduleListAdapter(Activity context, ArrayList<ScheduleItem> schedule) {
        super(context, R.layout.row_schedule, schedule);
        this.context = context;
        this.schedule = schedule;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = this.context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_schedule, parent, false);
            holder = new ViewHolder();
            holder.init(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fill(schedule.get(position));

        if (position > 0) {
            if (schedule.get(position - 1).getDayStart() == schedule.get(position).getDayStart()) {
                holder.hideDateBar();
            } else {
                holder.showDateBar();
            }
        } else {
            holder.showDateBar();
        }

        if (schedule.get(position).isToday()) {
            holder.showTodayTitle();
        } else {
            holder.hideTodayTitle();
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView title, subtitle, time, date;
        private TextView today;
        private TableRow dateRow;

        public void init(View rowView) {
            this.time = (TextView) rowView.findViewById(R.id.timeTextView);
            this.title = (TextView) rowView.findViewById(R.id.titleTextView);
            this.subtitle = (TextView) rowView.findViewById(R.id.subtitleTextView);
            this.date = (TextView) rowView.findViewById(R.id.dateTextView);
            this.dateRow = (TableRow) rowView.findViewById(R.id.dateRow);
            this.today = (TextView) rowView.findViewById(R.id.todayTextView);
        }

        public void fill(ScheduleItem item) {
            this.time.setText(item.getTimeInterval());
            this.title.setText(item.getTitle());
            this.subtitle.setText(item.getSubtitle());
            this.date.setText(item.getDate());
        }

        public void hideDateBar() {
            this.dateRow.setVisibility(View.GONE);
        }

        public void showDateBar() {
            this.dateRow.setVisibility(View.VISIBLE);
        }

        public void hideTodayTitle() {
            this.today.setVisibility(View.GONE);
        }

        public void showTodayTitle() {
            this.today.setVisibility(View.VISIBLE);
        }
    }
}
