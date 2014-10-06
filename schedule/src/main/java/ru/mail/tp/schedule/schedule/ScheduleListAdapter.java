package ru.mail.tp.schedule.schedule;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import ru.mail.tp.schedule.R;
import ru.mail.tp.schedule.schedule.entities.ScheduleItem;
import ru.mail.tp.schedule.schedule.entities.Type;

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
        ScheduleItem currentScheduleItem = schedule.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_schedule, parent, false);
            holder = new ViewHolder();
            holder.init(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fill(currentScheduleItem);

        if (currentScheduleItem.getLocation().equals("")) {
            holder.hideLocation();
        } else {
            holder.showLocation();
        }

        if (true || currentScheduleItem.getSubtitle().equals("")) {
            holder.hideSubtitle();
        } else {
            holder.showSubtitle();
        }

        if (currentScheduleItem.getType() == Type.EVENT) {
            holder.setAsEvent();
        } else {
            holder.setAsNotEvent();
        }

        if (position > 0) {
            if (schedule.get(position - 1).getDayStart() == currentScheduleItem.getDayStart()) {
                holder.hideDateBar();
            } else {
                holder.showDateBar();
            }
        } else {
            holder.showDateBar();
        }

        if (currentScheduleItem.isToday()) {
            holder.showTodayTitle();
        } else {
            holder.hideTodayTitle();
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView timeStart, timeEnd, date;
        private TextView title, subtitle, locationTitle;
        private TextView today;
        private TableRow dateRow;
        private RelativeLayout tableRowScheduleContent;
        private LinearLayout locationTitleContainer;

        public void init(View rowView) {
            this.tableRowScheduleContent = (RelativeLayout) rowView.findViewById(R.id.tableRowSchedule);
            this.timeStart = (TextView) rowView.findViewById(R.id.timeStartTextView);
            this.timeEnd = (TextView) rowView.findViewById(R.id.timeEndTextView);

            this.title = (TextView) rowView.findViewById(R.id.titleTextView);
            this.subtitle = (TextView) rowView.findViewById(R.id.subtitleTextView);
            this.locationTitle = (TextView) rowView.findViewById(R.id.locationTextView);

            this.date = (TextView) rowView.findViewById(R.id.dateTextView);
            this.dateRow = (TableRow) rowView.findViewById(R.id.dateRow);
            this.today = (TextView) rowView.findViewById(R.id.todayTextView);

            this.locationTitleContainer = (LinearLayout) rowView.findViewById(R.id.locationTitleContainer);
        }

        public void fill(ScheduleItem item) {
            this.timeStart.setText(item.getFormatTimeStart("HH:mm"));
            this.timeEnd.setText(item.getFormatTimeEnd("HH:mm"));
            this.title.setText(item.getShortTitle());
            this.subtitle.setText(item.getSubtitle());
            this.locationTitle.setText(item.getLocation());
            this.date.setText(item.getDate());
        }

        public void setAsEvent() {
            this.tableRowScheduleContent.setBackgroundColor(0xFFFCF8E3);
        }

        public void setAsNotEvent() {
            this.tableRowScheduleContent.setBackgroundColor(0x00FFFFFF);
        }

        public void hideDateBar() {
            this.dateRow.setVisibility(View.GONE);
        }

        public void showDateBar() {
            this.dateRow.setVisibility(View.VISIBLE);
        }

        public void hideSubtitle() {
            this.subtitle.setVisibility(View.GONE);
        }

        public void showSubtitle() {
            this.subtitle.setVisibility(View.VISIBLE);
        }

        public void hideTodayTitle() {
            this.today.setVisibility(View.GONE);
        }

        public void showTodayTitle() {
            this.today.setVisibility(View.VISIBLE);
        }

        public void hideLocation() {
            this.locationTitleContainer.setVisibility(View.GONE);
        }

        public void showLocation() {
            this.locationTitleContainer.setVisibility(View.VISIBLE);
        }
    }
}
