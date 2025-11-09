# 项目结构详解

## 目录树

```
PreservationManager/
├── app/                                      # 应用模块
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/preservationmanager/
│   │   │   │       ├── MainActivity.java     # 主 Activity（示例）
│   │   │   │       ├── ExampleUsage.java     # 使用示例集合
│   │   │   │       └── utils/
│   │   │   │           └── CalendarUtils.java # 核心工具类
│   │   │   ├── res/
│   │   │   │   └── layout/
│   │   │   │       └── activity_main.xml     # 主 Activity 布局
│   │   │   └── AndroidManifest.xml           # Android 应用清单
│   │   ├── test/
│   │   │   └── java/
│   │   │       └── com/example/preservationmanager/
│   │   │           └── utils/
│   │   │               └── CalendarUtilsTest.java # 单元测试
│   │   └── androidTest/
│   │       └── java/
│   │           └── com/example/preservationmanager/
│   │               └── utils/
│   │                   └── CalendarUtilsInstrumentedTest.java # 仪器测试
│   ├── build.gradle                         # 模块级构建文件
│   └── proguard-rules.pro                   # ProGuard 混淆规则
├── build.gradle                             # 项目级构建文件
├── settings.gradle                          # Gradle 配置
├── gradle.properties                        # Gradle 属性
├── .gitignore                               # Git 忽略文件
├── README.md                                # 项目说明
├── USAGE_GUIDE.md                          # 使用指南
├── API_REFERENCE.md                        # API 参考
└── PROJECT_STRUCTURE.md                    # 本文件
```

## 文件详解

### 核心文件

#### 1. CalendarUtils.java
**位置**：`app/src/main/java/com/example/preservationmanager/utils/CalendarUtils.java`

**功能**：
- 日历权限检查和申请
- 日历账户获取或创建
- 事件的创建、更新、删除
- 提醒的添加
- 事件详情查询
- 时间工具方法

**主要方法**：
- `hasCalendarPermissions()` - 检查权限
- `requestCalendarPermissions()` - 请求权限
- `getOrCreateCalendarAccount()` - 获取日历账户
- `insertEvent()` - 插入事件
- `addReminder()` - 添加提醒
- `addEventWithReminder()` - 创建事件和提醒
- `updateEvent()` - 更新事件
- `deleteEvent()` - 删除事件
- `getEventDetails()` - 获取事件详情
- `createDateTime()` - 创建时间戳
- `createFutureDateTime()` - 创建未来时间戳

**代码行数**：约 400 行
**复杂度**：中等
**推荐阅读**：必读

---

#### 2. MainActivity.java
**位置**：`app/src/main/java/com/example/preservationmanager/MainActivity.java`

**功能**：
- 展示如何在 Activity 中使用 CalendarUtils
- 实现权限请求流程
- 创建日历事件的示例

**主要方法**：
- `onCreate()` - 初始化 Activity
- `createCalendarEvent()` - 创建事件的示例
- `onRequestPermissionsResult()` - 处理权限申请结果

**用途**：学习如何集成 CalendarUtils

---

#### 3. ExampleUsage.java
**位置**：`app/src/main/java/com/example/preservationmanager/ExampleUsage.java`

**功能**：
提供 10 个详细的使用示例，包括：
1. 最简单的调用方式
2. 权限检查的完整流程
3. 指定时区的事件创建
4. 手动分步操作
5. 多个提醒时间
6. 指定日期时间
7. 更新和删除事件
8. 获取事件详情
9. 异步操作
10. 批量创建提醒

**用途**：学习各种常见场景的实现方式

---

#### 4. activity_main.xml
**位置**：`app/src/main/res/layout/activity_main.xml`

**功能**：
- 定义 MainActivity 的布局
- 包含创建事件的按钮
- 简单的信息展示

---

#### 5. AndroidManifest.xml
**位置**：`app/src/main/AndroidManifest.xml`

**功能**：
- 声明日历权限（READ_CALENDAR, WRITE_CALENDAR）
- 定义应用基本信息

**关键声明**：
```xml
<uses-permission android:name="android.permission.READ_CALENDAR" />
<uses-permission android:name="android.permission.WRITE_CALENDAR" />
```

---

### 构建文件

#### 1. build.gradle (模块级)
**位置**：`app/build.gradle`

**配置**：
- Android SDK 版本：编译 SDK 34，最小 SDK 21，目标 SDK 34
- 应用包名和版本
- 依赖库声明
- 编译选项

**依赖**：
- androidx.appcompat
- androidx.constraintlayout
- junit
- androidx.test

---

#### 2. build.gradle (项目级)
**位置**：`build.gradle`

**功能**：
- 声明使用的 Gradle 插件
- 全局配置

---

#### 3. settings.gradle
**位置**：`settings.gradle`

**功能**：
- 定义项目根目录
- 包含的模块
- 仓库配置

---

#### 4. gradle.properties
**位置**：`gradle.properties`

**功能**：
- Gradle 运行配置
- JVM 内存设置
- Android 命名空间配置

---

### 测试文件

#### 1. CalendarUtilsTest.java
**位置**：`app/src/test/java/.../CalendarUtilsTest.java`

**类型**：单元测试

**功能**：
- 测试时间工具方法
- 测试时间计算准确性
- 验证一致性

**运行命令**：`./gradlew test`

---

#### 2. CalendarUtilsInstrumentedTest.java
**位置**：`app/src/androidTest/java/.../CalendarUtilsInstrumentedTest.java`

**类型**：仪器测试（集成测试）

**功能**：
- 测试权限检查
- 测试日历操作
- 测试事件创建、更新、删除
- 测试提醒添加

**运行命令**：`./gradlew connectedAndroidTest`

