package org.yohei.extendedcalendarview.widget.util;

import java.util.Calendar;

/**
 * Created by yohei on 3/15/15.
 */
public class CalendarUtils {

    private CalendarUtils() {
    }

    public static int getDayOfMonth(Calendar c) {
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static int getYear(Calendar c) {
        return c.get(Calendar.YEAR);
    }

    public static int getMonth(Calendar c) {
        return c.get(Calendar.MONTH);
    }

    public static boolean isCurrentDay(Calendar c) {
        final Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        if (getYear(c) != getYear(now)) {
            return false;
        }
        if (getMonth(c) != getMonth(now)) {
            return false;
        }
        return getDayOfMonth(c) == getDayOfMonth(now);
    }

    public static boolean isHoliday(Calendar c) {
        final int week = c.get(Calendar.DAY_OF_WEEK);
        return week == Calendar.SUNDAY || week == Calendar.SATURDAY;
    }

    public static boolean isSunday(Calendar c) {
        return c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    public static boolean isSaturday(Calendar c) {
        return c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
    }
}
