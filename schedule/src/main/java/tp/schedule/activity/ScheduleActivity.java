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

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        menu.setBehindWidth((int) (screenWidth * 0.8));

    }

    private void initSpinners() {
        initSubgroupsSpinner();
        initDisciplinesSpinner();
        initTypesSpinner();
    }

    private void initSubgroupsSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner subgroupsSpinner = (Spinner) findViewById(R.id.subgroupsSpinner);
        subgroupsSpinner.setAdapter(adapter);
        subgroupsSpinner.setSelection(filter.getSubgroup() + 1);
        subgroupsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                int position = ((Spinner) findViewById(R.id.subgroupsSpinner)).getSelectedItemPosition();
                filter.setSubgroup(position);

                updateScheduleView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void initDisciplinesSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner disciplinesSpinner = (Spinner) findViewById(R.id.disciplinesSpinner);
        disciplinesSpinner.setAdapter(adapter);
        disciplinesSpinner.setSelection(filter.getDiscipline() + 1);
        disciplinesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                filter.setDiscipline(((Spinner) findViewById(R.id.disciplinesSpinner)).getSelectedItemPosition());
                updateScheduleView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void initTypesSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner typesSpinner = (Spinner) findViewById(R.id.typesSpinner);
        typesSpinner.setAdapter(adapter);
        typesSpinner.setSelection(filter.getType() + 1);
        typesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                filter.setType(((Spinner) findViewById(R.id.typesSpinner)).getSelectedItemPosition());
                updateScheduleView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
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
