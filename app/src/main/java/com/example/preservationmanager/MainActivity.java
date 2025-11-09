package com.example.preservationmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.preservationmanager.utils.CalendarUtils;

/**
 * 主 Activity - 示例如何使用 CalendarUtils
 */
public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 请求日历权限
        CalendarUtils.requestCalendarPermissions(this);

        Button createEventButton = findViewById(R.id.create_event_button);
        createEventButton.setOnClickListener(v -> createCalendarEvent());
    }

    /**
     * 创建日历事件示例
     */
    private void createCalendarEvent() {
        String title = "牛奶保质期提醒";
        String description = "商品：牛奶\n数量：1 瓶\n需要及时消费";

        // 设置事件时间：明天的 09:00 到 10:00
        long startTime = CalendarUtils.createFutureDateTime(1, 9, 0);
        long endTime = CalendarUtils.createFutureDateTime(1, 10, 0);

        // 提醒时间：提前 60 分钟（1 小时）
        int reminderMinutesBefore = 60;

        // 创建事件和提醒
        long eventId = CalendarUtils.addEventWithReminder(
                this,
                title,
                description,
                startTime,
                endTime,
                reminderMinutesBefore
        );

        if (eventId != -1) {
            Toast.makeText(this, "事件创建成功！事件 ID: " + eventId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "事件创建失败！请检查权限设置", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理权限申请结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean hasAllPermissions = true;
            for (int grantResult : grantResults) {
                if (grantResult != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    hasAllPermissions = false;
                    break;
                }
            }

            if (hasAllPermissions) {
                Toast.makeText(this, "权限已授予", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
