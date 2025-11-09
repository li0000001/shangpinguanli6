# CalendarUtils 使用指南

## 快速开始

### 1. 基础设置

首先，在你的 Activity 中请求日历权限：

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 请求日历权限
        CalendarUtils.requestCalendarPermissions(this);
    }
}
```

### 2. 最简单的使用方法

```java
// 在任何地方创建一个日历事件
long startTime = CalendarUtils.createFutureDateTime(1, 9, 0);  // 明天 09:00
long endTime = CalendarUtils.createFutureDateTime(1, 10, 0);   // 明天 10:00

CalendarUtils.addEventWithReminder(
    context,
    "牛奶保质期提醒",
    "需要及时消费",
    startTime,
    endTime,
    60  // 提前 60 分钟提醒
);
```

## 常见场景

### 场景 1: 简单的保质期提醒

需求：为一瓶牛奶创建一个保质期提醒

```java
public void remindMilkExpiry(Context context) {
    // 计算过期时间：30 天后的 09:00
    long expiryTime = CalendarUtils.createFutureDateTime(30, 9, 0);
    
    CalendarUtils.addEventWithReminder(
        context,
        "牛奶即将过期",
        "购买日期：2024-11-01\n" +
        "保质期：30天\n" +
        "存放地：冷藏柜",
        expiryTime,
        CalendarUtils.createFutureDateTime(30, 10, 0),
        120  // 提前 2 小时提醒
    );
}
```

### 场景 2: 批量创建商品提醒

需求：为多个商品一次性创建提醒

```java
public void createMultipleReminders(Context context) {
    // 商品数据：{名称, 保质期(天)}
    String[][] products = {
        {"牛奶", "7"},
        {"酸奶", "14"},
        {"奶酪", "30"},
        {"黄油", "60"}
    };
    
    for (String[] product : products) {
        int shelfDays = Integer.parseInt(product[1]);
        long reminderTime = CalendarUtils.createFutureDateTime(shelfDays - 1, 9, 0);
        
        CalendarUtils.addEventWithReminder(
            context,
            product[0] + "保质期提醒",
            "商品：" + product[0] + "\n保质期：" + shelfDays + " 天",
            reminderTime,
            CalendarUtils.createFutureDateTime(shelfDays - 1, 10, 0),
            120
        );
    }
}
```

### 场景 3: 多个提醒时间

需求：对同一个商品设置多个提醒时间

```java
public void createMultipleRemindersForProduct(Context context, String product, int expiryDays) {
    long expiryTime = CalendarUtils.createFutureDateTime(expiryDays, 9, 0);
    
    // 创建事件（包含第一个提醒）
    long eventId = CalendarUtils.addEventWithReminder(
        context,
        product + "保质期提醒",
        "商品：" + product,
        expiryTime,
        CalendarUtils.createFutureDateTime(expiryDays, 10, 0),
        1440  // 提前 1 天提醒
    );
    
    if (eventId != -1) {
        // 添加第二个提醒（提前 1 小时）
        CalendarUtils.addReminder(context, eventId, 60);
        
        // 添加第三个提醒（提前 30 分钟）
        CalendarUtils.addReminder(context, eventId, 30);
    }
}
```

### 场景 4: 指定时区

需求：在不同的时区创建事件

```java
public void createEventInShanghai(Context context) {
    CalendarUtils.addEventWithReminder(
        context,
        "上海时区提醒",
        "这个事件使用上海时区",
        CalendarUtils.createFutureDateTime(5, 14, 0),
        CalendarUtils.createFutureDateTime(5, 15, 0),
        90,
        "Asia/Shanghai"
    );
}
```

### 场景 5: 具体日期和时间

需求：为特定的日期创建提醒

```java
public void createSpecificDateReminder(Context context) {
    // 创建 2024 年 12 月 25 日 10:00 的提醒
    long time = CalendarUtils.createDateTime(2024, 11, 25, 10, 0);  // 注意：月份是 0-11
    
    CalendarUtils.addEventWithReminder(
        context,
        "圣诞节促销提醒",
        "促销即将结束",
        time,
        CalendarUtils.createDateTime(2024, 11, 25, 11, 0),
        240  // 提前 4 小时
    );
}
```

### 场景 6: 权限检查和处理

需求：完整的权限检查和错误处理

```java
public void createEventWithPermissionCheck(AppCompatActivity activity) {
    // 检查权限
    if (!CalendarUtils.hasCalendarPermissions(activity)) {
        // 权限未授予，请求权限
        CalendarUtils.requestCalendarPermissions(activity);
        Toast.makeText(activity, "请授予日历权限", Toast.LENGTH_SHORT).show();
        return;
    }
    
    // 权限已授予，创建事件
    long eventId = CalendarUtils.addEventWithReminder(
        activity,
        "事件标题",
        "事件描述",
        CalendarUtils.createFutureDateTime(1, 10, 0),
        CalendarUtils.createFutureDateTime(1, 11, 0),
        60
    );
    
    if (eventId != -1) {
        Toast.makeText(activity, "事件创建成功", Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(activity, "事件创建失败", Toast.LENGTH_SHORT).show();
    }
}
```

### 场景 7: 后台线程操作

需求：在后台线程中创建事件，避免 ANR

```java
public void createEventInBackground(AppCompatActivity activity) {
    new Thread(() -> {
        long eventId = CalendarUtils.addEventWithReminder(
            activity,
            "后台创建的事件",
            "在后台线程中创建",
            CalendarUtils.createFutureDateTime(2, 10, 0),
            CalendarUtils.createFutureDateTime(2, 11, 0),
            60
        );
        
        // 返回主线程处理结果
        activity.runOnUiThread(() -> {
            if (eventId != -1) {
                Toast.makeText(activity, "事件创建成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "事件创建失败", Toast.LENGTH_SHORT).show();
            }
        });
    }).start();
}
```

### 场景 8: 修改和删除事件

需求：更新或删除已创建的事件

```java
public void updateAndDeleteEvent(Context context, long eventId) {
    // 更新事件
    boolean updated = CalendarUtils.updateEvent(
        context,
        eventId,
        "新的标题",
        "新的描述",
        CalendarUtils.createFutureDateTime(3, 14, 0),
        CalendarUtils.createFutureDateTime(3, 15, 0)
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
```

### 场景 9: 获取事件详情

需求：查看已创建事件的详细信息

```java
public void getEventInfo(Context context, long eventId) {
    android.content.ContentValues details = CalendarUtils.getEventDetails(context, eventId);
    
    if (details != null) {
        String title = details.getAsString(android.provider.CalendarContract.Events.TITLE);
        String description = details.getAsString(android.provider.CalendarContract.Events.DESCRIPTION);
        long startTime = details.getAsLong(android.provider.CalendarContract.Events.DTSTART);
        long endTime = details.getAsLong(android.provider.CalendarContract.Events.DTEND);
        
        System.out.println("标题: " + title);
        System.out.println("描述: " + description);
        System.out.println("开始: " + new Date(startTime));
        System.out.println("结束: " + new Date(endTime));
    }
}
```

### 场景 10: 定时重复提醒（使用 AlarmManager）

需求：每天在同一时间重复提醒

```java
public void setupDailyReminder(Context context, String product, int hour, int minute) {
    // 创建初始事件
    long eventId = CalendarUtils.addEventWithReminder(
        context,
        product + "每日提醒",
        "每天提醒检查商品状态",
        CalendarUtils.createFutureDateTime(0, hour, minute),
        CalendarUtils.createFutureDateTime(0, hour + 1, minute),
        30
    );
    
    if (eventId != -1) {
        // 可以结合 AlarmManager 实现重复提醒
        setupAlarm(context, hour, minute);
    }
}

private void setupAlarm(Context context, int hour, int minute) {
    // 这里使用 AlarmManager 创建定期提醒
    // 具体实现需要根据你的需求调整
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    
    Intent intent = new Intent(context, YourReminderReceiver.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, hour);
    calendar.set(Calendar.MINUTE, minute);
    calendar.set(Calendar.SECOND, 0);
    
    alarmManager.setAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.getTimeInMillis(),
        pendingIntent
    );
}
```

## 时间计算技巧

### 获取当前时间
```java
long now = System.currentTimeMillis();
```

### 获取未来某个时间
```java
// 3 天后的 10:30
long futureTime = CalendarUtils.createFutureDateTime(3, 10, 30);

// 1 周后的 09:00
long oneWeekLater = CalendarUtils.createFutureDateTime(7, 9, 0);

// 1 个月后的 14:00
long oneMonthLater = CalendarUtils.createFutureDateTime(30, 14, 0);
```

### 获取过去的时间
```java
// 1 天前的 10:00
long yesterday = CalendarUtils.createFutureDateTime(-1, 10, 0);

// 7 天前的 09:00
long oneWeekAgo = CalendarUtils.createFutureDateTime(-7, 9, 0);
```

### 指定确切日期
```java
// 2024 年 12 月 25 日 10:00
long xmasTime = CalendarUtils.createDateTime(2024, 11, 25, 10, 0);
// 注意：月份参数是 0-11，所以 12 月是 11
```

## 错误处理

### 返回值说明
- 事件创建返回值：`-1` 表示失败，其他数值为事件 ID
- 权限检查返回值：`true` 表示已授予，`false` 表示未授予
- 删除/更新返回值：`true` 表示成功，`false` 表示失败

### 常见错误处理

```java
long eventId = CalendarUtils.addEventWithReminder(
    context, title, description, startTime, endTime, 60);

if (eventId == -1) {
    // 检查权限
    if (!CalendarUtils.hasCalendarPermissions(context)) {
        // 权限问题
        Toast.makeText(context, "请授予日历权限", Toast.LENGTH_SHORT).show();
    } else {
        // 其他问题（如日历账户不存在）
        Toast.makeText(context, "创建事件失败，请检查日历设置", Toast.LENGTH_SHORT).show();
    }
} else {
    // 成功
    Toast.makeText(context, "事件创建成功", Toast.LENGTH_SHORT).show();
}
```

## 性能优化建议

1. **在后台线程中操作**
   ```java
   new Thread(() -> {
       CalendarUtils.addEventWithReminder(...);
   }).start();
   ```

2. **避免在 UI 线程中进行频繁操作**
   - 使用 AsyncTask 或 Thread 处理

3. **批量创建时的优化**
   - 分批处理大量事件
   - 不要在 UI 线程中创建超过 10 个事件

4. **权限检查的缓存**
   ```java
   boolean hasPermission = CalendarUtils.hasCalendarPermissions(context);
   if (!hasPermission) {
       // 缓存权限状态，避免频繁检查
   }
   ```

## 权限管理最佳实践

1. **在 onCreate 中请求权限**
   ```java
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       CalendarUtils.requestCalendarPermissions(this);
   }
   ```

2. **处理权限结果**
   ```java
   @Override
   public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
       super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       if (requestCode == 1001) {
           // 处理权限申请结果
       }
   }
   ```

3. **在执行操作前检查权限**
   ```java
   if (CalendarUtils.hasCalendarPermissions(context)) {
       // 执行日历操作
   }
   ```

## 调试技巧

### 检查创建的事件
1. 打开系统日历应用
2. 查看相应的日期
3. 点击事件查看详情

### 使用 Logcat 查看错误
```
adb logcat | grep CalendarUtils
```

### 验证权限
```
adb shell pm list permissions -g | grep CALENDAR
```

## FAQ

**Q: 为什么事件没有显示在日历中？**
A: 检查以下几点：
- 是否授予了权限
- 是否有可用的日历账户
- 事件时间是否设置正确

**Q: 提醒没有触发怎么办？**
A: 
- 检查提醒时间是否已过期
- 验证设备的通知设置
- 确认日历应用是否允许通知

**Q: 如何实现定期提醒？**
A: 使用 AlarmManager 或 JobScheduler 实现定期任务，再通过 CalendarUtils 创建事件。

**Q: 支持哪些提醒方式？**
A: 当前支持 METHOD_ALERT（通知）。可以手动修改代码支持其他方式如 METHOD_EMAIL、METHOD_SMS。

## 更多资源

- [Android CalendarContract 官方文档](https://developer.android.google.cn/reference/android/provider/CalendarContract)
- [Android 权限系统](https://developer.android.google.cn/guide/topics/permissions/overview)
- [Android 运行时权限](https://developer.android.google.cn/training/permissions/requesting)
