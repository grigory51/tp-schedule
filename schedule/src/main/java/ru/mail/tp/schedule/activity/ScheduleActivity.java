package ru.mail.tp.schedule.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import ru.mail.tp.schedule.R;
import ru.mail.tp.schedule.schedule.FilterArrayAdapter;
import ru.mail.tp.schedule.schedule.FilterSpinnerItem;
import ru.mail.tp.schedule.schedule.FilterSpinnerItemsContainer;
import ru.mail.tp.schedule.schedule.OnFilterChangeListener;
import ru.mail.tp.schedule.schedule.ScheduleBuilder;
import ru.mail.tp.schedule.schedule.ScheduleCache;
import ru.mail.tp.schedule.schedule.ScheduleFilter;
import ru.mail.tp.schedule.schedule.ScheduleListAdapter;
import ru.mail.tp.schedule.schedule.entities.ScheduleItem;
import ru.mail.tp.schedule.tasks.ITaskResultReceiver;
import ru.mail.tp.schedule.tasks.TaskResult;
import ru.mail.tp.schedule.tasks.scheduleFetch.ScheduleFetchTaskResult;
import ru.mail.tp.schedule.utils.MoscowCalendar;
import ru.mail.tp.schedule.utils.MoscowSimpleDateFormat;

public class ScheduleActivity extends SherlockActivity implements OnClickListener, ITaskResultReceiver {
    private static final String CACHE_NAME = "ScheduleCache.txt";
    private ScheduleCache cache = null;
    private FilterSpinnerItemsContainer filterSpinnerItemsContainer = null;
    private ScheduleBuilder scheduleBuilder = null;
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
        TextView lastUpdateTextView = (TextView) findViewById(R.id.lastUpdateTextView);

        if (fetchTaskResult.getStatus() == ScheduleFetchTaskResult.STATUS_OK) {
            this.scheduleBuilder = fetchTaskResult.getScheduleBuilder();
            this.filterSpinnerItemsContainer = fetchTaskResult.getFilterSpinnerItemsContainer();
            lastUpdateTextView.setText("Последнее обновление\n" + new MoscowSimpleDateFormat("dd.MM.yyyy в HH:mm").format(MoscowCalendar.getInstance().getTime()));
            this.saveCache(new ScheduleCache(this.scheduleBuilder, this.filterSpinnerItemsContainer));
        } else {
            if (fetchTaskResult.getStatus() == ScheduleFetchTaskResult.STATUS_NETWORK_ERROR) {
                Toast.makeText(this, "Ошибка получения данных", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Ошибка обработки данных", Toast.LENGTH_LONG).show();
            }
            if (getCache() != null) {
                this.scheduleBuilder = getCache().getScheduleBuilder();
                this.filterSpinnerItemsContainer = getCache().getFilterContainer();
                lastUpdateTextView.setText("Последнее обновление\n" + new MoscowSimpleDateFormat("dd.MM.yyyy в HH:mm").format(getCache().getLastModified()));
            }
        }

        if (this.scheduleBuilder != null && this.filterSpinnerItemsContainer != null) {
            ScheduleListAdapter scheduleAdapter = new ScheduleListAdapter(this, scheduleBuilder.getScheduleItems());
            scheduleList.setAdapter(scheduleAdapter);
            this.initFilters();
        }
    }

    private void initFilters() {
        FilterArrayAdapter subgroupListAdapter = new FilterArrayAdapter(this);
        FilterArrayAdapter disciplineListAdapter = new FilterArrayAdapter(this);
        FilterArrayAdapter typeListAdapter = new FilterArrayAdapter(this);

        final Spinner subgroupsSpinner = (Spinner) findViewById(R.id.subgroupsSpinner);
        final Spinner disciplinesSpinner = (Spinner) findViewById(R.id.disciplinesSpinner);
        final Spinner typesSpinner = (Spinner) findViewById(R.id.typesSpinner);
        final CheckBox showPastCheckBox = (CheckBox) findViewById(R.id.showPastCheckBox);

        subgroupsSpinner.setAdapter(subgroupListAdapter);
        disciplinesSpinner.setAdapter(disciplineListAdapter);
        typesSpinner.setAdapter(typeListAdapter);

        subgroupListAdapter.addAll(this.filterSpinnerItemsContainer.getSubgroupItems());
        disciplineListAdapter.addAll(this.filterSpinnerItemsContainer.getDisciplineItems());
        typeListAdapter.addAll(this.filterSpinnerItemsContainer.getTypeItems());

        subgroupsSpinner.setSelection(0, false);
        disciplinesSpinner.setSelection(0, false);
        typesSpinner.setSelection(0, false);

        final OnFilterChangeListener filterChangeListener = new OnFilterChangeListener() {
            @Override
            public void onFilterChange() {
                FilterSpinnerItem subgroupItem = (FilterSpinnerItem) subgroupsSpinner.getSelectedItem();
                FilterSpinnerItem disciplineItem = (FilterSpinnerItem) disciplinesSpinner.getSelectedItem();
                FilterSpinnerItem typeItem = (FilterSpinnerItem) typesSpinner.getSelectedItem();
                boolean showPass = showPastCheckBox.isChecked();

                ArrayList<ScheduleItem> newScheduleList = scheduleBuilder.getScheduleItems(new ScheduleFilter(subgroupItem.getId(), disciplineItem.getId(), typeItem.getId(), showPass));
                ScheduleListAdapter newAdapter = new ScheduleListAdapter(context, newScheduleList);

                scheduleList.setAdapter(newAdapter);
            }
        };

        OnItemSelectedListener selectListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                filterChangeListener.onFilterChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        };

        subgroupsSpinner.setOnItemSelectedListener(selectListener);
        disciplinesSpinner.setOnItemSelectedListener(selectListener);
        typesSpinner.setOnItemSelectedListener(selectListener);

        showPastCheckBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                filterChangeListener.onFilterChange();
            }
        });
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

    private void saveCache(ScheduleCache cache) {
        FileOutputStream fos;
        ObjectOutputStream oos;
        try {
            fos = openFileOutput(CACHE_NAME, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(cache);
            oos.flush();
            oos.close();
            this.cache = cache;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ScheduleCache getCache() {
        if (this.cache == null) {
            FileInputStream fis;
            ObjectInputStream ois;
            try {
                fis = openFileInput(CACHE_NAME);
                ois = new ObjectInputStream(fis);

                ScheduleCache cache = (ScheduleCache) ois.readObject();
                ois.close();

                this.cache = cache;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return this.cache;
    }
}
