package com.br.commonutils.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.br.commonutils.provider.DelayProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CommonUtil {

    public static void makeDelay(DelayProvider delayProvider) {
        new Handler().postDelayed(() -> delayProvider.completed(), delayProvider.delayFor());
    }

    public static String getUniqueKey() {
        return UUID.randomUUID().toString();
    }

    public static int getRandomNumber(int end) {
        return new Random().nextInt(end);
    }

    public static int getNumberBetween(int start, int end) {
        // This gives a random integer between start (inclusive) and end (exclusive)
        return new Random().nextInt(end - start) + start;
    }

    public static <T> List<T> asList(T... values) {
        return Arrays.asList(values);
    }

    public static <T> Set<T> asSet(T... values) {
        return new HashSet<>(Arrays.asList(values));
    }

    public static String[] toArray(@NonNull String data) {
        String[] retVal = new String[data.length()];

        for (int i = 0; i < data.length(); i++) {
            retVal[i] = String.valueOf(data.charAt(i));
        }

        return retVal;
    }

    public static String toCSV(@NonNull Collection<?> data) {
        return TextUtils.join(", ", data);
    }

    public static String valueToCSV(@NonNull Map<?, ?> data) {
        return toCSV(data.keySet());
    }

    public static String keyToCSV(@NonNull Map<?, ?> data) {
        return toCSV(data.keySet());
    }

    public static String pad(int data) {
        return data >= 10 ? String.valueOf(data) : ("0" + data);
    }

    public static String limitString(@NonNull String data, int length) {
        if (data.length() <= length)
            return data;
        else
            return data.substring(0, length - 2) + "...";
    }

    @SuppressLint("NewApi")
    public static Spanned fromHTML(@NonNull String data) {
        if (VersionUtil.isNougat())
            return Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT);
        else
            return Html.fromHtml(data);
    }

    public static String getCountDown(long milliseconds) {
        return String.format("%02d min %02d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds), TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }
}
