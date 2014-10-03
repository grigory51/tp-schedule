package ru.mail.tp.schedule.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import ru.mail.tp.schedule.R;
import ru.mail.tp.schedule.schedule.ScheduleBuilder;
import ru.mail.tp.schedule.schedule.ScheduleListAdapter;
import ru.mail.tp.schedule.schedule.filter.ScheduleFilter;

public class ScheduleListFragment extends Fragment implements AdapterView.OnItemClickListener {
    ListView scheduleListView;
    ScheduleBuilder scheduleBuilder;
    ScheduleFilter filter = new ScheduleFilter();

    public ScheduleListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = this.getArguments();
        if (arguments != null) {
            this.scheduleBuilder = (ScheduleBuilder) arguments.getSerializable("scheduleBuilder");
        }

        if (savedInstanceState != null) {
            this.filter = (ScheduleFilter) savedInstanceState.get("scheduleFilter");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("scheduleFilter", this.filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_list, container, false);
        this.scheduleListView = (ListView) view.findViewById(R.id.scheduleListView);

        if (this.scheduleBuilder != null) {
            this.scheduleListView.setAdapter(new ScheduleListAdapter(this.getActivity(), this.scheduleBuilder.getScheduleItems(this.filter)));
         //   this.scheduleListView.setOnItemClickListener(this);
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this.getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
    }
}
