package ru.mail.tp.schedule.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import ru.mail.tp.schedule.R;
import ru.mail.tp.schedule.fragments.OnScheduleItemClick;
import ru.mail.tp.schedule.fragments.ScheduleDetailFragment;
import ru.mail.tp.schedule.fragments.ScheduleListFragment;
import ru.mail.tp.schedule.schedule.ScheduleBuilder;
import ru.mail.tp.schedule.schedule.ScheduleCache;
import ru.mail.tp.schedule.schedule.entities.ScheduleItem;
import ru.mail.tp.schedule.schedule.filter.FilterArrayAdapter;
import ru.mail.tp.schedule.schedule.filter.FilterSpinner;
import ru.mail.tp.schedule.schedule.filter.FilterSpinnerItemsContainer;
import ru.mail.tp.schedule.schedule.filter.OnFilterChangeListener;
import ru.mail.tp.schedule.schedule.filter.ScheduleFilter;
import ru.mail.tp.schedule.tasks.scheduleFetch.ScheduleFetchTaskResult;
import ru.mail.tp.schedule.utils.MoscowCalendar;
import ru.mail.tp.schedule.utils.MoscowSimpleDateFormat;

public class ScheduleActivity extends SherlockFragmentActivity implements OnClickListener, OnScheduleItemClick {
    private static final String CACHE_NAME = "ScheduleCache.txt";
    private FilterSpinnerItemsContainer filterSpinnerItemsContainer = null;
    private ScheduleCache cache = null;
    private ScheduleBuilder scheduleBuilder = null;
    private SlidingMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

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

