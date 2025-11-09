# 快速开始指南

## 5 分钟快速上手

### 步骤 1: 在你的 Activity 中请求权限

```java
public class MyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 请求日历权限
        CalendarUtils.requestCalendarPermissions(this);
    }
}
```

### 步骤 2: 创建事件和提醒

```java
// 创建一个明天 09:00-10:00 的提醒，提前 60 分钟通知
long eventId = CalendarUtils.addEventWithReminder(
    this,
    "牛奶保质期提醒",           // 标题
    "需要及时消费",              // 描述
    CalendarUtils.createFutureDateTime(1, 9, 0),   // 明天 09:00
    CalendarUtils.createFutureDateTime(1, 10, 0),  // 明天 10:00
    60  // 提前 60 分钟提醒
);

if (eventId != -1) {
    Toast.makeText(this, "事件创建成功！", Toast.LENGTH_SHORT).show();
} else {
    Toast.makeText(this, "事件创建失败", Toast.LENGTH_SHORT).show();
}
```

完成！就这么简单！

---

## 项目集成

### 1. 添加到你的项目

将 `CalendarUtils.java` 文件复制到你的项目的相同路径：
```
your_project/app/src/main/java/com/example/preservationmanager/utils/CalendarUtils.java
```

### 2. 在 AndroidManifest.xml 中添加权限

```xml
<uses-permission android:name="android.permission.READ_CALENDAR" />
<uses-permission android:name="android.permission.WRITE_CALENDAR" />
```

### 3. 在你的代码中使用

```java
import com.example.preservationmanager.utils.CalendarUtils;

// 请求权限
CalendarUtils.requestCalendarPermissions(this);

// 创建事件
long eventId = CalendarUtils.addEventWithReminder(
    context,
    "事件标题",
    "事件描述",
    CalendarUtils.createFutureDateTime(1, 10, 0),
    CalendarUtils.createFutureDateTime(1, 11, 0),
    60
);
```

---

## 常见用法

### 创建一个简单的提醒

```java
CalendarUtils.addEventWithReminder(
    context,
    "午餐提醒",
    "该吃饭了",
    CalendarUtils.createFutureDateTime(0, 12, 0),  // 今天 12:00
    CalendarUtils.createFutureDateTime(0, 12, 30), // 今天 12:30
    0  // 准时提醒
);
```

### 为 30 天后创建提醒

```java
CalendarUtils.addEventWithReminder(
    context,
    "牛奶保质期提醒",
    "商品：常温牛奶\n保质期：30天",
    CalendarUtils.createFutureDateTime(30, 9, 0),  // 30 天后 09:00
    CalendarUtils.createFutureDateTime(30, 10, 0), // 30 天后 10:00
    120  // 提前 2 小时提醒
);
```

### 创建一个带有多个提醒的事件

```java
// 先创建事件
long eventId = CalendarUtils.addEventWithReminder(
    context,
    "重要会议",
    "团队会议",
    CalendarUtils.createFutureDateTime(3, 14, 0),
    CalendarUtils.createFutureDateTime(3, 15, 0),
    1440  // 提前 1 天提醒
);

if (eventId != -1) {
    // 添加第二个提醒（提前 2 小时）
    CalendarUtils.addReminder(context, eventId, 120);
    
    // 添加第三个提醒（提前 15 分钟）
    CalendarUtils.addReminder(context, eventId, 15);
}
```

### 获取所有文件

项目已包含以下文件：
- ✅ CalendarUtils.java - 核心工具类
- ✅ MainActivity.java - 使用示例
- ✅ ExampleUsage.java - 10 个详细示例
- ✅ AndroidManifest.xml - 权限声明
- ✅ README.md - 项目说明
- ✅ USAGE_GUIDE.md - 详细使用指南
- ✅ API_REFERENCE.md - API 参考文档
- ✅ PROJECT_STRUCTURE.md - 项目结构说明

---

## 重要提示

### ⚠️ 权限必须在运行时申请

在 Android 6.0+ 上，日历权限需要运行时申请：

```java
// 在 Activity 的 onCreate 中调用
CalendarUtils.requestCalendarPermissions(this);

// 处理权限申请结果
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    // 权限申请结果会自动处理
}
```

### ⚠️ 在后台线程中操作

为了避免 ANR，应该在后台线程中创建事件：

```java
new Thread(() -> {
    long eventId = CalendarUtils.addEventWithReminder(...);
    
    // 返回主线程处理结果
    runOnUiThread(() -> {
        if (eventId != -1) {
            Toast.makeText(MyActivity.this, "成功", Toast.LENGTH_SHORT).show();
        }
    });
}).start();
```

### ⚠️ 需要有日历账户

设备上必须至少有一个日历账户（Google 账户或本地账户），CalendarUtils 会自动查找可用的账户。

