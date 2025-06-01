package myPackage;

import java.time.LocalDateTime;
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

    public LocalDateTime getAlarTime(){
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

    public void setIsActive(Boolean isActive){
        this.isActive = isActive;
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
