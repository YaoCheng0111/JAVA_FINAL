package myPackage;

import java.time.LocalDateTime;

public class AlarmItem {
    public String title;
    public LocalDateTime alarmTime;

    public AlarmItem() {} // Gson 需要無參數建構子

    public AlarmItem(String title, LocalDateTime alarmTime) {
        this.title = title;
        this.alarmTime = alarmTime;
    }

    // 重新設定鬧鐘時間（例如：設定成明天同一時間）
    public void restart() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newAlarmTime = alarmTime.withYear(now.getYear())
                                              .withMonth(now.getMonthValue())
                                              .withDayOfMonth(now.getDayOfMonth());
        if (!newAlarmTime.isAfter(now)) {
            newAlarmTime = newAlarmTime.plusDays(1);
        }
        alarmTime = newAlarmTime;
    }
}
