# 商品保质期管家 - 日历事件提醒功能

## 项目概述

这是一个 Android 应用，用于在系统日历中创建商品保质期提醒事件。主要功能包括：
- 检查和请求日历读写权限
- 获取或创建日历账户
- 在系统日历中插入事件
- 为事件添加提醒

## 项目结构

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/preservationmanager/
│   │   │   ├── MainActivity.java          # 主 Activity（示例）
│   │   │   └── utils/
│   │   │       └── CalendarUtils.java     # 日历工具类（核心）
│   │   ├── res/
│   │   │   └── layout/
│   │   │       └── activity_main.xml      # 主 Activity 布局
│   │   └── AndroidManifest.xml            # Android 清单文件
│   └── test/                              # 测试文件
├── build.gradle                           # 模块级构建配置
├── README.md                              # 本文件
└── proguard-rules.pro                     # ProGuard 混淆规则
```

## CalendarUtils 工具类说明

### 主要方法

#### 1. 权限检查和请求

```java
// 检查是否已获得日历权限
boolean hasPermission = CalendarUtils.hasCalendarPermissions(context);

// 请求日历权限（在 Activity 中调用）
CalendarUtils.requestCalendarPermissions(activity);
```

#### 2. 获取或创建日历账户

```java
// 获取主日历 ID，如果不存在则创建本地日历
long calendarId = CalendarUtils.getOrCreateCalendarAccount(context);
```

#### 3. 插入事件

```java
long eventId = CalendarUtils.insertEvent(
    context,
    calendarId,
    "牛奶保质期提醒",           // 标题
    "商品：牛奶\n数量：1 瓶",   // 描述
    startTimeMillis,             // 开始时间（毫秒）
    endTimeMillis,               // 结束时间（毫秒）
    "Asia/Shanghai"              // 时区
);
```

#### 4. 添加提醒

```java
long reminderId = CalendarUtils.addReminder(
    context,
    eventId,
    60  // 提前 60 分钟提醒
);
```

#### 5. 完整创建事件和提醒（推荐使用）

```java
// 方式一：使用默认时区（设备时区）
long eventId = CalendarUtils.addEventWithReminder(
    this,
    "牛奶保质期提醒",           // 标题
    "商品：牛奶\n数量：1 瓶",   // 描述
    startTime,                   // 开始时间（毫秒）
    endTime,                     // 结束时间（毫秒）
    60                           // 提醒时间（分钟）
);

// 方式二：指定时区
long eventId = CalendarUtils.addEventWithReminder(
    this,
    "牛奶保质期提醒",
    "商品：牛奶\n数量：1 瓶",
    startTime,
    endTime,
    60,
    "Asia/Shanghai"
);
```

#### 6. 时间工具方法

```java
// 创建指定日期和时间
long time = CalendarUtils.createDateTime(2024, 11, 15, 14, 30);  // 2024-11-15 14:30

// 创建未来某天的时间
long futureTime = CalendarUtils.createFutureDateTime(3, 9, 0);  // 3 天后的 09:00
```

#### 7. 其他操作

```java
// 删除事件
boolean deleted = CalendarUtils.deleteEvent(context, eventId);

// 更新事件
boolean updated = CalendarUtils.updateEvent(
    context,
    eventId,
    "新标题",
    "新描述",
    newStartTime,
    newEndTime
);

