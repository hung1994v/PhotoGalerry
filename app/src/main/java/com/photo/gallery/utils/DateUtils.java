package com.photo.gallery.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final String FORMAT_DATE_2 = "EEEE, MM dd, yyyy hh:mm:ss a";
    public static final String FORMAT_TIME = "mm_ss";
    public static final String FORMAT_DATE_1 = "EEEE, MMM dd, yyyy";
    public static final String FORMAT_DATE = "EEE, d MMM yyyy";
    public static final String FORMAT_WEEKDAY = "EEE";
    public static final String FORMAT_DAY = "d";
    public static final String FORMAT_DAY_MONTH = "d MMM";
    public static final String FORMAT_MONTH_YEAR = "MMM yyyy";
    public static final String FORMAT_MONTH_DIGIT = "MM";
    /**
     * The maximum dateInLong possible.
     */
    public static Date MAX_DATE = new Date(Long.MAX_VALUE);

    /**
     * <p>Checks if two dates are on the same day ignoring time.</p>
     *
     * @param date1 the first dateInLong, not altered, not null
     * @param date2 the second dateInLong, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either dateInLong is <code>null</code>
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    /**
     * <p>Checks if two calendars represent the same day ignoring time.</p>
     *
     * @param cal1 the first calendar, not altered, not null
     * @param cal2 the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * <p>Checks if a dateInLong is today.</p>
     *
     * @param date the dateInLong, not altered, not null.
     * @return true if the dateInLong is today.
     * @throws IllegalArgumentException if the dateInLong is <code>null</code>
     */
    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    /**
     * <p>Checks if a calendar dateInLong is today.</p>
     *
     * @param cal the calendar, not altered, not null
     * @return true if cal dateInLong is today
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }

    /**
     * <p>Checks if the first dateInLong is before the second dateInLong ignoring time.</p>
     *
     * @param date1 the first dateInLong, not altered, not null
     * @param date2 the second dateInLong, not altered, not null
     * @return true if the first dateInLong day is before the second dateInLong day.
     * @throws IllegalArgumentException if the dateInLong is <code>null</code>
     */
    public static boolean isBeforeDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isBeforeDay(cal1, cal2);
    }

    /**
     * <p>Checks if the first calendar dateInLong is before the second calendar dateInLong ignoring time.</p>
     *
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 dateInLong is before cal2 dateInLong ignoring time.
     * @throws IllegalArgumentException if either of the calendars are <code>null</code>
     */
    public static boolean isBeforeDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return false;
        return cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * <p>Checks if the first dateInLong is after the second dateInLong ignoring time.</p>
     *
     * @param date1 the first dateInLong, not altered, not null
     * @param date2 the second dateInLong, not altered, not null
     * @return true if the first dateInLong day is after the second dateInLong day.
     * @throws IllegalArgumentException if the dateInLong is <code>null</code>
     */
    public static boolean isAfterDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isAfterDay(cal1, cal2);
    }

    /**
     * <p>Checks if the first calendar dateInLong is after the second calendar dateInLong ignoring time.</p>
     *
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 dateInLong is after cal2 dateInLong ignoring time.
     * @throws IllegalArgumentException if either of the calendars are <code>null</code>
     */
    public static boolean isAfterDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return true;
        return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * <p>Checks if a dateInLong is after today and within a number of days in the future.</p>
     *
     * @param date the dateInLong to check, not altered, not null.
     * @param days the number of days.
     * @return true if the dateInLong day is after today and within days in the future .
     * @throws IllegalArgumentException if the dateInLong is <code>null</code>
     */
    public static boolean isWithinDaysFuture(Date date, int days) {
        if (date == null) {
            throw new IllegalArgumentException("The dateInLong must not be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return isWithinDaysFuture(cal, days);
    }

    /**
     * <p>Checks if a calendar dateInLong is after today and within a number of days in the future.</p>
     *
     * @param cal  the calendar, not altered, not null
     * @param days the number of days.
     * @return true if the calendar dateInLong day is after today and within days in the future .
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isWithinDaysFuture(Calendar cal, int days) {
        if (cal == null) {
            throw new IllegalArgumentException("The dateInLong must not be null");
        }
        Calendar today = Calendar.getInstance();
        Calendar future = Calendar.getInstance();
        future.add(Calendar.DAY_OF_YEAR, days);
        return (isAfterDay(cal, today) && !isAfterDay(cal, future));
    }

    /**
     * Returns the given dateInLong with the time set to the start of the day.
     */
    public static Date getStart(Date date) {
        return clearTime(date);
    }

    /** Determines whether or not a dateInLong has any time values (hour, minute,
     * seconds or millisecondsReturns the given dateInLong with the time values cleared. */

    /**
     * Returns the given dateInLong with the time values cleared.
     */
    public static Date clearTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * Determines whether or not a dateInLong has any time values.
     *
     * @param date The dateInLong.
     * @return true iff the dateInLong is not null and any of the dateInLong's hour, minute,
     * seconds or millisecond values are greater than zero.
     */
    public static boolean hasTime(Date date) {
        if (date == null) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (c.get(Calendar.HOUR_OF_DAY) > 0) {
            return true;
        }
        if (c.get(Calendar.MINUTE) > 0) {
            return true;
        }
        if (c.get(Calendar.SECOND) > 0) {
            return true;
        }
        if (c.get(Calendar.MILLISECOND) > 0) {
            return true;
        }
        return false;
    }

    /**
     * Returns the given dateInLong with time set to the end of the day
     */
    public static Date getEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * Returns the maximum of two dates. A null dateInLong is treated as being less
     * than any non-null dateInLong.
     */
    public static Date max(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.after(d2)) ? d1 : d2;
    }

    /**
     * Returns the minimum of two dates. A null dateInLong is treated as being greater
     * than any non-null dateInLong.
     */
    public static Date min(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.before(d2)) ? d1 : d2;
    }

    /**
     * Return dateInLong in specified format.
     *
     * @param milliSeconds Date in milliseconds
     * @param dateFormat   Date format
     * @return String representing dateInLong in specified format
     */
    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying dateInLong in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());

        // Create a calendar object that will convert the dateInLong and time value in milliseconds to dateInLong.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static long getLong(String date, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Date dater = null;
        long dateInLong = 0;
        try {
            dater = formatter.parse(date);
            dateInLong = dater.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateInLong;
    }

        public static long getLong1(String date, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        Date dater = null;
        long dateInLong = 0;
        try {
            dater = formatter.parse(date);
            dateInLong = dater.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateInLong;
    }

    public static int getCountOfDays(String createdDateString, String expireDateString) {
        long startDay = getLong(createdDateString, FORMAT_DATE);
        long endDay = getLong(expireDateString, FORMAT_DATE);

        int dayCount = (int) (Math.abs(endDay - startDay) / (24 * 60 * 60 * 1000));

        return dayCount;
    }

    public static int getCountOfDays(long createdDateLong, long expireDateLong) {
        long startDay = createdDateLong;
        long endDay = expireDateLong;

        int dayCount = (int) (Math.abs(endDay - startDay) / (24 * 60 * 60 * 1000));

        return dayCount;
    }

    public static boolean isWidthRange(Date startDate, Date endDate, Date curDate) {
        return curDate.after(startDate) && curDate.before(endDate);
    }

    public static long convertDateToLong(String date, String format) {
        String string_date = date;
        long milliseconds = 0;

        SimpleDateFormat f = new SimpleDateFormat(format);
        try {
            Date d = f.parse(string_date);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    private String convertLongToDate(long timestamp, String format) {
//       MM dd yyyy
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(timestamp);
    }

    public static String getTime(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
