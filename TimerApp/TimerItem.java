package myPackage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimerItem {
    public String title;
    public LocalDateTime endTime;
    private long durationSeconds; // 紀錄倒數時間總秒數，用於重新開始
    private long remainingSeconds;
    private boolean isPaused;

    public TimerItem() {} // Gson 需要無參數建構子

    //timer 三個物件:名稱 結束時間 運行時間
    public TimerItem(String title, LocalDateTime endTime, long durationSeconds) {
        this.title = title;
        this.endTime = endTime;
        this.durationSeconds = durationSeconds;
    }

    //顯示剩餘時間
    public String getRemainingTime() {
        long secs = ChronoUnit.SECONDS.between(LocalDateTime.now(), endTime);
        return secs > 0 ? String.format("%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60) : "完成";
    }

    //重新開始
    public void restart() {
        this.endTime = LocalDateTime.now().plusSeconds(durationSeconds);
    }
}
