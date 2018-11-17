package com.br.commonutils.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

public class VersionUtil {

    public static String getAppVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static String getAppVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return String.valueOf(packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static int getOSVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean isHoneyComb() {
        return getOSVersion() >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean isHoneyCombMR1() {
        return getOSVersion() >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean isHoneyCombMR2() {
        return getOSVersion() >= Build.VERSION_CODES.HONEYCOMB_MR2;
    }

    public static boolean isIceCreamSandwich() {
        return getOSVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean isIceCreamSandwichMR1() {
        return getOSVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }

    public static boolean isJellyBean() {
        return getOSVersion() >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean isJellyBeanMR1() {
        return getOSVersion() >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean isJellyBeanMR2() {
        return getOSVersion() >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean isKitkat() {
        return getOSVersion() >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean isLollipop() {
        return getOSVersion() >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isLollipopMR1() {
        return getOSVersion() >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean isMarshmallow() {
        return getOSVersion() >= Build.VERSION_CODES.M;
    }

    public static boolean isNougat() {
        return getOSVersion() >= Build.VERSION_CODES.N;
    }

    public static boolean isNougatMR1() {
        return getOSVersion() >= Build.VERSION_CODES.N_MR1;
    }

    public static boolean isOreo() {
        return getOSVersion() >= Build.VERSION_CODES.O;
    }

    public static boolean isOreoMR1() {
        return getOSVersion() >= Build.VERSION_CODES.O_MR1;
    }

    public static boolean isPie() {
        return getOSVersion() >= Build.VERSION_CODES.P;
    }
}
