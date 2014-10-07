package ru.mail.tp.schedule.tasks.scheduleFetch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mail.tp.schedule.schedule.ScheduleJSONProcessor;
import ru.mail.tp.schedule.schedule.db.DBHelper;
import ru.mail.tp.schedule.schedule.db.entities.Discipline;
import ru.mail.tp.schedule.schedule.db.entities.LessonType;
import ru.mail.tp.schedule.schedule.db.entities.Place;
import ru.mail.tp.schedule.schedule.db.entities.ScheduleItem;
import ru.mail.tp.schedule.schedule.db.entities.Subgroup;
import ru.mail.tp.schedule.tasks.ITaskResultReceiver;
import ru.mail.tp.schedule.tasks.Task;
import ru.mail.tp.schedule.tasks.TaskResult;

/**
 * author: grigory51
 * date: 26/09/14
 */
public class ScheduleFetchTask extends Task {
    private final String url;
    private final Context context;
    private final ITaskResultReceiver receiver;

    private ScheduleFetchTask(Context context, ITaskResultReceiver receiver, String url) {
        this.url = url;
        this.context = context;
        this.receiver = receiver;
    }

    public ScheduleFetchTask(Context context, String url) {
        this(context, (ITaskResultReceiver) context, url);
    }

    private String getRawData() throws IOException {
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
    protected TaskResult doInBackground(Void... params) {
        String LOG_TAG = "ScheduleFetchTask";
        try {
            JSONObject json = new JSONObject(this.getRawData());
            ScheduleJSONProcessor processor = new ScheduleJSONProcessor(json);
            DBHelper dbHelper = new DBHelper(this.context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction();

            for (Discipline discipline : processor.getDisciplines()) {
                discipline.replace(db);
            }
            for (LessonType lessonType : processor.getLessonTypes()) {
                lessonType.replace(db);
            }
            for (Place place : processor.getPlaces()) {
                place.replace(db);
            }
            for (Subgroup subgroup : processor.getSubgroups()) {
                subgroup.replace(db);
            }
            for (ScheduleItem item : processor.getScheduleItems()) {
                item.replace(db);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            return new ScheduleFetchTaskResult(ScheduleFetchTaskResult.STATUS_OK);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Ошибка сети");
            e.printStackTrace();
            return new ScheduleFetchTaskResult(ScheduleFetchTaskResult.STATUS_NETWORK_ERROR);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Ошибка обработки данных");
            e.printStackTrace();
            return new ScheduleFetchTaskResult(ScheduleFetchTaskResult.STATUS_DATA_PROCESS_ERROR);
        }
    }

    @Override
    protected void onPostExecute(TaskResult taskResult) {
        super.onPostExecute(taskResult);
        this.receiver.onPostExecute(taskResult);
    }
}
