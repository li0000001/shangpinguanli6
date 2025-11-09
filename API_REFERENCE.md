# CalendarUtils API 参考

## 类名
`com.example.preservationmanager.utils.CalendarUtils`

## 静态方法

### 权限相关方法

#### 1. hasCalendarPermissions
```java
public static boolean hasCalendarPermissions(Context context)
```

**描述**：检查应用是否已获得日历读写权限

**参数**：
- `context` (Context) - 应用上下文

**返回值**：
- `true` - 已获得权限
- `false` - 未获得权限

**异常**：无

**示例**：
```java
if (CalendarUtils.hasCalendarPermissions(context)) {
    // 权限已授予，可以操作日历
} else {
    // 需要请求权限
    CalendarUtils.requestCalendarPermissions(activity);
}
```

---

#### 2. requestCalendarPermissions
```java
public static void requestCalendarPermissions(androidx.appcompat.app.AppCompatActivity activity)
```

**描述**：请求日历读写权限（Android 6.0+）

**参数**：
- `activity` (AppCompatActivity) - Activity 实例

**返回值**：无

**异常**：无

**注意**：
- 仅适用于 Android 6.0+
- 请在 `onRequestPermissionsResult()` 中处理权限申请结果
- 必须在 AndroidManifest.xml 中声明权限

**示例**：
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    CalendarUtils.requestCalendarPermissions(this);
}

@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    // 处理权限申请结果
}
```

---

### 日历账户相关方法

#### 3. getOrCreateCalendarAccount
```java
public static long getOrCreateCalendarAccount(Context context)
```

**描述**：获取可用的日历账户，如果没有则创建一个本地日历账户

**参数**：
- `context` (Context) - 应用上下文

**返回值**：
- 日历 ID (long > 0) - 成功获取或创建
- `-1` - 失败

**异常**：无

**查询顺序**：
1. 主日历（IS_PRIMARY = 1）
2. 任何可见的日历
3. 创建本地日历

**权限要求**：READ_CALENDAR, WRITE_CALENDAR

**示例**：
```java
long calendarId = CalendarUtils.getOrCreateCalendarAccount(context);
if (calendarId == -1) {
    Log.e("Calendar", "无法获取或创建日历账户");
} else {
    Log.d("Calendar", "日历 ID: " + calendarId);
}
```

---

### 事件操作方法

#### 4. insertEvent
```java
public static long insertEvent(Context context, long calendarId, String title, 
                              String description, long startTime, long endTime, 
                              String timeZone)
```

**描述**：在指定的日历中插入一个事件

**参数**：
- `context` (Context) - 应用上下文
- `calendarId` (long) - 日历 ID
- `title` (String) - 事件标题
- `description` (String) - 事件描述（可以为 null）
- `startTime` (long) - 开始时间（毫秒时间戳）
- `endTime` (long) - 结束时间（毫秒时间戳）
- `timeZone` (String) - 时区（例如 "Asia/Shanghai"，"UTC" 等）

**返回值**：
- 事件 ID (long > 0) - 创建成功
- `-1` - 创建失败

**异常**：无

**权限要求**：WRITE_CALENDAR

**示例**：
```java
long startTime = CalendarUtils.createFutureDateTime(1, 9, 0);
long endTime = CalendarUtils.createFutureDateTime(1, 10, 0);

long eventId = CalendarUtils.insertEvent(
    context,
    calendarId,
    "会议",
    "团队周会",
    startTime,
    endTime,
    "Asia/Shanghai"
);
```

---

#### 5. addReminder
```java
public static long addReminder(Context context, long eventId, int minutesBefore)
```

**描述**：为事件添加提醒

**参数**：
- `context` (Context) - 应用上下文
- `eventId` (long) - 事件 ID
- `minutesBefore` (int) - 提前提醒的分钟数

**返回值**：
- 提醒 ID (long > 0) - 添加成功
- `-1` - 添加失败

**异常**：无

**权限要求**：WRITE_CALENDAR

**提醒方式**：METHOD_ALERT（系统通知）

**示例**：
```java
// 提前 60 分钟提醒
long reminderId = CalendarUtils.addReminder(context, eventId, 60);

