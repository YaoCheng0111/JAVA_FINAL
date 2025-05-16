package myPackage;

import java.util.*;

class Habit {
    String name;
    Set<String> dates; // 儲存打卡日期，格式：yyyy-MM-dd

    Habit(String name) {
        this.name = name;
        this.dates = new HashSet<>();
    }

    void markDoneToday() {
        String today = java.time.LocalDate.now().toString();
        dates.add(today);
    }

    boolean isDone(String date) {
        return dates.contains(date);
    }
}
