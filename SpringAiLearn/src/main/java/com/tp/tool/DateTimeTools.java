package com.tp.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 包名称：com.tp.tool
 * 类名称：DateTimeTools
 * 类描述：日期时间工具类
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/6/30 10:09
 */
@Component
public class DateTimeTools {

    @Tool(description = "获取用户在指定时区的当前日期和时间，用于回答需要实时时间的问题")
    public String getCurrentDateTime() {
        // 获取用户的时区偏好设置
        ZoneId zoneId = LocaleContextHolder.getTimeZone().toZoneId();
        ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(zoneId);
        // 格式化为 yyyy-MM-dd HH:mm:ss
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(zonedDateTime);
    }

    @Tool(description = "设置闹钟，调用此工具可在指定时间触发提醒。时间参数必须是ISO-8601格式，例如：2026-06-30 10:15:00")
    public void setAlarm(@ToolParam(description = "闹钟的触发时间，标准格式：yyyy-MM-dd HH:mm:ss") String alarmTime) {
        System.out.println("⏰闹钟已设置，将在" + alarmTime + "提醒用户。");
        //此处可扩展实际定时任务逻辑，如存入数据库、发送推送通知等
    }
}