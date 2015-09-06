package ru.mail.tp.schedule.utils;

import android.content.Context;

import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.HttpSender;
import org.acra.sender.ReportSenderException;
import org.acra.util.HttpRequest;
import org.acra.util.JSONReportBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * author: grigory51
 * date: 06/06/15
 */
public class ErrorSender extends HttpSender {
    private final Method method;
    private final Type type;
    private String formUri;
    private final Map<ReportField, String> mapping;
    private final Map<String, String> customParameters;

    public ErrorSender(Method method, Type type, String formUri,
                       Map<ReportField, String> mapping, Map<String, String> customParameters) {
        super(method, type, formUri, mapping);
        this.method = method;
        this.type = type;
        this.mapping = mapping;
        this.formUri = formUri;
        this.customParameters = customParameters;
    }

    @Override
    public void send(Context context, CrashReportData report) throws ReportSenderException {
        try {
            URL reportUrl = formUri == null ? new URL(ACRA.getConfig().formUri()) : new URL(formUri);

            final String login = ACRAConfiguration.isNull(ACRA.getConfig().formUriBasicAuthLogin()) ? null : ACRA
                    .getConfig().formUriBasicAuthLogin();
            final String password = ACRAConfiguration.isNull(ACRA.getConfig().formUriBasicAuthPassword()) ? null : ACRA
                    .getConfig().formUriBasicAuthPassword();

            final HttpRequest request = new HttpRequest();
            request.setConnectionTimeOut(ACRA.getConfig().connectionTimeout());
            request.setSocketTimeOut(ACRA.getConfig().socketTimeout());
            request.setMaxNrRetries(ACRA.getConfig().maxNumberOfRequestRetries());
            request.setLogin(login);
            request.setPassword(password);
            request.setHeaders(ACRA.getConfig().getHttpHeaders());

            String reportAsString = "";

            switch (type) {
                case JSON:
                    reportAsString = report.toJSON().toString();
                    break;
                case FORM:
                default:
                    final Map<String, String> finalReport = remap(report, customParameters);
                    reportAsString = HttpRequest.getParamsAsFormString(finalReport);
                    break;

            }

            switch (method) {
                case POST:
                    break;
                case PUT:
                    reportUrl = new URL(reportUrl.toString() + '/' + report.getProperty(ReportField.REPORT_ID));
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown method: " + method.name());
            }
            request.send(context, reportUrl, method, reportAsString, type);

        } catch (IOException | JSONReportBuilder.JSONReportException e) {
//            throw new ReportSenderException("Error while sending " + ACRA.getConfig().reportType()
//                    + " report via Http " + method.name(), e);
        }
    }


    private Map<String, String> remap(Map<ReportField, String> report, Map<String, String> custom) {

        ReportField[] fields = ACRA.getConfig().customReportContent();
        if (fields.length == 0) {
            fields = ACRAConstants.DEFAULT_REPORT_FIELDS;
        }

        final Map<String, String> finalReport = new HashMap<String, String>(report.size() + custom.size());
        for (ReportField field : fields) {
            if (mapping == null || mapping.get(field) == null) {
                finalReport.put(field.toString(), report.get(field));
            } else {
                finalReport.put(mapping.get(field), report.get(field));
            }
        }
        for (String field : custom.keySet()) {
            finalReport.put(field, custom.get(field));
        }
        return finalReport;
    }
}