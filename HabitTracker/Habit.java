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

    //空的建構子給Gson用(反序列化時會用到)
    public Habit() {
        this.name = "";
        this.checkInStatus = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            checkInStatus.add(false);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Boolean> getCheckInStatus() {
        //保證不會回傳null，且長度必須是7(七天)
        if (checkInStatus == null) {
            checkInStatus = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                checkInStatus.add(false);
            }
        } else if (checkInStatus.size() != 7) {
            List<Boolean> fixed = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                fixed.add(i < checkInStatus.size() ? checkInStatus.get(i) : false);
            }
            checkInStatus = fixed;
        }
        return checkInStatus;
    }
}
