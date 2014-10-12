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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import ru.mail.tp.schedule.R;
import ru.mail.tp.schedule.schedule.db.entities.ScheduleItem;
import ru.mail.tp.schedule.utils.MoscowCalendar;

public class ScheduleDetailFragment extends Fragment implements OnClickListener {
    private TextView title, subtitle, date, time;
    private ImageButton addToCalendarButton;
    private ScheduleItem scheduleItem = null;
    private final Calendar calendar = MoscowCalendar.getInstance();
    private RelativeLayout layout;

    public ScheduleDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = this.getArguments();
        if (arguments != null) {
            this.scheduleItem = (ScheduleItem) arguments.getSerializable("scheduleItem");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_detail, container, false);

        layout = (RelativeLayout) view.findViewById(R.id.f_schedule_detail);
        title = (TextView) view.findViewById(R.id.f_schedule_detail__titleTextView);
        subtitle = (TextView) view.findViewById(R.id.f_schedule_detail__subtitleTextView);
        date = (TextView) view.findViewById(R.id.f_schedule_detail__dateTextView);
        time = (TextView) view.findViewById(R.id.f_schedule_detail__timeTextView);
        addToCalendarButton = (ImageButton) view.findViewById(R.id.f_schedule_detail__addToCalendarButton);

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        layout.setVisibility(View.INVISIBLE);
        this.showItem(this.scheduleItem);

        return view;
    }

    public void showItem(ScheduleItem item) {
        if (item != null) {
            layout.setVisibility(View.VISIBLE);
            this.scheduleItem = item;

            title.setText(item.getTitle());
            if (!item.getSubtitle().equals("")) {
                subtitle.setText(item.getSubtitle());
                subtitle.setVisibility(View.VISIBLE);
            } else {
                subtitle.setVisibility(View.GONE);
            }

            calendar.setTimeInMillis(item.getDayStart());
            date.setText((calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + calendar.get(Calendar.DAY_OF_MONTH) + "\n" + MoscowCalendar.getMonthTitle(calendar.get(Calendar.MONTH)));
            time.setText(item.getFormatTimeStart("HH:mm") + "\n" + item.getFormatTimeEnd("HH:mm"));

            if (Build.VERSION.SDK_INT >= 14) {
                addToCalendarButton.setOnClickListener(this);
            } else {
                addToCalendarButton.setVisibility(View.GONE);
            }
        }
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
