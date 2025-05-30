package myPackage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimerItem {
    public enum TimerState { READY, RUNNING, PAUSED, FINISHED };
    
    private String title;
    private LocalDateTime endTime;
    private long durationSeconds; // 原始倒數總時間
    private long remainingSeconds;// 開始執行後的動態時間 
    private TimerState state = TimerState.READY; 

    public TimerItem() {} // Gson 需要無參數建構子

    //timer 四個物件:名稱 結束時間 運行時間 是否暫停
    public TimerItem(String title, LocalDateTime endTime, long durationSeconds) {
        this.title = title;
        this.endTime = endTime;
        this.durationSeconds = durationSeconds;
        this.remainingSeconds = durationSeconds;
        this.state = TimerState.RUNNING; 
    }

    //顯示
    public String getRemainingTime() {
        if(state == TimerState.FINISHED){
            return "完成";
        }
        long secs;
        if(state == TimerState.RUNNING) {
            secs = ChronoUnit.SECONDS.between(LocalDateTime.now(), endTime);
            if (secs <= 0) {
                state = TimerState.FINISHED;
                return "完成";
            }
            remainingSeconds = secs;
        } else{
            secs = remainingSeconds;
        }
        return String.format("%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60);
    }

    //開始
    public void start() {
        if (state == TimerState.READY || state == TimerState.PAUSED) {
            endTime = LocalDateTime.now().plusSeconds(remainingSeconds);
            state = TimerState.RUNNING;
        }
    }

    //暫停
    public void pause() {
        if (state == TimerState.RUNNING) {
            remainingSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), endTime);
            if (remainingSeconds < 0) remainingSeconds = 0;
            state = TimerState.PAUSED;
        }
    }

    //取消
    public void cancel(){
        if(state == TimerState.RUNNING || state == TimerState.PAUSED){
            remainingSeconds = durationSeconds;
            state = TimerState.READY;
        }
    }

    //重新開始
    public void restart() {
        remainingSeconds = durationSeconds;
        endTime = LocalDateTime.now().plusSeconds(remainingSeconds);
        state = TimerState.RUNNING;
    }

    //get method
    public TimerState getState() {
        return state;
    }
    
    public String getTitle() {
        return title;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

}
