package ru.mail.tp.schedule.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import ru.mail.tp.schedule.R;
import ru.mail.tp.schedule.tasks.ITaskResultReceiver;
import ru.mail.tp.schedule.tasks.TaskResult;
import ru.mail.tp.schedule.tasks.scheduleFetch.ScheduleFetchTask;
import ru.mail.tp.schedule.tasks.scheduleFetch.ScheduleFetchTaskResult;

public class SplashActivity extends Activity implements ITaskResultReceiver {
    public static final String TAG_FETCH_RESULT = "fetchResult";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/pfisopro_regular.ttf");
        TextView myTextView = (TextView) findViewById(R.id.splashScreenTitle);
        myTextView.setTypeface(myTypeface);
    }

    @Override
    public void onStart() {
        super.onStart();
        ScheduleFetchTask task = new ScheduleFetchTask(this, getString(R.string.dataSourceUrl));
        task.execute();
    }

    @Override
    public void onPostExecute(TaskResult taskResult) {
        ScheduleFetchTaskResult result = (ScheduleFetchTaskResult) taskResult;
        startActivity(new Intent(this, ScheduleActivity.class).putExtra(TAG_FETCH_RESULT, result));
        finish();
    }
}
