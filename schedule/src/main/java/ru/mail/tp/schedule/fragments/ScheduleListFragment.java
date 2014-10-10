package ru.mail.tp.schedule.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ru.mail.tp.schedule.R;
import ru.mail.tp.schedule.schedule.ScheduleFilter;

import ru.mail.tp.schedule.schedule.ScheduleListAdapter;
import ru.mail.tp.schedule.schedule.db.DBHelper;
import ru.mail.tp.schedule.schedule.db.entities.ScheduleItem;

public class ScheduleListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private OnScheduleItemClick onScheduleItemClickListener = null;
    private DBHelper dbHelper;
    private ArrayList<ScheduleItem> scheduleItems;
    private ListView scheduleListView;

    private ScheduleFilter filter;

    public ScheduleListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dbHelper = new DBHelper(getActivity());
        if (savedInstanceState == null) {
            this.filter = new ScheduleFilter();
            this.scheduleItems = dbHelper.getScheduleItems(this.filter);
        } else {
            this.filter = (ScheduleFilter) savedInstanceState.get("scheduleFilter");
            this.scheduleItems = (ArrayList<ScheduleItem>) savedInstanceState.get("scheduleItems");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("scheduleFilter", this.filter);
        outState.putSerializable("scheduleItems", this.scheduleItems);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_list, container, false);
        this.scheduleListView = (ListView) view.findViewById(R.id.f_schedule_list__scheduleListView);

        this.scheduleListView.setAdapter(new ScheduleListAdapter(getActivity(), this.scheduleItems));
        this.scheduleListView.setOnItemClickListener(this);

        return view;
    }

    public void setScheduleFilter(ScheduleFilter filter) {
        if (filter != null) {
            this.filter = filter;
            this.scheduleItems = this.dbHelper.getScheduleItems(filter);
            this.scheduleListView.setAdapter(new ScheduleListAdapter(this.getActivity(), this.scheduleItems));
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