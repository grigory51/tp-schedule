package ru.mail.tp.schedule.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
import ru.mail.tp.schedule.schedule.ScheduleCache;
import ru.mail.tp.schedule.schedule.ScheduleFilter;
import ru.mail.tp.schedule.schedule.db.DBHelper;
import ru.mail.tp.schedule.schedule.db.entities.ScheduleItem;
import ru.mail.tp.schedule.schedule.filter.FilterArrayAdapter;
import ru.mail.tp.schedule.schedule.filter.FilterSpinner;
import ru.mail.tp.schedule.schedule.filter.FilterSpinnerItemsContainer;
import ru.mail.tp.schedule.schedule.filter.OnFilterChangeListener;
import ru.mail.tp.schedule.tasks.scheduleFetch.ScheduleFetchTaskResult;
import ru.mail.tp.schedule.utils.MoscowCalendar;
import ru.mail.tp.schedule.utils.MoscowSimpleDateFormat;

public class ScheduleActivity extends SherlockFragmentActivity implements OnClickListener, OnScheduleItemClick, FragmentManager.OnBackStackChangedListener {
    private static final String CACHE_NAME = "ScheduleCache.txt";
    private Spinner subgroupsSpinner, disciplinesSpinner, typesSpinner;
    private CheckBox showPastCheckBox;
    private FilterSpinnerItemsContainer filterSpinnerItemsContainer = null;
    private ScheduleCache cache = null;
    private SlidingMenu menu;

    private boolean stackNotEmptySemaphore = false;

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

        this.subgroupsSpinner = (Spinner) findViewById(R.id.v_menu__subgroupsSpinner);
        this.disciplinesSpinner = (Spinner) findViewById(R.id.v_menu__disciplinesSpinner);
        this.typesSpinner = (Spinner) findViewById(R.id.v_menu__typesSpinner);
        this.showPastCheckBox = (CheckBox) findViewById(R.id.showPastCheckBox);

        if (savedInstanceState == null) {
            ScheduleFetchTaskResult fetchTaskResult = (ScheduleFetchTaskResult) getIntent().getExtras().get(SplashActivity.TAG_FETCH_RESULT);
            if (fetchTaskResult.getStatus() == ScheduleFetchTaskResult.STATUS_OK) {
                this.setLastUpdateDate(new Date(MoscowCalendar.getInstance().getTimeInMillis()));
            } else if (fetchTaskResult.getStatus() == ScheduleFetchTaskResult.STATUS_NETWORK_ERROR) {
                Toast.makeText(this, "Ошибка получения данных", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Ошибка обработки данных", Toast.LENGTH_LONG).show();
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.a_schedule__frameLayout, new ScheduleListFragment())
                    .commit();

            this.filterSpinnerItemsContainer = new FilterSpinnerItemsContainer(new DBHelper(this));
        } else {
            this.filterSpinnerItemsContainer = (FilterSpinnerItemsContainer) savedInstanceState.getSerializable("filterSpinnerItemsContainer");
        }

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        this.initSlideMenu();
        this.initFilters(this.filterSpinnerItemsContainer);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("filterSpinnerItemsContainer", this.filterSpinnerItemsContainer);
    }

    private void setLastUpdateDate(Date date) {
        TextView lastUpdateTextView = (TextView) findViewById(R.id.v_menu__lastUpdateTextView);
        lastUpdateTextView.setText("Последнее обновление\n" + new MoscowSimpleDateFormat("dd.MM.yyyy в HH:mm").format(date));
    }

    private void initFilters(FilterSpinnerItemsContainer filterContainer) {
        if (filterContainer != null) {
            FilterArrayAdapter subgroupListAdapter = new FilterArrayAdapter(this, filterContainer.getSubgroupItems());
            FilterArrayAdapter disciplineListAdapter = new FilterArrayAdapter(this, filterContainer.getDisciplineItems());
            FilterArrayAdapter typeListAdapter = new FilterArrayAdapter(this, filterContainer.getLessonTypeItems());

            subgroupsSpinner.setAdapter(subgroupListAdapter);
            disciplinesSpinner.setAdapter(disciplineListAdapter);
            typesSpinner.setAdapter(typeListAdapter);

            subgroupsSpinner.setSelection(filterSpinnerItemsContainer.getSubgroupPosition(), false);
            disciplinesSpinner.setSelection(filterSpinnerItemsContainer.getDisciplinePosition(), false);
            typesSpinner.setSelection(filterSpinnerItemsContainer.getLessonTypePosition(), false);
            showPastCheckBox.setChecked(filterSpinnerItemsContainer.getShowPassed());

            final OnFilterChangeListener filterChangeListener = new OnFilterChangeListener() {
                @Override
                public void onFilterChange() {
                    filterSpinnerItemsContainer.setSubgroupPosition(subgroupsSpinner.getSelectedItemPosition());
                    filterSpinnerItemsContainer.setDisciplinePosition(disciplinesSpinner.getSelectedItemPosition());
                    filterSpinnerItemsContainer.setLessonTypePosition(typesSpinner.getSelectedItemPosition());
                    filterSpinnerItemsContainer.setShowPassed(showPastCheckBox.isChecked());

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
    }

    private ScheduleFilter getScheduleFilter() {
        FilterSpinner subgroupItem = (FilterSpinner) subgroupsSpinner.getSelectedItem();
        FilterSpinner disciplineItem = (FilterSpinner) disciplinesSpinner.getSelectedItem();
        FilterSpinner typeItem = (FilterSpinner) typesSpinner.getSelectedItem();

        return new ScheduleFilter(subgroupItem.getId(), disciplineItem.getId(), typeItem.getId(), showPastCheckBox.isChecked());
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
            fragment = (ScheduleListFragment) getSupportFragmentManager().findFragmentById(R.id.a_schedule__frameLayout);
        } catch (ClassCastException ignore) {
        }
        return fragment;
    }

    private ScheduleDetailFragment getScheduleDetailFragment() {
        ScheduleDetailFragment fragment = null;
        try {
            fragment = (ScheduleDetailFragment) getSupportFragmentManager().findFragmentById(R.id.a_schedule__frameLayout);
        } catch (ClassCastException ignore) {
        }
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_actionbar__updateScheduleButton:
                //todo сделать кнопку обновления расписания
                break;
            case R.id.v_actionbar__todayButton:
                //todo сделать прокручивание к сегодняшнему дню
                break;
            case R.id.v_actionbar__menuButton:
                if (!this.stackNotEmptySemaphore) {
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
                .replace(R.id.a_schedule__frameLayout, scheduleDetailFragment)
                .addToBackStack("tag")
                .commit();
    }

    private void initSlideMenu() {
        ImageButton menuButton = (ImageButton) findViewById(R.id.v_actionbar__menuButton);
        ViewGroup.LayoutParams params = menuButton.getLayoutParams();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.stackNotEmptySemaphore = false;
            this.menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            params.width = 300;
        } else {
            this.stackNotEmptySemaphore = true;
            this.menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            params.width = 200;
        }
        menuButton.setLayoutParams(params);
        menuButton.requestLayout();
    }

    @Override
    public void onBackStackChanged() {
        this.initSlideMenu();
    }
}
