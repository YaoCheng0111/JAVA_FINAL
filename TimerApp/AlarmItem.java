package myPackage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;

public class AlarmItem {
    private String title;
    private int hour,minute,second;
    private List<Boolean> repeatDays = new ArrayList<>();
    private Boolean isActive;
    private LocalDate lastTriggerDate = null;

    public AlarmItem() {} // Gson 需要無參數建構子

    public AlarmItem(String title, int hour,int minute,int second,List<Boolean> repeatDays,Boolean isActive) {
        this.title = title;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.repeatDays = repeatDays;
        this.isActive = isActive;
    }

    //get set method
    public String getTitle(){return title;}
    public int getHour(){return hour;}
    public int getMinute(){return minute;}
    public int getSecond(){return second;}
    public List<Boolean> getRepeatDays(){return repeatDays;}
    public Boolean getIsActive(){return isActive;}

    public void setTitle(String title){this.title = title;}
    public void setTime(int hour,int minute,int second){
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
    public void setRepeatDays(List<Boolean> repeatDays){this.repeatDays = repeatDays;}
    public void setActive(){isActive = !isActive;}

    //顯示剩餘時間
    public String getRemainingTime() {
        if(!isActive)return "未啟用";

        LocalDateTime now = LocalDateTime.now();
        int todayOfWeek = now.getDayOfWeek().getValue() % 7;

        for(int i=0;i<8;i++){
            int checkDay = (todayOfWeek + i)%7;
            if(repeatDays.get(checkDay)){
                LocalDateTime nextTime = now.plusDays(i)
                    .withHour(hour).withMinute(minute).withSecond(second).withNano(0);
            
                if(nextTime.isAfter(now)){
                    long secs = ChronoUnit.SECONDS.between(now, nextTime);
                    return String.format("%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60);
                }
            }
        }
        
        return "無符合的鬧鐘時間";
    }

    //檢查是否要跳通知
    public boolean shouldTrigger(LocalDateTime now) {
        if (!isActive) return false;

        int today = now.getDayOfWeek().getValue() % 7;
        if (!repeatDays.get(today)) return false;

        LocalDateTime alarmTime = now.withHour(hour).withMinute(minute).withSecond(second).withNano(0);
        if (now.truncatedTo(ChronoUnit.SECONDS).equals(alarmTime)) {
            if (lastTriggerDate == null || !lastTriggerDate.equals(now.toLocalDate())) {
                lastTriggerDate = now.toLocalDate();
                return true;
            }
        }
        return false;
    }

}
