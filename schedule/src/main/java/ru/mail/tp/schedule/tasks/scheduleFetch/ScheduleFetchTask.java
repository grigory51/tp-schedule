package ru.mail.tp.schedule.tasks.scheduleFetch;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mail.tp.schedule.schedule.ScheduleBuilder;
import ru.mail.tp.schedule.schedule.ScheduleJSONProcessor;
import ru.mail.tp.schedule.tasks.ITaskResultReceiver;
import ru.mail.tp.schedule.tasks.Task;
import ru.mail.tp.schedule.tasks.TaskResult;

/**
 * author: grigory51
 * date: 26/09/14
 */
public class ScheduleFetchTask extends Task {
    private final String LOG_TAG = "ScheduleFetchTask";
    private String url;
    private ITaskResultReceiver receiver;

    public ScheduleFetchTask(ITaskResultReceiver receiver, String url) {
        this.url = url;
        this.receiver = receiver;
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
        try {
            String data = this.getRawData();
            JSONObject json = new JSONObject(data);
            ScheduleJSONProcessor processor = new ScheduleJSONProcessor(json);
            return new ScheduleFetchTaskResult(ScheduleFetchTaskResult.STATUS_OK, new ScheduleBuilder(processor.getScheduleItems()), processor.getScheduleFiltersData());
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
