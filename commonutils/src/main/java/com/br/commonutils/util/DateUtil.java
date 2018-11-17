package com.br.commonutils.util;

import android.support.annotation.NonNull;
import android.text.format.DateFormat;

import java.util.Calendar;

public class DateUtil {

    public static String getFormattedDate(long milliseconds, @NonNull DatePattern datePattern) {
        return getFormattedDate(milliseconds, datePattern.name());
    }

    public static String getFormattedDate(long milliseconds, @NonNull String dateFormat) {
        String data = DateFormat.format(dateFormat, milliseconds).toString();

        if (data.contains("am"))
            data.replace("am", "AM");

        if (data.contains("a.m"))
            data.replace("a.m", "AM");

        if (data.contains("a.m."))
            data.replace("a.m.", "AM");

        if (data.contains("pm"))
            data.replace("pm", "PM");

        if (data.contains("p.m"))
            data.replace("p.m", "PM");

        if (data.contains("p.m."))
            data.replace("p.m.", "PM");

        return data;
    }

    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    public enum DatePattern {
        EEEE_MMMM_dd_hh_mm_a("EEEE, MMMM dd 'at' hh:mm a"),
        EEE_dd_MMM_yyyy_at_hh_mm_a("EEE, dd MMM yyyy 'at' hh:mm a"),
        EEE_dd_MMM_yyyy("EEE, dd MMM yyyy"),
        EEEE_dd_MMMM_yyyy("EEEE, dd MMMM yyyy"),
        dd_MMM_yyyy("dd MMM yyyy"),
        dd_MM_yyyy("dd/MM/yyyy"),
        MM_dd_yyyy(" MM/dd/yyyy"),
        yyyy__MM__dd(" yyyy-MM-dd"),
        hh_mm_a("hh:mm a");

        private String datePattern;

        DatePattern(String datePattern) {
            this.datePattern = datePattern;
        }
    }
}