**前提条件**：连接真实设备或运行模拟器

---

### 文档文件

#### 1. README.md
**位置**：`README.md`

**内容**：
- 项目概述
- 项目结构
- 主要方法说明
- 使用示例
- 权限声明
- 支持的 Android 版本
- 常见问题

**长度**：约 300 行

---

#### 2. USAGE_GUIDE.md
**位置**：`USAGE_GUIDE.md`

**内容**：
- 快速开始
- 10 个常见场景
- 时间计算技巧
- 错误处理
- 性能优化建议
- 权限管理最佳实践
- 调试技巧
- FAQ

**长度**：约 600 行

---

#### 3. API_REFERENCE.md
**位置**：`API_REFERENCE.md`

**内容**：
- 所有公开方法的详细说明
- 参数说明
- 返回值说明
- 使用示例
- 常量定义
- 返回值对照表
- 权限要求表

**长度**：约 800 行

---

#### 4. PROJECT_STRUCTURE.md
**位置**：`PROJECT_STRUCTURE.md`

**内容**：本文件，项目结构详解

---

### 配置文件

#### 1. .gitignore
**位置**：`.gitignore`

**功能**：
- 指定 Git 忽略的文件和文件夹
- 包括构建文件、IDE 配置、OS 文件等

**包含的忽略项**：
- `.gradle/`、`build/`
- `.idea/`、`*.iml`
- `.DS_Store`、`Thumbs.db`
- 等等

---

#### 2. proguard-rules.pro
**位置**：`app/proguard-rules.pro`

**功能**：
- 定义 ProGuard 混淆规则
- 保护 CalendarUtils 类
- 保护 AndroidX 类

---

## 包结构

```
com.example.preservationmanager/
├── MainActivity.java
├── ExampleUsage.java
└── utils/
    └── CalendarUtils.java
```

## 依赖关系图

```
MainActivity
    ↓
CalendarUtils
    ↓
Android Framework
    ├── android.provider.CalendarContract
    ├── android.content.ContentResolver
    └── android.content.pm.PackageManager

AndroidX Libraries
    ├── androidx.appcompat
    ├── androidx.core
    └── androidx.constraintlayout
```

## 编译流程

1. **源代码编译**
   - Java 源代码 → 字节码 (.class)
   - 编译目标：Java 11

2. **资源打包**
   - XML 资源 → 二进制资源

3. **DEX 转换**
   - 字节码 → DEX 文件

4. **打包**
   - DEX + 资源 → APK

5. **签名（可选）**
   - APK 签名

## 构建命令

### 开发构建
```bash
./gradlew build
```

### 调试构建
```bash
./gradlew assembleDebug
```

### 发布构建
```bash
./gradlew assembleRelease
```

### 运行单元测试
```bash
./gradlew test
```

### 运行仪器测试
```bash
./gradlew connectedAndroidTest
```

### 清理构建
```bash
./gradlew clean
```

## API 级别兼容性

| 功能 | 最小 API | 说明 |
|------|---------|------|
| 基础操作 | 21 | Android 5.0 |
| 运行时权限 | 23 | Android 6.0 |
| 所有功能 | 21 | Android 5.0+ |
| 目标版本 | 34 | Android 14 |

## 主要类和接口

### CalendarUtils（核心类）
- **修饰符**：public final
- **方法数**：12+
- **权限**：Calendar 相关权限
- **线程安全**：否

### MainActivity（示例 Activity）
- **继承**：AppCompatActivity
- **功能**：演示使用方法

### ExampleUsage（示例集合）
- **类型**：工具类
- **功能**：提供 10 个使用示例

## 资源说明

### 布局资源
- `activity_main.xml` - 主 Activity 布局

### 权限资源
- `READ_CALENDAR` - 读取日历
- `WRITE_CALENDAR` - 写入日历

## 性能考虑

- **权限检查**：快速，建议缓存结果
- **日历查询**：中等，避免频繁查询
- **事件创建**：慢，建议后台线程操作
- **事件删除**：快速

## 安全考虑

1. **权限隔离**
   - 严格遵循 Android 权限模型
   - 完整的运行时权限申请流程

2. **数据保护**
   - 通过 ContentProvider 访问日历
   - 不在日志中输出敏感信息

3. **异常处理**
   - 所有异常已捕获
   - 通过返回值表示错误

## 扩展建议

1. **添加 Kotlin 版本**
   - 提供 Kotlin 扩展函数

2. **添加 RxJava 支持**
   - 提供响应式编程接口

3. **添加更多提醒方式**
   - METHOD_EMAIL
   - METHOD_SMS
   - METHOD_SOUND

4. **添加重复事件**
   - 支持循环提醒

5. **添加多语言支持**
   - 国际化文本

## 维护指南

### 添加新功能
1. 在 CalendarUtils 中添加方法
2. 添加相应的测试
3. 更新 API_REFERENCE.md
4. 更新 USAGE_GUIDE.md

### 修复 Bug
1. 添加测试用例重现问题
2. 修复代码
3. 验证修复
4. 更新 README.md

### 版本发布
1. 更新版本号
2. 编写发布说明
3. 创建 Git 标签
4. 构建发布版本

## 相关资源

- [Android 开发者文档](https://developer.android.google.cn/)
- [CalendarContract API](https://developer.android.google.cn/reference/android/provider/CalendarContract)
- [Android 权限系统](https://developer.android.google.cn/guide/topics/permissions/overview)
- [Android 运行时权限](https://developer.android.google.cn/training/permissions/requesting)

## 许可证

MIT License

## 贡献指南

欢迎提交 Issue 和 Pull Request！

---

最后更新：2024-11-01
