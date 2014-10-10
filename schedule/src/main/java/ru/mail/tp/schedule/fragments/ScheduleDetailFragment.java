package ru.mail.tp.schedule.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;

import ru.mail.tp.schedule.R;
import ru.mail.tp.schedule.schedule.db.entities.ScheduleItem;
import ru.mail.tp.schedule.utils.MoscowCalendar;

public class ScheduleDetailFragment extends Fragment implements OnClickListener {
    private ScheduleItem scheduleItem = null;

    public ScheduleDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = this.getArguments();

        this.scheduleItem = (ScheduleItem) arguments.getSerializable("scheduleItem");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_detail, container, false);

        TextView title = (TextView) view.findViewById(R.id.f_schedule_detail__titleTextView);
        TextView subtitle = (TextView) view.findViewById(R.id.f_schedule_detail__subtitleTextView);
        TextView date = (TextView) view.findViewById(R.id.f_schedule_detail__dateTextView);
        TextView time = (TextView) view.findViewById(R.id.f_schedule_detail__timeTextView);
        ImageButton addToCalendarButton = (ImageButton) view.findViewById(R.id.f_schedule_detail__addToCalendarButton);

        title.setText(scheduleItem.getTitle());
        if (!scheduleItem.getSubtitle().equals("")) {
            subtitle.setText(scheduleItem.getSubtitle());
            subtitle.setVisibility(View.VISIBLE);
        } else {
            subtitle.setVisibility(View.GONE);
        }

        Calendar calendar = MoscowCalendar.getInstance();
        calendar.setTimeInMillis(scheduleItem.getDayStart());
        date.setText((calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + calendar.get(Calendar.DAY_OF_MONTH) + "\n" + MoscowCalendar.getMonthTitle(calendar.get(Calendar.MONTH)));

        time.setText(scheduleItem.getFormatTimeStart("HH:mm") + "\n" + scheduleItem.getFormatTimeEnd("HH:mm"));

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        if (Build.VERSION.SDK_INT >= 14) {
            addToCalendarButton.setOnClickListener(this);
        } else {
            addToCalendarButton.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        if (Build.VERSION.SDK_INT >= 14) {
            if (this.scheduleItem != null) {
                Intent calendarIntent = new Intent(Intent.ACTION_INSERT);

                calendarIntent.setData(CalendarContract.Events.CONTENT_URI);
                calendarIntent.putExtra(CalendarContract.Events.TITLE, this.scheduleItem.getTitle());
                calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Технопарк, " + this.scheduleItem.getPlace().getTitle());
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, this.scheduleItem.getTimeStart().getTime());
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, this.scheduleItem.getTimeEnd().getTime());

                startActivity(calendarIntent);
            }
        }
    }
}
