package myPackage;

import java.util.ArrayList;
import java.util.List;

public class Habit {
    private String name;
    private List<Boolean> checkInStatus;
    private boolean weeklyAttendence;

    public Habit(String name) {
        this.name = name;
        this.checkInStatus = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            checkInStatus.add(false);
        }
        this.weeklyAttendence = false;
    }

    //get method of habit name
    public String getName() {
        return name;
    }

    //get method of all days check in
    public List<Boolean> getCheckInStatus() {
        return checkInStatus;
    }

    //存檔時用來檢測status完整性與回傳status值
    public void setCheckInStatus(List<Boolean> checkInStatus) {
        if (checkInStatus == null || checkInStatus.size() != 7) {
            this.checkInStatus = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                this.checkInStatus.add(false);
            }
        } else {
            this.checkInStatus = checkInStatus;
        }
    }

    //確認當天check in狀態
    public boolean isChecked(int dayIndex) {
        return checkInStatus.get(dayIndex);
    }

    //切換check in 狀態
    public void toggleCheckIn(int dayIndex) {
        checkInStatus.set(dayIndex, !checkInStatus.get(dayIndex));
    }

    //reset
    public void reset(){
        for (int i = 0; i < 7; i++) {
            checkInStatus.add(false);
        }
    }

    //周全勤
    public boolean isWeeklyAttendence(){
        for(int i=0;i<7;i++){
            if(!isChecked(i)){
                return false;
            }
        }
        return true;
    }

}