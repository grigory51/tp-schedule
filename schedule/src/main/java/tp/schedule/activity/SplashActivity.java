package tp.schedule.activity;

import ru.mail.tp.schedule.R;
import tp.schedule.service.ScheduleService;

import android.app.PendingIntent;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onStart() {
        super.onStart();
        PendingIntent pendingIntent = createPendingResult(ScheduleService.TASK_FETCH, new Intent(), 0);
        startService(new Intent(this, ScheduleService.class).putExtra(ScheduleService.PARAM_PENDING_INTENT, pendingIntent));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        startActivity(new Intent(this, ScheduleActivity.class).putExtras(data.getExtras()));
        finish();
    }
}
