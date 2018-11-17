package com.br.commonutils.log;

import android.support.annotation.NonNull;
import android.util.Log;

public class Logger {

    public static void logError(@NonNull String tag, @NonNull Exception exception) {
        writeLog(Log.ERROR, tag, exception.getLocalizedMessage());
    }

    public static void logInfo(@NonNull String tag, String message) {
        writeLog(Log.INFO, tag, message);
    }

    public static void logWarn(@NonNull String tag, String message) {
        writeLog(Log.WARN, tag, message);
    }

    public static void logDebug(@NonNull String tag, @NonNull Exception exception) {
        writeLog(Log.DEBUG, tag, exception.getLocalizedMessage());
    }

    public static void logVerbose(@NonNull String tag, @NonNull Exception exception) {
        writeLog(Log.VERBOSE, tag, exception.getLocalizedMessage());
    }

    private static void writeLog(int priority, String tag, String message) {
        switch (priority) {
            case Log.ERROR:
                Log.e(tag, message);
                break;

            case Log.INFO:
                Log.i(tag, message);
                break;

            case Log.WARN:
                Log.w(tag, message);
                break;

            case Log.DEBUG:
                Log.d(tag, message);
                break;

            case Log.VERBOSE:
                Log.v(tag, message);
                break;
        }
    }
}
