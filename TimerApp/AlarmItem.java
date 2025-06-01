package myPackage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;

public class AlarmItem {
    private String title;
    private LocalDateTime alarmTime;
    private List<Boolean> repeatDays = new ArrayList<>();
    private Boolean isActive;

    public AlarmItem() {} // Gson 需要無參數建構子

    public AlarmItem(String title, LocalDateTime alarmTime,List<Boolean> repeatDays,Boolean isActive) {
        this.title = title;
        this.alarmTime = alarmTime;
        this.repeatDays = repeatDays;
        this.isActive = isActive;
    }

    //get set method
    public String getTitle(){
        return title;
    }

    public LocalDateTime getAlarmTime(){
        return alarmTime;
    }

    public List<Boolean> getRepeatDays(){
        return repeatDays;
    }

    public Boolean getIsActive(){
        return isActive;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setAlarmTime(LocalDateTime alarmTime){
        this.alarmTime = alarmTime;
    }

    public void setRepeatDays(List<Boolean> repeatDays){
        this.repeatDays = repeatDays;
    }

    public void setActive(){
        isActive = !isActive;
    }

    //不可能有bug
    public String getRemainingTime() {
        if(!isActive){
            return "未啟用";
        }
        
        LocalDate today = LocalDate.now();
        int todayOfWeek = today.getDayOfWeek().getValue() % 7;
        if(!repeatDays.get(todayOfWeek)) return "非重複星期";

        long secs = ChronoUnit.SECONDS.between(LocalDateTime.now(), alarmTime);
        if (secs <= 0) return "今日鬧鐘已過";
        return String.format("%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60);
    }

}