// 获取事件详情
ContentValues eventDetails = CalendarUtils.getEventDetails(context, eventId);
```

## 使用示例

### 完整流程示例（推荐）

```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. 请求权限
        CalendarUtils.requestCalendarPermissions(this);

        Button createButton = findViewById(R.id.create_event_button);
        createButton.setOnClickListener(v -> createEvent());
    }

    private void createEvent() {
        // 2. 检查权限
        if (!CalendarUtils.hasCalendarPermissions(this)) {
            Toast.makeText(this, "请授予日历权限", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. 设置事件参数
        String title = "牛奶保质期提醒";
        String description = "商品：牛奶\n数量：1 瓶\n过期时间：2024-11-20 00:00";
        
        // 明天 09:00 到 10:00
        long startTime = CalendarUtils.createFutureDateTime(1, 9, 0);
        long endTime = CalendarUtils.createFutureDateTime(1, 10, 0);
        
        // 提前 60 分钟提醒
        int reminderMinutes = 60;

        // 4. 创建事件和提醒
        long eventId = CalendarUtils.addEventWithReminder(
            this,
            title,
            description,
            startTime,
            endTime,
            reminderMinutes
        );

        // 5. 处理结果
        if (eventId != -1) {
            Toast.makeText(this, "事件创建成功！ID: " + eventId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "事件创建失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == 1001) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            
            if (allGranted) {
                Toast.makeText(this, "权限已授予", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
```

## 权限声明

在 `AndroidManifest.xml` 中已添加以下权限：

```xml
<uses-permission android:name="android.permission.READ_CALENDAR" />
<uses-permission android:name="android.permission.WRITE_CALENDAR" />
```

**注意**：这两个权限在 Android 6.0+ 需要运行时申请，CalendarUtils 提供了自动申请的方法。

## 支持的 Android 版本

- **最低版本**：Android 6.0（API Level 21）
- **编译版本**：Android 14（API Level 34）
- **目标版本**：Android 14（API Level 34）

## 依赖库

```gradle
dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
}
```

## 关键技术点

### 1. 权限申请

- 使用 `ContextCompat.checkSelfPermission()` 检查权限
- 使用 `ActivityCompat.requestPermissions()` 请求权限
- 处理权限申请结果

### 2. 日历操作

- 使用 `CalendarContract.Calendars` 访问日历
- 使用 `CalendarContract.Events` 管理事件
- 使用 `CalendarContract.Reminders` 管理提醒

### 3. ContentResolver

- 通过 `ContentResolver` 插入、查询、更新、删除日历数据
- 正确处理 URI 和 Cursor

### 4. 时区处理

- 默认使用设备时区
- 支持自定义时区（如 "Asia/Shanghai"）

## 常见问题

### Q1: 为什么事件创建失败？

**可能原因**：
1. 权限未授予 - 需要在设置中手动授予权限
2. 没有可用的日历账户 - 需要在设备上至少有一个日历账户（Google 或本地）
3. ContentResolver 操作异常

**解决方案**：
```java
if (!CalendarUtils.hasCalendarPermissions(context)) {
    CalendarUtils.requestCalendarPermissions(activity);
}
```

### Q2: 如何修改提醒时间？

需要先删除原有提醒，然后重新添加：
```java
// 删除事件（会级联删除相关提醒）
CalendarUtils.deleteEvent(context, eventId);

// 重新创建
long newEventId = CalendarUtils.addEventWithReminder(
    context, title, description, startTime, endTime, 120);  // 改为 120 分钟
```

### Q3: 如何在后台线程中操作？

由于 ContentResolver 操作可能较耗时，建议在后台线程中执行：
```java
Thread thread = new Thread(() -> {
    long eventId = CalendarUtils.addEventWithReminder(
        context, title, description, startTime, endTime, 60);
    // 处理结果
});
thread.start();
```

或使用 AsyncTask：
```java
new AsyncTask<Void, Void, Long>() {
    @Override
    protected Long doInBackground(Void... voids) {
        return CalendarUtils.addEventWithReminder(
            context, title, description, startTime, endTime, 60);
    }

    @Override
    protected void onPostExecute(Long eventId) {
        if (eventId != -1) {
            // 成功
        }
    }
}.execute();
```

## 测试建议

1. **权限测试**：在不同 Android 版本上测试权限申请流程
2. **日历账户测试**：在有/无日历账户的设备上测试
3. **事件创建测试**：验证事件是否正确显示在系统日历中
4. **提醒测试**：验证提醒是否在指定时间触发
5. **并发测试**：测试同时创建多个事件的情况

## 许可证

MIT License

## 联系方式

如有问题或建议，请联系开发团队。
