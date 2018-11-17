package com.br.commonutils.util;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.net.InetAddress;

public class ConnectionUtil {

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
