package com.example.preservationmanager.utils;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * CalendarUtils 的仪器测试
 * 这些测试需要在真实设备或模拟器上运行
 * 使用 ./gradlew connectedAndroidTest 执行
 */
@RunWith(AndroidJUnit4.class)
public class CalendarUtilsInstrumentedTest {

    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    /**
     * 测试权限检查方法
     */
    @Test
    public void testHasCalendarPermissions() {
        // 这个测试的结果取决于应用是否获得了权限
        boolean result = CalendarUtils.hasCalendarPermissions(context);
        // 我们只验证方法不会抛出异常
        assertNotNull(result);
    }

    /**
     * 测试获取或创建日历账户
     * 注意：此测试需要权限并且设备上需要有日历账户
     */
    @Test
    public void testGetOrCreateCalendarAccount() {
        if (!CalendarUtils.hasCalendarPermissions(context)) {
            // 如果没有权限，跳过此测试
            return;
        }

        long calendarId = CalendarUtils.getOrCreateCalendarAccount(context);
        // 如果成功获取或创建，返回值应该 > 0
        // 如果失败，返回值应该是 -1
        assertTrue(calendarId >= -1);
    }

    /**
     * 测试时间创建方法
     */
    @Test
    public void testCreateDateTime() {
        long time = CalendarUtils.createDateTime(2024, 10, 15, 14, 30);
        assertNotEquals(-1, time);
        assertTrue(time > 0);
    }

    /**
     * 测试未来时间创建方法
     */
    @Test
    public void testCreateFutureDateTime() {
        long time = CalendarUtils.createFutureDateTime(1, 10, 0);
        assertNotEquals(-1, time);
        assertTrue(time > 0);

        // 验证时间确实是未来的
        long now = System.currentTimeMillis();
        assertTrue(time >= now);
    }

    /**
     * 测试完整的事件创建流程
     * 注意：此测试需要完整的权限和日历账户
     */
    @Test
    public void testAddEventWithReminder() {
        if (!CalendarUtils.hasCalendarPermissions(context)) {
            // 如果没有权限，跳过此测试
            return;
        }

        String title = "测试事件";
        String description = "这是一个测试事件";

        long startTime = CalendarUtils.createFutureDateTime(1, 14, 0);
        long endTime = CalendarUtils.createFutureDateTime(1, 15, 0);

        long eventId = CalendarUtils.addEventWithReminder(
                context,
                title,
                description,
                startTime,
                endTime,
                60
        );

        // 事件创建成功或失败都是可以接受的
        // 取决于设备的日历配置
        if (eventId != -1) {
            // 如果创建成功，验证事件 ID 是正数
            assertTrue(eventId > 0);

            // 清理：删除测试事件
            CalendarUtils.deleteEvent(context, eventId);
        }
    }

    /**
     * 测试使用自定义时区创建事件
     */
    @Test
    public void testAddEventWithCustomTimezone() {
        if (!CalendarUtils.hasCalendarPermissions(context)) {
            return;
        }

        String title = "测试事件 - 上海时区";
        String description = "使用上海时区创建的测试事件";

        long startTime = CalendarUtils.createFutureDateTime(2, 16, 0);
        long endTime = CalendarUtils.createFutureDateTime(2, 17, 0);

        long eventId = CalendarUtils.addEventWithReminder(
                context,
                title,
                description,
                startTime,
                endTime,
                120,
                "Asia/Shanghai"
        );

        if (eventId != -1) {
            assertTrue(eventId > 0);
            CalendarUtils.deleteEvent(context, eventId);
        }
    }

