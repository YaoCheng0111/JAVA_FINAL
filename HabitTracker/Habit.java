package myPackage;

import java.util.ArrayList;
import java.util.List;

public class Habit {
    private String name;
    private List<Boolean> checkInStatus;

    public Habit(String name) {
        this.name = name;
        this.checkInStatus = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            checkInStatus.add(false);
        }
    }

    public String getName() {
        return name;
    }

    public List<Boolean> getCheckInStatus() {
        return checkInStatus;
    }

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

    public boolean isChecked(int dayIndex) {
        return checkInStatus.get(dayIndex);
    }

    public void toggleCheckIn(int dayIndex) {
        checkInStatus.set(dayIndex, !checkInStatus.get(dayIndex));
    }
}
