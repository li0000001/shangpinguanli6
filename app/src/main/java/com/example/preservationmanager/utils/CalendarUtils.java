package com.example.preservationmanager.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.provider.CalendarContract;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 日历工具类 - 用于在系统日历中创建事件和添加提醒
 * 支持 Android 6.0+ 运行时权限申请
 */
public class CalendarUtils {

    private static final String[] CALENDAR_PERMISSIONS = {
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };

    private static final int PERMISSION_REQUEST_CODE = 1001;

    /**
     * 检查是否已经拥有日历权限
     *
     * @param context 上下文
     * @return 如果已获得权限返回 true，否则返回 false
     */
    public static boolean hasCalendarPermissions(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR)
                    == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)
                            == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    /**
     * 请求日历权限（用于 Activity）
     *
     * @param activity Activity 实例
     */
    public static void requestCalendarPermissions(androidx.appcompat.app.AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasCalendarPermissions(activity)) {
                ActivityCompat.requestPermissions(activity, CALENDAR_PERMISSIONS, PERMISSION_REQUEST_CODE);
            }
        }
    }

    /**
     * 获取或创建一个日历账户
     *
     * @param context 上下文
     * @return 日历 ID，如果获取失败返回 -1
     */
    public static long getOrCreateCalendarAccount(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR)
                    != PackageManager.PERMISSION_GRANTED) {
                return -1;
            }
        }

        // 查询现有日历账户
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String[] projection = {CalendarContract.Calendars._ID, CalendarContract.Calendars.ACCOUNT_NAME};
        String selection = CalendarContract.Calendars.VISIBLE + " = 1 AND " + CalendarContract.Calendars.IS_PRIMARY + " = 1";

        Cursor cursor = contentResolver.query(uri, projection, selection, null, null);
        long calendarId = -1;

        if (cursor != null && cursor.moveToFirst()) {
            calendarId = cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Calendars._ID));
            cursor.close();
            return calendarId;
        }

        if (cursor != null) {
            cursor.close();
        }

        // 如果没有主日历，则查询任何可用的日历
        cursor = contentResolver.query(uri, projection, CalendarContract.Calendars.VISIBLE + " = 1", null, null);

        if (cursor != null && cursor.moveToFirst()) {
            calendarId = cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Calendars._ID));
            cursor.close();
            return calendarId;
        }

        if (cursor != null) {
            cursor.close();
        }

        // 如果没有任何日历，创建一个本地日历
        return createLocalCalendar(context);
    }

    /**
     * 创建一个本地日历账户
     *
     * @param context 上下文
     * @return 新创建的日历 ID
     */
    private static long createLocalCalendar(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Calendars.ACCOUNT_NAME, "local_calendar");
        values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(CalendarContract.Calendars.NAME, "本地日历");
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "本地日历");
        values.put(CalendarContract.Calendars.CALENDAR_COLOR, 0xFF0000FF);
        values.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, TimeZone.getDefault().getID());
        values.put(CalendarContract.Calendars.VISIBLE, 1);
        values.put(CalendarContract.Calendars.IS_PRIMARY, 1);
        values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);

        Uri uri = Uri.withAppendedPath(CalendarContract.Calendars.CONTENT_URI, "local_calendar");

        try {
            Uri result = contentResolver.insert(uri, values);
            if (result != null) {
                return Long.parseLong(result.getLastPathSegment());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 向日历中插入一个事件
     *
     * @param context       上下文
     * @param calendarId    日历 ID
     * @param title         事件标题
     * @param description   事件描述
     * @param startTime     事件开始时间（毫秒）
     * @param endTime       事件结束时间（毫秒）
     * @param timeZone      时区
     * @return 事件 ID，如果插入失败返回 -1
     */
    public static long insertEvent(Context context, long calendarId, String title, String description,
                                    long startTime, long endTime, String timeZone) {
        ContentResolver contentResolver = context.getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)
                    != PackageManager.PERMISSION_GRANTED) {
                return -1;
            }
        }

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.DTSTART, startTime);
        values.put(CalendarContract.Events.DTEND, endTime);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);
        values.put(CalendarContract.Events.EVENT_COLOR, 0xFF0000FF);
        values.put(CalendarContract.Events.HAS_ALARM, 1);

        try {
            Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values);
            if (uri != null) {
                return Long.parseLong(uri.getLastPathSegment());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 为事件添加提醒
     *
     * @param context      上下文
     * @param eventId      事件 ID
     * @param minutesBefore 提前提醒的分钟数
     * @return 提醒 ID，如果添加失败返回 -1
     */
    public static long addReminder(Context context, long eventId, int minutesBefore) {
        ContentResolver contentResolver = context.getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)
                    != PackageManager.PERMISSION_GRANTED) {
                return -1;
            }
        }

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, eventId);
        values.put(CalendarContract.Reminders.MINUTES, minutesBefore);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);

        try {
            Uri uri = contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, values);
            if (uri != null) {
                return Long.parseLong(uri.getLastPathSegment());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 完整的事件和提醒创建方法（推荐使用）
     * 一次性创建事件和提醒
     *
     * @param context              上下文
     * @param title                事件标题（例如："牛奶过期提醒"）
     * @param description          事件描述（例如："这是一个商品过期提醒"）
     * @param startTime            事件开始时间（毫秒）
     * @param endTime              事件结束时间（毫秒）
     * @param reminderMinutesBefore 提前提醒的分钟数（例如：60 表示提前 1 小时）
     * @return 事件 ID，如果创建失败返回 -1
     */
    public static long addEventWithReminder(Context context, String title, String description,
                                             long startTime, long endTime, int reminderMinutesBefore) {
        return addEventWithReminder(context, title, description, startTime, endTime,
                reminderMinutesBefore, TimeZone.getDefault().getID());
    }

    /**
     * 完整的事件和提醒创建方法（推荐使用）
     * 一次性创建事件和提醒，可指定时区
     *
     * @param context              上下文
     * @param title                事件标题
     * @param description          事件描述
     * @param startTime            事件开始时间（毫秒）
     * @param endTime              事件结束时间（毫秒）
     * @param reminderMinutesBefore 提前提醒的分钟数
     * @param timeZone             时区（例如："Asia/Shanghai"）
     * @return 事件 ID，如果创建失败返回 -1
     */
    public static long addEventWithReminder(Context context, String title, String description,
                                             long startTime, long endTime, int reminderMinutesBefore,
                                             String timeZone) {
        // 检查权限
        if (!hasCalendarPermissions(context)) {
            return -1;
        }

        // 获取或创建日历账户
        long calendarId = getOrCreateCalendarAccount(context);
        if (calendarId == -1) {
            return -1;
        }

        // 插入事件
        long eventId = insertEvent(context, calendarId, title, description, startTime, endTime, timeZone);
        if (eventId == -1) {
            return -1;
        }

        // 添加提醒
        addReminder(context, eventId, reminderMinutesBefore);

        return eventId;
    }

    /**
     * 创建一个经过计算的日期时间（便于测试）
     *
     * @param year  年份
     * @param month 月份（0-11）
     * @param day   日期
     * @param hour  小时
     * @param minute 分钟
     * @return 时间戳（毫秒）
     */
    public static long createDateTime(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 创建一个未来某天的日期时间
     *
     * @param daysFromNow 距今天数（正数表示未来）
     * @param hour        小时
     * @param minute      分钟
     * @return 时间戳（毫秒）
     */
    public static long createFutureDateTime(int daysFromNow, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, daysFromNow);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 删除日历事件
     *
     * @param context 上下文
     * @param eventId 事件 ID
     * @return 删除成功返回 true，否则返回 false
     */
    public static boolean deleteEvent(Context context, long eventId) {
        ContentResolver contentResolver = context.getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        try {
            Uri uri = ContentResolver.SCHEME_CONTENT + "://" + CalendarContract.AUTHORITY + "/events/" + eventId;
            contentResolver.delete(Uri.parse(uri), null, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新日历事件
     *
     * @param context    上下文
     * @param eventId    事件 ID
     * @param title      新的事件标题
     * @param description 新的事件描述
     * @param startTime  新的事件开始时间（毫秒）
     * @param endTime    新的事件结束时间（毫秒）
     * @return 更新成功返回 true，否则返回 false
     */
    public static boolean updateEvent(Context context, long eventId, String title, String description,
                                       long startTime, long endTime) {
        ContentResolver contentResolver = context.getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.DTSTART, startTime);
        values.put(CalendarContract.Events.DTEND, endTime);

        try {
            Uri uri = Uri.withAppendedPath(CalendarContract.Events.CONTENT_URI, String.valueOf(eventId));
            int rows = contentResolver.update(uri, values, null, null);
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取指定事件的详细信息
     *
     * @param context 上下文
     * @param eventId 事件 ID
     * @return 包含事件信息的 ContentValues，如果获取失败返回 null
     */
    public static ContentValues getEventDetails(Context context, long eventId) {
        ContentResolver contentResolver = context.getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR)
                    != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
        }

        Uri uri = Uri.withAppendedPath(CalendarContract.Events.CONTENT_URI, String.valueOf(eventId));
        String[] projection = {
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DESCRIPTION,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.Events.EVENT_TIMEZONE
        };

        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        ContentValues values = null;

        if (cursor != null && cursor.moveToFirst()) {
            values = new ContentValues();
            values.put(CalendarContract.Events.TITLE, cursor.getString(0));
            values.put(CalendarContract.Events.DESCRIPTION, cursor.getString(1));
            values.put(CalendarContract.Events.DTSTART, cursor.getLong(2));
            values.put(CalendarContract.Events.DTEND, cursor.getLong(3));
            values.put(CalendarContract.Events.EVENT_TIMEZONE, cursor.getString(4));
            cursor.close();
        }

        if (cursor != null) {
            cursor.close();
        }

        return values;
    }
}