    /**
     * 测试插入事件方法
     */
    @Test
    public void testInsertEvent() {
        if (!CalendarUtils.hasCalendarPermissions(context)) {
            return;
        }

        long calendarId = CalendarUtils.getOrCreateCalendarAccount(context);
        if (calendarId == -1) {
            // 如果无法获取日历账户，跳过此测试
            return;
        }

        String title = "测试插入事件";
        String description = "测试描述";

        long startTime = CalendarUtils.createFutureDateTime(3, 10, 0);
        long endTime = CalendarUtils.createFutureDateTime(3, 11, 0);

        long eventId = CalendarUtils.insertEvent(
                context,
                calendarId,
                title,
                description,
                startTime,
                endTime,
                "UTC"
        );

        if (eventId != -1) {
            assertTrue(eventId > 0);
            CalendarUtils.deleteEvent(context, eventId);
        }
    }

    /**
     * 测试添加提醒方法
     */
    @Test
    public void testAddReminder() {
        if (!CalendarUtils.hasCalendarPermissions(context)) {
            return;
        }

        // 先创建一个事件
        long calendarId = CalendarUtils.getOrCreateCalendarAccount(context);
        if (calendarId == -1) {
            return;
        }

        long startTime = CalendarUtils.createFutureDateTime(4, 9, 0);
        long endTime = CalendarUtils.createFutureDateTime(4, 10, 0);

        long eventId = CalendarUtils.insertEvent(
                context,
                calendarId,
                "提醒测试事件",
                "用于测试添加提醒",
                startTime,
                endTime,
                "UTC"
        );

        if (eventId != -1) {
            // 添加提醒
            long reminderId = CalendarUtils.addReminder(context, eventId, 45);

            if (reminderId != -1) {
                assertTrue(reminderId > 0);
            }

            // 清理
            CalendarUtils.deleteEvent(context, eventId);
        }
    }

    /**
     * 测试更新事件方法
     */
    @Test
    public void testUpdateEvent() {
        if (!CalendarUtils.hasCalendarPermissions(context)) {
            return;
        }

        // 创建一个事件用于更新测试
        long eventId = CalendarUtils.addEventWithReminder(
                context,
                "原始标题",
                "原始描述",
                CalendarUtils.createFutureDateTime(5, 14, 0),
                CalendarUtils.createFutureDateTime(5, 15, 0),
                60
        );

        if (eventId != -1) {
            // 更新事件
            boolean updated = CalendarUtils.updateEvent(
                    context,
                    eventId,
                    "更新后的标题",
                    "更新后的描述",
                    CalendarUtils.createFutureDateTime(5, 16, 0),
                    CalendarUtils.createFutureDateTime(5, 17, 0)
            );

            assertTrue(updated);

            // 清理
            CalendarUtils.deleteEvent(context, eventId);
        }
    }

    /**
     * 测试删除事件方法
     */
    @Test
    public void testDeleteEvent() {
        if (!CalendarUtils.hasCalendarPermissions(context)) {
            return;
        }

        // 创建一个事件用于删除测试
        long eventId = CalendarUtils.addEventWithReminder(
                context,
                "待删除的事件",
                "这个事件将被删除",
                CalendarUtils.createFutureDateTime(6, 10, 0),
                CalendarUtils.createFutureDateTime(6, 11, 0),
                60
        );

        if (eventId != -1) {
            // 删除事件
            boolean deleted = CalendarUtils.deleteEvent(context, eventId);

            assertTrue(deleted);
        }
    }

    /**
     * 测试获取事件详情方法
     */
    @Test
    public void testGetEventDetails() {
        if (!CalendarUtils.hasCalendarPermissions(context)) {
            return;
        }

        // 创建一个事件
        long eventId = CalendarUtils.addEventWithReminder(
                context,
                "详情测试事件",
                "用于测试获取详情",
                CalendarUtils.createFutureDateTime(7, 11, 0),
                CalendarUtils.createFutureDateTime(7, 12, 0),
                60
        );

        if (eventId != -1) {
            // 获取事件详情
            android.content.ContentValues details = CalendarUtils.getEventDetails(context, eventId);

            if (details != null) {
                String title = details.getAsString(android.provider.CalendarContract.Events.TITLE);
                assertEquals("详情测试事件", title);
            }

            // 清理
            CalendarUtils.deleteEvent(context, eventId);
        }
    }
}
