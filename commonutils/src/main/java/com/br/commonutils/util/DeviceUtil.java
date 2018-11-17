package com.br.commonutils.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DeviceUtil {

    public static String getAndroidId(@NonNull Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void disableScreenShot(@NonNull Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);

        return (xlarge || large);
    }

    public static int getDeviceWidth(@NonNull Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getDeviceHeight(@NonNull Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static int getDeviceDpi(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static int dpToPx(int dp) {
        return Math.round(dp * (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(int pixel) {
        return Math.round(pixel / (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static float dipToPx(float dipValue) {
        return Math.round(dipValue * (Resources.getSystem().getDisplayMetrics().density + 0.5f));
    }

    public static float spToPx(float spValue) {
        return Math.round(spValue * (Resources.getSystem().getDisplayMetrics().scaledDensity + 0.5f));
    }

    public static int getStatusBarHeight(@NonNull Context context) {
        int result = 0;

        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = context.getResources().getDimensionPixelSize(resourceId);

        return result;
    }

    public static int getNavigationBarHeight(@NonNull Context context) {
        int result = 0;

        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = context.getResources().getDimensionPixelSize(resourceId);

        return result;
    }

    public static void setPortraitOrientation(@NonNull Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static void setLandScapeOrientation(@NonNull Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    @SuppressLint("NewApi")
    public static void setStatusBarColor(@NonNull Activity activity, int color) {
        if (VersionUtil.isLollipop()) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(ColorUtil.getColor(activity, color));
        }
    }

    public static int getSmallImageSize(@NonNull Context context) {
        int widthInPixel = getDeviceWidth(context);
        return widthInPixel / 10;
    }

    public static int getMediumImageSize(@NonNull Context context) {
        int widthInPixel = getDeviceWidth(context);
        return widthInPixel / 7;
    }

    public static int getLargeImageSize(@NonNull Context context) {
        int widthInPixel = getDeviceWidth(context);
        return widthInPixel / 4;
    }

    public static int getExtraLargeImageSize(@NonNull Context context) {
        int widthInPixel = getDeviceWidth(context);
        return widthInPixel / 3;
    }
}
