package tp.schedule.activity;

import ru.mail.tp.schedule.R;
import tp.schedule.service.ScheduleService;

import android.app.PendingIntent;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.Toast;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
    }

    @Override
    public void onStart() {
        super.onStart();
        PendingIntent pendingIntent = createPendingResult(ScheduleService.TASK_FETCH, new Intent(), 0);
        startService(new Intent(this, ScheduleService.class).putExtra("pendingIntent", pendingIntent));
    }

    private void changeActivity() {
        startActivity(new Intent(this, ScheduleActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private String loadData() {
        SharedPreferences sPref = getSharedPreferences("Data", MODE_PRIVATE);
        return sPref.getString("data", null);
    }

    private void saveData(String data) {
        SharedPreferences sPref = getSharedPreferences("Data", MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putString("data", data);
        ed.commit();
    }

    private void refreshData() {
        findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
        startService(new Intent(this, ScheduleService.class));
    }
}
