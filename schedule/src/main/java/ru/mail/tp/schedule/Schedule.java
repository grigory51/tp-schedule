package ru.mail.tp.schedule;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import java.util.HashMap;
import java.util.Map;

import ru.mail.tp.schedule.utils.ErrorSender;

/**
 * author: grigory51
 * date: 06/09/15
 */
@ReportsCrashes(
        formUri = "http://errors.ktsstudio.ru/collect",
        customReportContent = {ReportField.STACK_TRACE}
)
public class Schedule extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        String versionName = "1.1.1";
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException ignore) {
        }

        ACRA.init(this);
        Map<String, String> params = new HashMap<>();
        params.put("app", "Schedule");
        params.put("version", versionName);
        ACRA.getErrorReporter().setReportSender(new ErrorSender(
                HttpSender.Method.POST,
                HttpSender.Type.FORM,
                "http://errors.ktsstudio.ru/collect",
                null,
                params
        ));
    }
}
