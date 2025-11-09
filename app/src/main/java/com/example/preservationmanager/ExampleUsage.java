package com.example.preservationmanager;

import android.content.Context;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;
import com.example.preservationmanager.utils.CalendarUtils;

/**
 * CalendarUtils 的使用示例
 * 展示各种常见场景的调用方式
 */
public class ExampleUsage {

    /**
     * 示例 1: 最简单的调用方式 - 创建一个保质期提醒
     * 这是推荐的使用方法
     */
    public static void example1_SimpleUsage(AppCompatActivity activity) {
        String title = "牛奶保质期提醒";
        String description = "商品：牛奶\n数量：1 瓶\n购买日期：2024-11-01";

        // 事件时间：明天的 09:00 - 10:00
        long startTime = CalendarUtils.createFutureDateTime(1, 9, 0);
        long endTime = CalendarUtils.createFutureDateTime(1, 10, 0);

        // 提前 60 分钟提醒
        long eventId = CalendarUtils.addEventWithReminder(
                activity,
                title,
                description,
                startTime,
                endTime,
                60  // 提醒时间：分钟
        );

        if (eventId != -1) {
            System.out.println("事件创建成功，ID: " + eventId);
        } else {
            System.out.println("事件创建失败");
        }
    }

    /**
     * 示例 2: 带有权限检查的完整流程
     */
    public static void example2_WithPermissionCheck(AppCompatActivity activity) {
        // 第 1 步：检查权限
        if (!CalendarUtils.hasCalendarPermissions(activity)) {
            // 权限未授予，请求权限
            CalendarUtils.requestCalendarPermissions(activity);
            System.out.println("权限不足，已请求权限");
            return;
        }

        // 第 2 步：权限已授予，继续创建事件
        String title = "酸奶保质期提醒";
        String description = "商品：酸奶\n规格：500ml\n保存温度：2-8°C";

        // 3 天后的 14:00 - 15:00
        long startTime = CalendarUtils.createFutureDateTime(3, 14, 0);
        long endTime = CalendarUtils.createFutureDateTime(3, 15, 0);

        long eventId = CalendarUtils.addEventWithReminder(
                activity,
                title,
                description,
                startTime,
                endTime,
                30  // 提前 30 分钟提醒
        );

        if (eventId != -1) {
            System.out.println("事件创建成功");
        }
    }

    /**
     * 示例 3: 指定时区的事件创建
     */
    public static void example3_WithCustomTimezone(AppCompatActivity activity) {
        String title = "黄油保质期提醒";
        String description = "商品：黄油\n保存地点：冷藏柜";

        long startTime = CalendarUtils.createFutureDateTime(7, 10, 0);
        long endTime = CalendarUtils.createFutureDateTime(7, 11, 0);

        // 指定时区为上海时区
        long eventId = CalendarUtils.addEventWithReminder(
                activity,
                title,
                description,
                startTime,
                endTime,
                120,  // 提前 120 分钟提醒
                "Asia/Shanghai"
        );

        if (eventId != -1) {
            System.out.println("上海时区事件创建成功，ID: " + eventId);
        }
    }

    /**
     * 示例 4: 详细的多步骤操作（手动分步）
     */
    public static void example4_StepByStepManualOperation(Context context) {
        // 步骤 1：请求权限
        if (!CalendarUtils.hasCalendarPermissions(context)) {
            System.out.println("权限不足");
            return;
        }

        // 步骤 2：获取或创建日历账户
        long calendarId = CalendarUtils.getOrCreateCalendarAccount(context);
        if (calendarId == -1) {
            System.out.println("获取日历账户失败");
            return;
        }
        System.out.println("获取日历账户成功，ID: " + calendarId);

        // 步骤 3：创建事件
        String title = "芝士保质期提醒";
        String description = "商品：芝士\n数量：200g\n保存地点：冷藏柜";

        long startTime = CalendarUtils.createFutureDateTime(5, 16, 0);
        long endTime = CalendarUtils.createFutureDateTime(5, 17, 0);

        long eventId = CalendarUtils.insertEvent(
                context,
                calendarId,
                title,
                description,
                startTime,
                endTime,
                "Asia/Shanghai"
        );

        if (eventId == -1) {
            System.out.println("创建事件失败");
            return;
        }
        System.out.println("创建事件成功，ID: " + eventId);

        // 步骤 4：为事件添加提醒
        long reminderId = CalendarUtils.addReminder(context, eventId, 60);
        if (reminderId == -1) {
            System.out.println("添加提醒失败");
        } else {
            System.out.println("添加提醒成功，提醒 ID: " + reminderId);
        }
    }