// 提前 1 天提醒
long reminderId = CalendarUtils.addReminder(context, eventId, 1440);

// 提前 30 分钟提醒
long reminderId = CalendarUtils.addReminder(context, eventId, 30);
```

---

#### 6. addEventWithReminder
```java
public static long addEventWithReminder(Context context, String title, String description,
                                        long startTime, long endTime, int reminderMinutesBefore)
```

**描述**：创建事件并添加提醒（使用默认时区）

**参数**：
- `context` (Context) - 应用上下文
- `title` (String) - 事件标题
- `description` (String) - 事件描述
- `startTime` (long) - 开始时间（毫秒时间戳）
- `endTime` (long) - 结束时间（毫秒时间戳）
- `reminderMinutesBefore` (int) - 提前提醒的分钟数

**返回值**：
- 事件 ID (long > 0) - 成功
- `-1` - 失败

**异常**：无

**权限要求**：READ_CALENDAR, WRITE_CALENDAR

**时区**：使用设备默认时区

**推荐使用**：✓ 推荐

**示例**：
```java
long eventId = CalendarUtils.addEventWithReminder(
    this,
    "牛奶过期提醒",
    "商品：牛奶\n购买日期：2024-11-01",
    CalendarUtils.createFutureDateTime(30, 9, 0),
    CalendarUtils.createFutureDateTime(30, 10, 0),
    120  // 提前 2 小时
);
```

---

#### 7. addEventWithReminder（带时区）
```java
public static long addEventWithReminder(Context context, String title, String description,
                                        long startTime, long endTime, int reminderMinutesBefore,
                                        String timeZone)
```

**描述**：创建事件并添加提醒（可指定时区）

**参数**：
- `context` (Context) - 应用上下文
- `title` (String) - 事件标题
- `description` (String) - 事件描述
- `startTime` (long) - 开始时间（毫秒时间戳）
- `endTime` (long) - 结束时间（毫秒时间戳）
- `reminderMinutesBefore` (int) - 提前提醒的分钟数
- `timeZone` (String) - 时区

**返回值**：
- 事件 ID (long > 0) - 成功
- `-1` - 失败

**异常**：无

**权限要求**：READ_CALENDAR, WRITE_CALENDAR

**常用时区**：
- `"UTC"` - 协调世界时
- `"Asia/Shanghai"` - 中国标准时
- `"America/New_York"` - 美国东部时间
- `"Europe/London"` - 英国时间

**推荐使用**：✓ 推荐

**示例**：
```java
long eventId = CalendarUtils.addEventWithReminder(
    context,
    "会议提醒",
    "国际会议",
    CalendarUtils.createFutureDateTime(1, 14, 0),
    CalendarUtils.createFutureDateTime(1, 15, 0),
    60,
    "Asia/Shanghai"
);
```

---

### 时间工具方法

#### 8. createDateTime
```java
public static long createDateTime(int year, int month, int day, int hour, int minute)
```

**描述**：创建指定日期时间的时间戳

**参数**：
- `year` (int) - 年份（例如 2024）
- `month` (int) - 月份（0-11，其中 0 表示 1 月）
- `day` (int) - 日期（1-31）
- `hour` (int) - 小时（0-23）
- `minute` (int) - 分钟（0-59）

**返回值**：
- 时间戳 (long) - 毫秒

**异常**：无

**注意**：月份参数是 0-11，不是 1-12

**示例**：
```java
// 创建 2024 年 12 月 25 日 10:30
long time = CalendarUtils.createDateTime(2024, 11, 25, 10, 30);

// 创建 2024 年 1 月 1 日 00:00
long time = CalendarUtils.createDateTime(2024, 0, 1, 0, 0);

