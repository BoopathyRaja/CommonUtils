package com.br.commonutils.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.File;
import java.net.InetAddress;

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
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 130290, intent, PendingIntent.FLAG_CANCEL_CURRENT);

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

    public static String[] getAddedPermissionsFromManifest(Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions;
    }

    public static boolean isInternetEnabled() {
        try {
            InetAddress inetAddress = InetAddress.getByName("www.google.com");
            return !inetAddress.equals("");
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isGPSEnabled(@NonNull Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @RequiresPermission(anyOf = {Manifest.permission.USE_FINGERPRINT, Manifest.permission.USE_BIOMETRIC})
    public static boolean isFingerprintBiometricAvaialble(@NonNull Context context) {
        return FingerprintManagerCompat.from(context).isHardwareDetected();
    }

    @RequiresPermission(anyOf = {Manifest.permission.USE_FINGERPRINT, Manifest.permission.USE_BIOMETRIC})
    public static boolean isFingerprintEnrolled(@NonNull Context context) {
        return FingerprintManagerCompat.from(context).hasEnrolledFingerprints();
    }

    public static boolean isServiceRunning(@NonNull Context context, @NonNull Class<? extends Service> serviceClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName()))
                return true;
        }

        return false;
    }

    public static boolean isPlayServicesAvailable(@NonNull Context context) {
        boolean retVal = false;

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);
        if (result == ConnectionResult.SUCCESS)
            retVal = true;

        return retVal;
    }
}