    /**
     * 示例 5: 多个不同时间的提醒
     * 虽然 CalendarUtils 每次只能添加一个提醒，
     * 但可以通过多次调用来添加多个提醒
     */
    public static void example5_MultipleReminders(Context context) {
        String title = "鸡蛋保质期提醒";
        String description = "商品：鸡蛋\n数量：10 个\n购买日期：2024-11-01";

        long startTime = CalendarUtils.createFutureDateTime(14, 10, 0);
        long endTime = CalendarUtils.createFutureDateTime(14, 11, 0);

        // 先创建事件（只有一个提醒）
        long eventId = CalendarUtils.addEventWithReminder(
                context,
                title,
                description,
                startTime,
                endTime,
                1440  // 提前 1 天提醒
        );

        if (eventId != -1) {
            // 添加第二个提醒（提前 1 小时）
            CalendarUtils.addReminder(context, eventId, 60);
            System.out.println("事件已创建，ID: " + eventId);
        }
    }

    /**
     * 示例 6: 使用指定日期和时间创建事件
     */
    public static void example6_SpecificDateTime(AppCompatActivity activity) {
        String title = "牛奶促销到期提醒";
        String description = "促销信息：\n商品：进口牛奶\n折扣：30%\n促销截止日期：2024-11-30";

        // 创建指定日期时间：2024 年 11 月 30 日 23:59
        long startTime = CalendarUtils.createDateTime(2024, 10, 30, 23, 0);  // 注意：月份是 0-11
        long endTime = CalendarUtils.createDateTime(2024, 10, 31, 0, 0);

        // 创建事件
        long eventId = CalendarUtils.addEventWithReminder(
                activity,
                title,
                description,
                startTime,
                endTime,
                240  // 提前 4 小时提醒
        );

        if (eventId != -1) {
            System.out.println("促销提醒已创建");
        }
    }

    /**
     * 示例 7: 更新和删除事件
     */
    public static void example7_UpdateAndDelete(Context context, long eventId) {
        // 更新事件
        boolean updated = CalendarUtils.updateEvent(
                context,
                eventId,
                "新的事件标题",
                "新的事件描述",
                CalendarUtils.createFutureDateTime(2, 10, 0),
                CalendarUtils.createFutureDateTime(2, 11, 0)
        );

        if (updated) {
            System.out.println("事件已更新");
        }

        // 删除事件
        boolean deleted = CalendarUtils.deleteEvent(context, eventId);
        if (deleted) {
            System.out.println("事件已删除");
        }
    }

    /**
     * 示例 8: 获取事件详情
     */
    public static void example8_GetEventDetails(Context context, long eventId) {
        android.content.ContentValues details = CalendarUtils.getEventDetails(context, eventId);

        if (details != null) {
            String title = details.getAsString(android.provider.CalendarContract.Events.TITLE);
            String description = details.getAsString(android.provider.CalendarContract.Events.DESCRIPTION);
            long startTime = details.getAsLong(android.provider.CalendarContract.Events.DTSTART);
            long endTime = details.getAsLong(android.provider.CalendarContract.Events.DTEND);
            String timezone = details.getAsString(android.provider.CalendarContract.Events.EVENT_TIMEZONE);

            System.out.println("标题: " + title);
            System.out.println("描述: " + description);
            System.out.println("开始时间: " + startTime);
            System.out.println("结束时间: " + endTime);
            System.out.println("时区: " + timezone);
        } else {
            System.out.println("未能获取事件详情");
        }
    }

    /**
     * 示例 9: 在线程中异步操作
     * 适合在后台线程中执行耗时操作
     */
    public static void example9_AsyncOperation(AppCompatActivity activity) {
        // 在后台线程中创建事件
        new Thread(() -> {
            String title = "酸奶活动到期提醒";
            String description = "商品：活性酸奶\n活动截止：2024-11-25";

            long startTime = CalendarUtils.createFutureDateTime(10, 12, 0);
            long endTime = CalendarUtils.createFutureDateTime(10, 13, 0);

            long eventId = CalendarUtils.addEventWithReminder(
                    activity,
                    title,
                    description,
                    startTime,
                    endTime,
                    120
            );

            // 在主线程中处理结果
            activity.runOnUiThread(() -> {
                if (eventId != -1) {
                    System.out.println("事件创建成功");
                } else {
                    System.out.println("事件创建失败");
                }
            });
        }).start();
    }

    /**
     * 示例 10: 完整的商品批量提醒创建
     */
    public static void example10_BulkCreateReminders(AppCompatActivity activity) {
        // 商品列表
        String[][] products = {
                {"牛奶", "常温牛奶 1L", "7"},
                {"酸奶", "活性酸奶 500ml", "14"},
                {"芝士", "进口芝士 200g", "30"},
                {"黄油", "黄油 250g", "60"}
        };

        for (String[] product : products) {
            String productName = product[0];
            String description = product[1];
            int shelfLife = Integer.parseInt(product[2]);

            // 根据保质期创建提醒
            long startTime = CalendarUtils.createFutureDateTime(shelfLife - 1, 10, 0);
            long endTime = CalendarUtils.createFutureDateTime(shelfLife - 1, 11, 0);

            long eventId = CalendarUtils.addEventWithReminder(
                    activity,
                    productName + "保质期提醒",
                    "商品：" + description + "\n保质期：" + shelfLife + " 天",
                    startTime,
                    endTime,
                    120  // 提前 2 小时提醒
            );

            if (eventId != -1) {
                System.out.println("已为 " + productName + " 创建提醒");
            }
        }
    }
}