        int menuWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.7);
        this.menu.setBehindWidth(menuWidth > 450 ? 450 : menuWidth);

        if (savedInstanceState == null) {
            ScheduleFetchTaskResult fetchTaskResult = (ScheduleFetchTaskResult) getIntent().getExtras().get(SplashActivity.TAG_FETCH_RESULT);

            if (fetchTaskResult.getStatus() == ScheduleFetchTaskResult.STATUS_OK) {
                this.scheduleBuilder = fetchTaskResult.getScheduleBuilder();
                this.filterSpinnerItemsContainer = fetchTaskResult.getFilterSpinnerItemsContainer();
                this.setLastUpdateDate(MoscowCalendar.getInstance().getTime());
                this.saveCache(new ScheduleCache(this.scheduleBuilder, filterSpinnerItemsContainer));
            } else {
                if (fetchTaskResult.getStatus() == ScheduleFetchTaskResult.STATUS_NETWORK_ERROR) {
                    Toast.makeText(this, "Ошибка получения данных", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Ошибка обработки данных", Toast.LENGTH_LONG).show();
                }
                if (getCache() != null) {
                    this.scheduleBuilder = getCache().getScheduleBuilder();
                    this.filterSpinnerItemsContainer = getCache().getFilterContainer();
                    this.setLastUpdateDate(getCache().getLastModified());
                }
            }

            if (this.scheduleBuilder != null) {
                Fragment scheduleListFragment;
                /*Fragment scheduleListFragment = getSupportFragmentManager().findFragmentById(R.id.scheduleListFragment);
                if (scheduleListFragment != null) {
                    View scheduleListFragmentView = scheduleListFragment.getView();
                    if (scheduleListFragmentView != null) {
                        ListView scheduleList = (ListView) scheduleListFragmentView.findViewById(R.id.scheduleListView);
                        scheduleList.setAdapter(new ScheduleListAdapter(this, scheduleBuilder.getScheduleItems()));
                    }
                } else {*/
                Bundle arguments = new Bundle();
                arguments.putSerializable("scheduleBuilder", this.scheduleBuilder);

                scheduleListFragment = new ScheduleListFragment();
                scheduleListFragment.setArguments(arguments);

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frameLayout, scheduleListFragment)
                        .commit();
                /*}*/
            }
        } else {
            this.filterSpinnerItemsContainer = (FilterSpinnerItemsContainer) savedInstanceState.getSerializable("filterSpinnerItemsContainer");
        }

        if (this.filterSpinnerItemsContainer != null) {
            this.initFilters();
        }
    }

    private void setLastUpdateDate(Date date) {
        TextView lastUpdateTextView = (TextView) findViewById(R.id.lastUpdateTextView);
        lastUpdateTextView.setText("Последнее обновление\n" + new MoscowSimpleDateFormat("dd.MM.yyyy в HH:mm").format(date));
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

        subgroupListAdapter.addAll(filterSpinnerItemsContainer.getSubgroupItems());
        disciplineListAdapter.addAll(filterSpinnerItemsContainer.getDisciplineItems());
        typeListAdapter.addAll(filterSpinnerItemsContainer.getLessonTypeItems());

        subgroupsSpinner.setSelection(filterSpinnerItemsContainer.getSubgroupPosition(), false);
        disciplinesSpinner.setSelection(filterSpinnerItemsContainer.getDisciplinePosition(), false);
        typesSpinner.setSelection(filterSpinnerItemsContainer.getLessonTypePosition(), false);

        final OnFilterChangeListener filterChangeListener = new OnFilterChangeListener() {
            @Override
            public void onFilterChange() {
                filterSpinnerItemsContainer.setSubgroupPosition(subgroupsSpinner.getSelectedItemPosition());
                filterSpinnerItemsContainer.setDisciplinePosition(disciplinesSpinner.getSelectedItemPosition());
                filterSpinnerItemsContainer.setLessonTypePosition(typesSpinner.getSelectedItemPosition());

                ScheduleListFragment scheduleListFragment = getScheduleListFragment();
                if (scheduleListFragment != null) {
                    scheduleListFragment.setScheduleFilter(getScheduleFilter());
                }
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

        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                filterChangeListener.onFilterChange();
            }
        };

        subgroupsSpinner.setOnItemSelectedListener(selectListener);
        disciplinesSpinner.setOnItemSelectedListener(selectListener);
        typesSpinner.setOnItemSelectedListener(selectListener);
        showPastCheckBox.setOnClickListener(clickListener);
    }

    private ScheduleFilter getScheduleFilter() {
        final Spinner subgroupsSpinner = (Spinner) findViewById(R.id.subgroupsSpinner);
        final Spinner disciplinesSpinner = (Spinner) findViewById(R.id.disciplinesSpinner);
        final Spinner typesSpinner = (Spinner) findViewById(R.id.typesSpinner);
        final CheckBox showPastCheckBox = (CheckBox) findViewById(R.id.showPastCheckBox);

        FilterSpinner subgroupItem = (FilterSpinner) subgroupsSpinner.getSelectedItem();
        FilterSpinner disciplineItem = (FilterSpinner) disciplinesSpinner.getSelectedItem();
        FilterSpinner typeItem = (FilterSpinner) typesSpinner.getSelectedItem();
        boolean showPass = showPastCheckBox.isChecked();

        return new ScheduleFilter(subgroupItem.getId(), disciplineItem.getId(), typeItem.getId(), showPass);
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

    private ScheduleListFragment getScheduleListFragment() {
        ScheduleListFragment fragment = null;
        try {
            fragment = (ScheduleListFragment) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        } catch (ClassCastException ignore) {
        }
        return fragment;
    }

    private ScheduleDetailFragment getScheduleDetailFragment() {
        ScheduleDetailFragment fragment = null;
        try {
            fragment = (ScheduleDetailFragment) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        } catch (ClassCastException ignore) {
        }
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateScheduleButton:
                //todo сделать кнопку обновления расписания
                break;
            case R.id.todayButton:
                //todo сделать прокручивание к сегодняшнему дню
                break;
            case R.id.menuButton:
                if (getScheduleListFragment() != null) {
                    this.menu.showMenu();
                }
                break;
        }
    }


    @Override
    public void onScheduleItemClick(ScheduleItem scheduleItem) {
        Bundle arguments = new Bundle();
        arguments.putSerializable("scheduleItem", scheduleItem);

        ScheduleDetailFragment scheduleDetailFragment = new ScheduleDetailFragment();
        scheduleDetailFragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.exit_to_left, R.animator.slide_in_left, R.animator.exit_to_left)
                .replace(R.id.frameLayout, scheduleDetailFragment)
                .addToBackStack("tag")
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("filterSpinnerItemsContainer", this.filterSpinnerItemsContainer);
    }
}
