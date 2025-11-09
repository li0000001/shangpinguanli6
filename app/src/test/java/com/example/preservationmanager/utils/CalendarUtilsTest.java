package com.example.preservationmanager.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * CalendarUtils 的单元测试
 */
public class CalendarUtilsTest {

    private Calendar calendar;

    @Before
    public void setUp() {
        calendar = Calendar.getInstance();
    }

    /**
     * 测试创建指定日期时间
     */
    @Test
    public void testCreateDateTime() {
        long time = CalendarUtils.createDateTime(2024, 10, 15, 14, 30);

        assertNotEquals(-1, time);
        assertTrue(time > 0);

        // 验证时间戳
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        assertEquals(2024, cal.get(Calendar.YEAR));
        assertEquals(10, cal.get(Calendar.MONTH));
        assertEquals(15, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(14, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, cal.get(Calendar.MINUTE));
    }

    /**
     * 测试创建未来日期时间
     */
    @Test
    public void testCreateFutureDateTime() {
        long now = System.currentTimeMillis();
        long futureTime = CalendarUtils.createFutureDateTime(3, 10, 0);

        assertTrue(futureTime > now);

        // 验证大约 3 天后
        long difference = futureTime - now;
        long threeeDaysMs = 3 * 24 * 60 * 60 * 1000L;

        // 允许 1 小时的误差
        assertTrue(difference >= threeeDaysMs - 60 * 60 * 1000);
        assertTrue(difference <= threeeDaysMs + 60 * 60 * 1000);
    }

    /**
     * 测试时间计算
     */
    @Test
    public void testTimeCalculation() {
        // 测试 1 天的毫秒数
        long oneDayMs = 24 * 60 * 60 * 1000L;

        long time1 = CalendarUtils.createFutureDateTime(0, 10, 0);
        long time2 = CalendarUtils.createFutureDateTime(1, 10, 0);

        long difference = time2 - time1;

        // 允许 10 分钟的误差
        assertTrue(Math.abs(difference - oneDayMs) < 10 * 60 * 1000);
    }

    /**
     * 测试多次调用 createFutureDateTime 的一致性
     */
    @Test
    public void testConsistency() {
        long time1 = CalendarUtils.createFutureDateTime(5, 14, 30);
        long time2 = CalendarUtils.createFutureDateTime(5, 14, 30);

        // 两次调用应该返回接近的时间（允许 1 秒的偏差）
        assertTrue(Math.abs(time1 - time2) < 1000);
    }

    /**
     * 测试零时差
     */
    @Test
    public void testZeroDaysOffset() {
        long now = System.currentTimeMillis();
        long today = CalendarUtils.createFutureDateTime(0, 10, 0);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(today);

        // 验证是今天
        Calendar nowCal = Calendar.getInstance();
        assertEquals(nowCal.get(Calendar.DAY_OF_YEAR), cal.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * 测试不同时区的时间差异
     */
    @Test
    public void testTimezoneHandling() {
        // 创建两个时间（应该相同）
        long time1 = CalendarUtils.createDateTime(2024, 10, 15, 14, 0);
        long time2 = CalendarUtils.createDateTime(2024, 10, 15, 14, 0);

        assertEquals(time1, time2);
    }

    /**
     * 测试负时差
     */
    @Test
    public void testNegativeDaysOffset() {
        long yesterday = CalendarUtils.createFutureDateTime(-1, 10, 0);
        long today = CalendarUtils.createFutureDateTime(0, 10, 0);

        assertTrue(yesterday < today);
    }

    /**
     * 测试大的天数偏差
     */
    @Test
    public void testLargeDaysOffset() {
        long time365 = CalendarUtils.createFutureDateTime(365, 10, 0);
        long time0 = CalendarUtils.createFutureDateTime(0, 10, 0);

        long difference = time365 - time0;
        long expectedMs = 365 * 24 * 60 * 60 * 1000L;

        // 允许 1 小时的误差
        assertTrue(Math.abs(difference - expectedMs) < 60 * 60 * 1000);
    }

    /**
     * 测试小时和分钟的设置
     */
    @Test
    public void testHourAndMinute() {
        long time = CalendarUtils.createFutureDateTime(0, 23, 59);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        assertEquals(23, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, cal.get(Calendar.MINUTE));
    }

    /**
     * 测试凌晨时间
     */
    @Test
    public void testMidnightTime() {
        long time = CalendarUtils.createFutureDateTime(1, 0, 0);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, cal.get(Calendar.MINUTE));
    }

    /**
     * 测试中午时间
     */
    @Test
    public void testNoonTime() {
        long time = CalendarUtils.createFutureDateTime(0, 12, 0);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        assertEquals(12, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, cal.get(Calendar.MINUTE));
    }
}
