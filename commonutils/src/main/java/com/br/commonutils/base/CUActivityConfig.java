package com.br.commonutils.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;

public class CUActivityConfig {

    public static void makeCall(@NonNull Activity activity, @NonNull String mobileNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mobileNumber));
        activity.startActivity(intent);
    }

    public static void sendEmail(@NonNull Activity activity, @NonNull String emailAddress) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + emailAddress));
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
//        intent.putExtra(Intent.EXTRA_HTML_TEXT, "");    // If you are using HTML in your body text
//        activity.startActivity(Intent.createChooser(intent, "Chooser Title"));
        activity.startActivity(intent);
    }

    public static void sharePlainText(@NonNull Activity activity, @NonNull String data) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, data);
        intent.setType("text/plain");
        activity.startActivity(intent);
    }

    public static void loadUrlInWeb(@NonNull Activity activity, @NonNull String url) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(sendIntent);
    }

    public static void openSecuritySettings(@NonNull Activity activity) {
        openSecuritySettings(activity, -1);
    }

    public static void openSecuritySettings(@NonNull Activity activity, int requestCode) {
        openSettings(activity, Settings.ACTION_SECURITY_SETTINGS, requestCode);
    }

    public static void openFingerprintEnrollment(@NonNull Activity activity) {
        openFingerprintEnrollment(activity, -1);
    }

    public static void openFingerprintEnrollment(@NonNull Activity activity, int requestCode) {
        openSettings(activity, Settings.ACTION_FINGERPRINT_ENROLL, requestCode);
    }

    private static void openSettings(@NonNull Activity activity, String type, int requestCode) {
        Intent intent = new Intent(type);

        if (requestCode != -1)
            activity.startActivityForResult(intent, requestCode);
        else
            activity.startActivity(intent);
    }
}
