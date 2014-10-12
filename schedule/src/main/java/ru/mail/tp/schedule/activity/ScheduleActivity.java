package ru.mail.tp.schedule.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

import java.util.Date;

import ru.mail.tp.schedule.R;
import ru.mail.tp.schedule.fragments.OnScheduleItemClick;
import ru.mail.tp.schedule.fragments.ScheduleDetailFragment;
import ru.mail.tp.schedule.fragments.ScheduleListFragment;
import ru.mail.tp.schedule.schedule.ScheduleFilter;
import ru.mail.tp.schedule.schedule.db.DBHelper;
import ru.mail.tp.schedule.schedule.db.entities.ScheduleItem;
import ru.mail.tp.schedule.schedule.filter.FilterArrayAdapter;
import ru.mail.tp.schedule.schedule.filter.FilterSpinner;
import ru.mail.tp.schedule.schedule.filter.FilterSpinnerItemsContainer;
import ru.mail.tp.schedule.schedule.filter.OnFilterChangeListener;
import ru.mail.tp.schedule.tasks.scheduleFetch.ScheduleFetchTaskResult;
import ru.mail.tp.schedule.utils.ActionBarSherlockMenuItemAdapter;
import ru.mail.tp.schedule.utils.MoscowCalendar;
import ru.mail.tp.schedule.utils.MoscowSimpleDateFormat;

public class ScheduleActivity extends SherlockFragmentActivity implements OnScheduleItemClick, FragmentManager.OnBackStackChangedListener {
    private Spinner subgroupsSpinner, disciplinesSpinner, typesSpinner;
    private CheckBox showPastCheckBox;
    private FilterSpinnerItemsContainer filterSpinnerItemsContainer = null;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        this.subgroupsSpinner = (Spinner) findViewById(R.id.v_menu__subgroupsSpinner);
        this.disciplinesSpinner = (Spinner) findViewById(R.id.v_menu__disciplinesSpinner);
        this.typesSpinner = (Spinner) findViewById(R.id.v_menu__typesSpinner);
        this.showPastCheckBox = (CheckBox) findViewById(R.id.showPastCheckBox);
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

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

        this.initDrawer();
        this.initFilters(this.filterSpinnerItemsContainer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return drawerToggle != null && drawerToggle.onOptionsItemSelected(new ActionBarSherlockMenuItemAdapter(item)) || super.onOptionsItemSelected(item);
        } else {
            getSupportFragmentManager().popBackStack();
            return true;
        }
    }

    @Override
    public void onBackStackChanged() {
        if (this.drawerToggle != null) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                this.drawerToggle.setDrawerIndicatorEnabled(false);
                this.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                this.drawerToggle.setDrawerIndicatorEnabled(true);
                this.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (this.drawerToggle != null) {
            this.drawerToggle.syncState();
        }
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

    @SuppressLint("InlinedApi")
    private void initDrawer() {
        if (this.drawerLayout != null) {
            this.drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.opened, R.string.closed);
            this.drawerToggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() == 0);

            drawerLayout.setDrawerListener(this.drawerToggle);

            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeButtonEnabled(true);
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
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

    private ScheduleListFragment getScheduleListFragment() {
        ScheduleListFragment fragment = null;
        try {
            fragment = (ScheduleListFragment) getSupportFragmentManager().findFragmentById(R.id.a_schedule__frameLayout);
        } catch (ClassCastException ignore) {
        }
        return fragment;
    }

    @Override
    public void onScheduleItemClick(ScheduleItem scheduleItem) {
        ScheduleDetailFragment detailFragment = (ScheduleDetailFragment) getSupportFragmentManager().findFragmentById(R.id.a_schedule__frameLayout_detail);

        if (detailFragment == null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable("scheduleItem", scheduleItem);

            ScheduleDetailFragment scheduleDetailFragment = new ScheduleDetailFragment();
            scheduleDetailFragment.setArguments(arguments);

            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_left, R.animator.slide_out_right)
                    .replace(R.id.a_schedule__frameLayout, scheduleDetailFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            detailFragment.showItem(scheduleItem);
        }
    }
}