---

## 支持的时间范围

| 类型 | 示例 | 参数 |
|------|------|------|
| 今天 | 今天 12:00 | `CalendarUtils.createFutureDateTime(0, 12, 0)` |
| 明天 | 明天 09:00 | `CalendarUtils.createFutureDateTime(1, 9, 0)` |
| 3 天后 | 3 天后 14:00 | `CalendarUtils.createFutureDateTime(3, 14, 0)` |
| 1 周后 | 1 周后 10:00 | `CalendarUtils.createFutureDateTime(7, 10, 0)` |
| 1 个月后 | 1 个月后 09:00 | `CalendarUtils.createFutureDateTime(30, 9, 0)` |
| 指定日期 | 2024-12-25 10:00 | `CalendarUtils.createDateTime(2024, 11, 25, 10, 0)` |

> 注意：`createDateTime()` 的月份参数是 0-11，其中 0 表示 1 月，11 表示 12 月

---

## 提醒时间表

| 提醒类型 | 分钟数 | 说明 |
|---------|-------|------|
| 准时 | 0 | 事件开始时提醒 |
| 提前 15 分钟 | 15 | 事件开始前 15 分钟 |
| 提前 30 分钟 | 30 | 事件开始前 30 分钟 |
| 提前 1 小时 | 60 | 事件开始前 1 小时 |
| 提前 2 小时 | 120 | 事件开始前 2 小时 |
| 提前 1 天 | 1440 | 事件开始前 1 天 |

---

## 返回值含义

| 方法 | 成功返回 | 失败返回 |
|------|--------|--------|
| `addEventWithReminder()` | 事件 ID (> 0) | -1 |
| `hasCalendarPermissions()` | true | false |
| `deleteEvent()` | true | false |
| `updateEvent()` | true | false |

---

## 调试技巧

### 检查是否有权限

```java
if (CalendarUtils.hasCalendarPermissions(context)) {
    Log.d("Calendar", "权限已授予");
} else {
    Log.d("Calendar", "权限未授予");
}
```

### 创建事件时检查返回值

```java
long eventId = CalendarUtils.addEventWithReminder(...);
if (eventId == -1) {
    Log.e("Calendar", "创建事件失败");
    // 可能的原因：
    // 1. 权限未授予
    // 2. 没有可用的日历账户
    // 3. ContentResolver 异常
} else {
    Log.d("Calendar", "创建事件成功，ID: " + eventId);
}
```

### 在日历应用中查看事件

1. 打开设备上的日历应用
2. 查看相应的日期
3. 点击事件查看详情
4. 检查提醒是否设置正确

---

## 常见错误和解决方案

### 错误 1: `eventId == -1`

**原因**：
- 权限未授予
- 没有日历账户
- ContentResolver 错误

**解决**：
```java
// 检查权限
if (!CalendarUtils.hasCalendarPermissions(context)) {
    CalendarUtils.requestCalendarPermissions(activity);
    return;
}

// 检查日历账户
long calendarId = CalendarUtils.getOrCreateCalendarAccount(context);
if (calendarId == -1) {
    Log.e("Calendar", "无法获取或创建日历账户");
    return;
}
```

### 错误 2: 事件创建后不显示

**原因**：
- 日历应用需要刷新
- 事件时间设置有误

**解决**：
- 重新打开日历应用
- 检查事件时间是否正确

### 错误 3: 提醒没有触发

**原因**：
- 提醒时间已过期
- 日历应用通知被禁用
- 设备静音

**解决**：
- 检查系统日期和时间
- 检查日历应用的通知设置
- 检查设备音量

---

## 下一步

1. **阅读详细文档**
   - 查看 [README.md](README.md) 了解项目概况
   - 查看 [USAGE_GUIDE.md](USAGE_GUIDE.md) 了解详细用法
   - 查看 [API_REFERENCE.md](API_REFERENCE.md) 了解 API 详情

2. **查看示例代码**
   - MainActivity.java - 基础示例
   - ExampleUsage.java - 10 个详细示例

3. **运行测试**
   ```bash
   ./gradlew test                    # 单元测试
   ./gradlew connectedAndroidTest    # 集成测试
   ```

4. **集成到你的项目**
   - 复制 CalendarUtils.java
   - 添加权限声明
   - 在 Activity 中调用

---

## 获取帮助

- 查看 README.md 中的 FAQ 部分
- 查看 USAGE_GUIDE.md 中的常见场景
- 查看 API_REFERENCE.md 了解方法详情
- 检查示例代码

---

## 版本信息

- **版本**：1.0
- **最小 API**：21（Android 5.0）
- **编译 API**：34（Android 14）
- **目标 API**：34（Android 14）

---

祝你使用愉快！🎉
