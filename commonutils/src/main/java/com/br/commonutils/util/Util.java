package com.br.commonutils.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;

public class Util {

    public static void openCamera(@NonNull Activity activity, @NonNull File capturedImageLocation, int requestCode) {
        // Need to add FileProvider permission in app AndroidManifest, and XML file in res
        // (Already added) check for reference
        Uri uri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", capturedImageLocation);

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intentCamera, requestCode);
    }

    public static void openCamera(@NonNull Fragment fragment, @NonNull File capturedImageLocation, int requestCode) {
        Context context = fragment.getContext();

        // Need to add FileProvider permission in app AndroidManifest, and XML file in res
        // (Already added) check for reference
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", capturedImageLocation);

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        fragment.startActivityForResult(intentCamera, requestCode);
    }

    public static void openGallery(@NonNull Activity activity, boolean allowMultiSelect, int requestCode) {
        Intent intentGallery = new Intent();
        intentGallery.setType("image/*");
        intentGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiSelect);
        intentGallery.setAction(Intent.ACTION_PICK);
        activity.startActivityForResult(Intent.createChooser(intentGallery, "Select Picture"), requestCode);
    }

    public static void openGallery(@NonNull Fragment fragment, boolean allowMultiSelect, int requestCode) {
        Intent intentGallery = new Intent();
        intentGallery.setType("image/*");
        intentGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiSelect);
        intentGallery.setAction(Intent.ACTION_PICK);
        fragment.startActivityForResult(Intent.createChooser(intentGallery, "Select Picture"), requestCode);
    }

    public static void restartApp(Activity activity, @NonNull Class<?> activityToStart) {
        Intent intent = new Intent(activity, activityToStart);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 130219, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        System.exit(0);
    }

    public static void killApp() {
        System.exit(0);
    }

    public static void requestFocus(@NonNull Context context, @NonNull View view) {
        if (view.requestFocus())
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public static void hideSoftKeyboard(@NonNull Context context, @NonNull View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(@NonNull Context context, @NonNull View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static final Uri getUriFromResource(@NonNull Context context, @AnyRes int resId) {
        Resources resources = context.getResources();
        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + resources.getResourcePackageName(resId)
                + '/' + resources.getResourceTypeName(resId)
                + '/' + resources.getResourceEntryName(resId));
        return resUri;
    }
}
