package tp.schedule.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.app.PendingIntent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tp.schedule.schedule.ScheduleItem;
import tp.schedule.schedule.ScheduleItemFactory;


/**
 * author: grigory51
 * date: 04.07.14
 */

public class ScheduleService extends Service {
    public static int TASK_FETCH = 1;

    public static int STATUS_OK = 0;
    public static int STATUS_ERROR = 1;

    public static String PARAM_DATA = "data";
    public static String PARAM_ERROR_MSG = "errorMessage";
    public static String PARAM_STATUS = "status";


    final String LOG_TAG = "ScheduleService";
    String dataSourceUrl = "http://tptt.ozhegov.name/";
    ArrayList<ScheduleItem> data = new ArrayList<ScheduleItem>();
    ExecutorService executorService;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.executorService = Executors.newFixedThreadPool(1);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        PendingIntent pendingIntent = intent.getParcelableExtra("pendingIntent");

        this.executorService.execute(new ScheduleFetcher(this.dataSourceUrl, pendingIntent));
        return super.onStartCommand(intent, flags, startId);
    }

    public class ScheduleFetcher implements Runnable {
        private PendingIntent pendingIntent;
        private String url;

        public ScheduleFetcher(String url, PendingIntent pendingIntent) {
            this.url = url;
            this.pendingIntent = pendingIntent;
        }

        private String fetchSchedule() throws IOException {
            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[1024];

            URL url = new URL(this.url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

            int count;
            do {
                count = inputStream.read(buffer);
                if (count > 0) {
                    sb.append(new String(buffer, 0, count));
                }
            } while (count != -1);

            inputStream.close();
            urlConnection.disconnect();

            return sb.toString();
        }

        @Override
        public void run() {
            Intent intent = new Intent();
            int status;
            try {
                String data = this.fetchSchedule();
                JSONObject json = new JSONObject(data);
                ScheduleItemFactory factory = new ScheduleItemFactory(json);

                status = ScheduleService.STATUS_OK;
                intent.putExtra(ScheduleService.PARAM_DATA, factory.getScheduleItems());
            } catch (IOException e) {
                Log.e(LOG_TAG, "Ошибка сети");
                status = ScheduleService.STATUS_ERROR;
                intent.putExtra(ScheduleService.PARAM_ERROR_MSG, "Ошибка сети");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Ошибка обработки данных");
                status = ScheduleService.STATUS_ERROR;
                intent.putExtra(ScheduleService.PARAM_ERROR_MSG, "Ошибка обработки данных");
                e.printStackTrace();
            }

            try {
                this.pendingIntent.send(ScheduleService.this, status, intent);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

}
