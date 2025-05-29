package myPackage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimerItem {
    public String title;
    public LocalDateTime endTime;

    public TimerItem(String title, LocalDateTime endTime) {
        this.title = title;
        this.endTime = endTime;
    }

    public String getRemainingTime() {
        long secs = ChronoUnit.SECONDS.between(LocalDateTime.now(), endTime);
        return secs > 0
                ? String.format("%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60)
                : "完成";
    }
}
