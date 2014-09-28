package ru.mail.tp.schedule.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.Calendar;

import ru.mail.tp.schedule.R;
import ru.mail.tp.schedule.schedule.FilterArrayAdapter;
import ru.mail.tp.schedule.schedule.FilterSpinnerItem;
import ru.mail.tp.schedule.schedule.FilterSpinnerItemsContainer;
import ru.mail.tp.schedule.schedule.ScheduleBuilder;
import ru.mail.tp.schedule.schedule.ScheduleFilter;
import ru.mail.tp.schedule.schedule.ScheduleListAdapter;
import ru.mail.tp.schedule.schedule.entities.ScheduleItem;
import ru.mail.tp.schedule.tasks.ITaskResultReceiver;
import ru.mail.tp.schedule.tasks.TaskResult;
import ru.mail.tp.schedule.tasks.scheduleFetch.ScheduleFetchTaskResult;

public class ScheduleActivity extends SherlockActivity implements OnClickListener, ITaskResultReceiver {
    private FilterSpinnerItemsContainer filterSpinnerItemsContainer;
    private ScheduleBuilder scheduleBuilder;
    private SlidingMenu menu;
    private ListView scheduleList;
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        this.context = this;
        this.scheduleList = (ListView) findViewById(R.id.scheduleList);

        this.getSupportActionBar().setDisplayShowHomeEnabled(false);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        this.getSupportActionBar().setCustomView(R.layout.actionbar);

        this.menu = new SlidingMenu(this);
        this.menu.setMode(SlidingMenu.LEFT);
        this.menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        this.menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        this.menu.setMenu(R.layout.menu);
        this.menu.setShadowDrawable(R.drawable.shadow);
        this.menu.setShadowWidth(1);
        this.menu.setBehindWidth((int) (getResources().getDisplayMetrics().widthPixels * 0.7));

        ScheduleFetchTaskResult fetchTaskResult = (ScheduleFetchTaskResult) getIntent().getExtras().get(SplashActivity.TAG_FETCH_RESULT);

        if (fetchTaskResult.getStatus() == ScheduleFetchTaskResult.STATUS_OK) {
            this.scheduleBuilder = fetchTaskResult.getScheduleBuilder();
            this.filterSpinnerItemsContainer = fetchTaskResult.getFilterSpinnerItemsContainer();
            ScheduleListAdapter scheduleAdapter = new ScheduleListAdapter(this, scheduleBuilder.getScheduleItems());
            scheduleList.setAdapter(scheduleAdapter);

            this.initSpinners();
        } else {
            if (fetchTaskResult.getStatus() == ScheduleFetchTaskResult.STATUS_DATA_PROCESS_ERROR) {
                Toast.makeText(this, "Ошибка обработки данных", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Ошибка получения данных", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initSpinners() {
        if (this.filterSpinnerItemsContainer != null) {
            FilterArrayAdapter subgroupListAdapter = new FilterArrayAdapter(this);
            FilterArrayAdapter disciplineListAdapter = new FilterArrayAdapter(this);
            FilterArrayAdapter typeListAdapter = new FilterArrayAdapter(this);

            final Spinner subgroupsSpinner = (Spinner) findViewById(R.id.subgroupsSpinner);
            final Spinner disciplinesSpinner = (Spinner) findViewById(R.id.disciplinesSpinner);
            final Spinner typesSpinner = (Spinner) findViewById(R.id.typesSpinner);

            subgroupsSpinner.setAdapter(subgroupListAdapter);
            disciplinesSpinner.setAdapter(disciplineListAdapter);
            typesSpinner.setAdapter(typeListAdapter);

            subgroupListAdapter.addAll(this.filterSpinnerItemsContainer.getSubgroupItems());
            disciplineListAdapter.addAll(this.filterSpinnerItemsContainer.getDisciplineItems());
            typeListAdapter.addAll(this.filterSpinnerItemsContainer.getTypeItems());

            subgroupsSpinner.setSelection(0, false);
            disciplinesSpinner.setSelection(0, false);
            typesSpinner.setSelection(0, false);

            OnItemSelectedListener selectListener = new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    FilterSpinnerItem subgroupItem = (FilterSpinnerItem) subgroupsSpinner.getSelectedItem();
                    FilterSpinnerItem disciplineItem = (FilterSpinnerItem) disciplinesSpinner.getSelectedItem();
                    FilterSpinnerItem typeItem = (FilterSpinnerItem) typesSpinner.getSelectedItem();

                    ArrayList<ScheduleItem> newScheduleList = scheduleBuilder.getScheduleItems(new ScheduleFilter(subgroupItem.getId(), disciplineItem.getId(), typeItem.getId()));
                    ScheduleListAdapter newAdapter = new ScheduleListAdapter(context, newScheduleList);

                    scheduleList.setAdapter(newAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            };

            subgroupsSpinner.setOnItemSelectedListener(selectListener);
            disciplinesSpinner.setOnItemSelectedListener(selectListener);
            typesSpinner.setOnItemSelectedListener(selectListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateScheduleButton:
                break;
            case R.id.todayButton:
                break;
            case R.id.menuButton:
                this.menu.showMenu();
                break;
        }
    }

    @Override
    public void onPostExecute(TaskResult result) {

    }

    private void saveData(String data) {
        SharedPreferences sPref = getSharedPreferences("Data", MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putString("data", data);
        ed.commit();
    }

    private void scrollSchedule() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
    }

    private void loadFilter() {
        SharedPreferences sPref = getSharedPreferences("Data", MODE_PRIVATE);
        int subgroup = sPref.getInt("subgroup", -1);
        int discipline = sPref.getInt("title", -1);
        int type = sPref.getInt("type", -1);
        //scheduleFilter = new FilterSchedule(subgroup, discipline, type);
    }

    private void saveFilter() {
        SharedPreferences sPref = getSharedPreferences("Data", MODE_PRIVATE);
        Editor editor = sPref.edit();
        /*editor.putInt("subgroup", scheduleFilter.getSubgroup());
        editor.putInt("title", scheduleFilter.getDiscipline());
        editor.putInt("type", scheduleFilter.getType());*/
        editor.commit();
    }


}