// 创建 2024 年 6 月 15 日 14:00
long time = CalendarUtils.createDateTime(2024, 5, 15, 14, 0);
```

---

#### 9. createFutureDateTime
```java
public static long createFutureDateTime(int daysFromNow, int hour, int minute)
```

**描述**：创建未来某天指定时间的时间戳

**参数**：
- `daysFromNow` (int) - 距今天数（负数表示过去的日期）
- `hour` (int) - 小时（0-23）
- `minute` (int) - 分钟（0-59）

**返回值**：
- 时间戳 (long) - 毫秒

**异常**：无

**示例**：
```java
// 明天 09:00
long tomorrow = CalendarUtils.createFutureDateTime(1, 9, 0);

// 3 天后 14:30
long in3Days = CalendarUtils.createFutureDateTime(3, 14, 30);

// 1 周后 10:00
long nextWeek = CalendarUtils.createFutureDateTime(7, 10, 0);

// 1 个月后 15:00
long nextMonth = CalendarUtils.createFutureDateTime(30, 15, 0);

// 昨天 09:00
long yesterday = CalendarUtils.createFutureDateTime(-1, 9, 0);

// 7 天前 10:00
long lastWeek = CalendarUtils.createFutureDateTime(-7, 10, 0);

// 今天 12:00
long today = CalendarUtils.createFutureDateTime(0, 12, 0);
```

---

### 事件管理方法

#### 10. updateEvent
```java
public static boolean updateEvent(Context context, long eventId, String title, 
                                  String description, long startTime, long endTime)
```

**描述**：更新已存在的日历事件

**参数**：
- `context` (Context) - 应用上下文
- `eventId` (long) - 事件 ID
- `title` (String) - 新的事件标题
- `description` (String) - 新的事件描述
- `startTime` (long) - 新的开始时间（毫秒时间戳）
- `endTime` (long) - 新的结束时间（毫秒时间戳）

**返回值**：
- `true` - 更新成功
- `false` - 更新失败

**异常**：无

**权限要求**：WRITE_CALENDAR

**示例**：
```java
boolean updated = CalendarUtils.updateEvent(
    context,
    eventId,
    "新标题",
    "新描述",
    CalendarUtils.createFutureDateTime(2, 10, 0),
    CalendarUtils.createFutureDateTime(2, 11, 0)
);

if (updated) {
    Toast.makeText(context, "事件已更新", Toast.LENGTH_SHORT).show();
}
```

---

#### 11. deleteEvent
```java
public static boolean deleteEvent(Context context, long eventId)
```

**描述**：删除指定的日历事件

**参数**：
- `context` (Context) - 应用上下文
- `eventId` (long) - 事件 ID

**返回值**：
- `true` - 删除成功
- `false` - 删除失败

**异常**：无

**权限要求**：WRITE_CALENDAR

**级联删除**：删除事件时会同时删除相关的提醒

**示例**：
```java
boolean deleted = CalendarUtils.deleteEvent(context, eventId);

if (deleted) {
    Toast.makeText(context, "事件已删除", Toast.LENGTH_SHORT).show();
} else {
    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
}
```

---

#### 12. getEventDetails
```java
public static ContentValues getEventDetails(Context context, long eventId)
```

**描述**：获取指定事件的详细信息

**参数**：
- `context` (Context) - 应用上下文
- `eventId` (long) - 事件 ID

**返回值**：
- ContentValues - 包含事件信息，或 null（获取失败）

**异常**：无

**权限要求**：READ_CALENDAR

**返回的字段**：
- `TITLE` - 事件标题 (String)
- `DESCRIPTION` - 事件描述 (String)
- `DTSTART` - 开始时间 (long)
- `DTEND` - 结束时间 (long)
- `EVENT_TIMEZONE` - 时区 (String)

**示例**：
```java
ContentValues details = CalendarUtils.getEventDetails(context, eventId);

