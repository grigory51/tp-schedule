package tp.schedule.activity;

import java.util.Calendar;

import ru.mail.tp.schedule.R;
import tp.schedule.schedule.ScheduleListAdapter;
import tp.schedule.utils.Filter;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ScheduleActivity extends SherlockActivity implements OnClickListener {
    private ScheduleActivity activity;
    private ListView mSchedule;
    private Filter filter;
    private SlidingMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        activity = this;
        mSchedule = (ListView) findViewById(R.id.schedule);

        initActionBar();

        loadFilter();
/*
        try {
            mSchedule.setAdapter(new ScheduleListAdapter(this, Dater.instance().getSchedule(filter)));
        } catch (Exception e) {

        }

        initMenu();
        initSpinners();*/
        if (this.scheduleFilter != null) {
            this.initSpinners();
        }
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    private void initMenu() {
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(R.layout.menu);
    private void initSpinners() {
        FilterArrayAdapter subgroupListAdapter = new FilterArrayAdapter(this);
        FilterArrayAdapter disciplineListAdapter = new FilterArrayAdapter(this);
        FilterArrayAdapter typeListAdapter = new FilterArrayAdapter(this);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        menu.setBehindWidth((int) (screenWidth * 0.8));
        Spinner subgroupsSpinner = (Spinner) findViewById(R.id.subgroupsSpinner);
        Spinner disciplinesSpinner = (Spinner) findViewById(R.id.disciplinesSpinner);
        Spinner typesSpinner = (Spinner) findViewById(R.id.typesSpinner);

    }
        subgroupsSpinner.setAdapter(subgroupListAdapter);
        disciplinesSpinner.setAdapter(disciplineListAdapter);
        typesSpinner.setAdapter(typeListAdapter);

        subgroupListAdapter.addAll(this.scheduleFilter.getSubgroupTitles());
        disciplineListAdapter.addAll(this.scheduleFilter.getDisciplineTitles());
        typeListAdapter.addAll(this.scheduleFilter.getTypeTitles());

        subgroupsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                /*int position = ((Spinner) findViewById(R.id.subgroupsSpinner)).getSelectedItemPosition();
                scheduleFilter.setSubgroup(position);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        disciplinesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //scheduleFilter.setDiscipline(((Spinner) findViewById(R.id.disciplinesSpinner)).getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        typesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //scheduleFilter.setType(((Spinner) findViewById(R.id.typesSpinner)).getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void updateSchedule() {
    }

    private void saveData(String data) {
        SharedPreferences sPref = getSharedPreferences("Data", MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putString("data", data);
        ed.commit();
    }

    @Override
    public void onClick(View v) {
        Log.v("Smth", "onCLick");
        switch (v.getId()) {
            case R.id.updateScheduleButton:
                updateSchedule();
                menu.toggle();
                break;
            case R.id.todayButton:
                scrollSchedule();
                break;
            case R.id.menuButton:
                menu.showMenu();
                break;
        }
    }

    private void updateScheduleView() {

    }

    private void scrollSchedule() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
    }

    private boolean isMoreThen(int version) {
        return android.os.Build.VERSION.SDK_INT >= version;
    }

    private void loadFilter() {
        SharedPreferences sPref = getSharedPreferences("Data", MODE_PRIVATE);
        int subgroup = sPref.getInt("subgroup", -1);
        int discipline = sPref.getInt("discipline", -1);
        int type = sPref.getInt("type", -1);
        filter = new Filter(subgroup, discipline, type);
    }

    private void saveFilter() {
        SharedPreferences sPref = getSharedPreferences("Data", MODE_PRIVATE);
        Editor editor = sPref.edit();
        editor.putInt("subgroup", filter.getSubgroup());
        editor.putInt("discipline", filter.getDiscipline());
        editor.putInt("type", filter.getType());
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        saveFilter();
        super.onDestroy();
    }
}
