package ru.mail.tp.schedule.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ru.mail.tp.schedule.R;
import ru.mail.tp.schedule.schedule.ScheduleBuilder;
import ru.mail.tp.schedule.schedule.ScheduleListAdapter;
import ru.mail.tp.schedule.schedule.db.DBHelper;
import ru.mail.tp.schedule.schedule.filter.ScheduleFilter;

public class ScheduleListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private OnScheduleItemClick onScheduleItemClickListener = null;

    private ListView scheduleListView;
    private ScheduleBuilder scheduleBuilder;
    private ScheduleFilter filter = new ScheduleFilter();

    public ScheduleListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            DBHelper dbHelper = new DBHelper(getActivity());
            this.scheduleBuilder = new ScheduleBuilder(dbHelper.getScheduleItems());
        } else {
            this.scheduleBuilder = (ScheduleBuilder) savedInstanceState.get("scheduleBuilder");
            this.filter = (ScheduleFilter) savedInstanceState.get("scheduleFilter");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("scheduleFilter", this.filter);
        outState.putSerializable("scheduleBuilder", this.scheduleBuilder);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_list, container, false);
        this.scheduleListView = (ListView) view.findViewById(R.id.f_schedule_list__scheduleListView);

        if (this.scheduleBuilder != null) {
            this.scheduleListView.setAdapter(new ScheduleListAdapter(this.getActivity(), this.scheduleBuilder.getScheduleItems(this.filter)));
            this.scheduleListView.setOnItemClickListener(this);
        }

        return view;
    }

    public void setScheduleFilter(ScheduleFilter filter) {
        if (filter != null && this.scheduleBuilder != null) {
            this.filter = filter;
            this.scheduleListView.setAdapter(new ScheduleListAdapter(this.getActivity(), this.scheduleBuilder.getScheduleItems(filter)));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.onScheduleItemClickListener = (OnScheduleItemClick) activity;
        } catch (ClassCastException ignore) {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (this.onScheduleItemClickListener != null) {
            ScheduleListAdapter adapter = (ScheduleListAdapter) adapterView.getAdapter();
            this.onScheduleItemClickListener.onScheduleItemClick(adapter.getItem(i));
        }
    }
}