if (details != null) {
    String title = details.getAsString(CalendarContract.Events.TITLE);
    String description = details.getAsString(CalendarContract.Events.DESCRIPTION);
    long startTime = details.getAsLong(CalendarContract.Events.DTSTART);
    long endTime = details.getAsLong(CalendarContract.Events.DTEND);
    String timezone = details.getAsString(CalendarContract.Events.EVENT_TIMEZONE);
    
    Log.d("Event", "Title: " + title);
    Log.d("Event", "Description: " + description);
    Log.d("Event", "Start: " + new Date(startTime));
    Log.d("Event", "End: " + new Date(endTime));
}
```

---

## 常量

### 权限请求代码
```java
private static final int PERMISSION_REQUEST_CODE = 1001;
```

### 权限数组
```java
private static final String[] CALENDAR_PERMISSIONS = {
    Manifest.permission.READ_CALENDAR,
    Manifest.permission.WRITE_CALENDAR
};
```

---

## 返回值说明

| 方法 | 成功返回值 | 失败返回值 |
|------|----------|----------|
| `hasCalendarPermissions()` | `true` | `false` |
| `getOrCreateCalendarAccount()` | `> 0` | `-1` |
| `insertEvent()` | `> 0` | `-1` |
| `addReminder()` | `> 0` | `-1` |
| `addEventWithReminder()` | `> 0` | `-1` |
| `updateEvent()` | `true` | `false` |
| `deleteEvent()` | `true` | `false` |
| `getEventDetails()` | `ContentValues` | `null` |

---

## 异常处理

所有方法都不抛出异常，而是通过返回值来表示成功或失败。建议始终检查返回值：

```java
long eventId = CalendarUtils.addEventWithReminder(...);
if (eventId == -1) {
    // 处理失败情况
} else {
    // 处理成功情况
}
```

---

## 线程安全性

- **非线程安全**：CalendarUtils 的方法不是线程安全的
- **建议**：在后台线程中执行 ContentResolver 操作
- **UI 线程**：不建议在 UI 线程中执行这些操作

```java
new Thread(() -> {
    long eventId = CalendarUtils.addEventWithReminder(...);
    // 如需更新 UI，使用 runOnUiThread()
}).start();
```

---

## 权限要求

| 操作 | 权限 | 最小 API |
|------|------|--------|
| 检查权限 | - | 23 |
| 读取日历 | READ_CALENDAR | 6 |
| 创建事件 | WRITE_CALENDAR | 6 |
| 删除事件 | WRITE_CALENDAR | 6 |
| 编辑事件 | WRITE_CALENDAR | 6 |

---

## 支持的时区

CalendarUtils 支持所有 Java TimeZone 支持的时区。常见时区：

```
UTC                 - 协调世界时
GMT                 - 格林威治标准时
Asia/Shanghai       - 中国标准时
Asia/Tokyo          - 日本时间
Asia/Hong_Kong      - 香港时间
Asia/Bangkok        - 泰国时间
Asia/Singapore      - 新加坡时间
America/New_York    - 美国东部时间
America/Chicago     - 美国中部时间
America/Denver      - 美国山地时间
America/Los_Angeles - 美国太平洋时间
Europe/London       - 英国时间
Europe/Paris        - 法国时间
Europe/Berlin       - 德国时间
Australia/Sydney    - 澳大利亚东部时间
```

---

## 版本历史

- **v1.0** (2024-11-01) - 初始发布
  - 基础事件和提醒创建
  - 权限管理
  - 事件查询、更新、删除

---

## 相关类

- `android.provider.CalendarContract` - Android 日历提供程序合约
- `android.provider.CalendarContract.Events` - 事件操作接口
- `android.provider.CalendarContract.Reminders` - 提醒操作接口
- `android.provider.CalendarContract.Calendars` - 日历账户操作接口

---

## 参考资源

- [Android 官方文档 - CalendarContract](https://developer.android.google.cn/reference/android/provider/CalendarContract)
- [Android 权限系统](https://developer.android.google.cn/guide/topics/permissions/overview)
- [Android 运行时权限申请](https://developer.android.google.cn/training/permissions/requesting)
