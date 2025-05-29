package myPackage;

import java.time.LocalDateTime;

public class AlarmItem {
    public String title;
    public LocalDateTime alarmTime;

    public AlarmItem(String title, LocalDateTime alarmTime) {
        this.title = title;
        this.alarmTime = alarmTime;
    }
}
