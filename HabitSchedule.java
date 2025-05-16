package myPackage;

import java.time.*;

public class HabitSchedule {
    int dayOfWeek; // 1~7
    int startHour, startMinute;
    int endHour, endMinute;

    public HabitSchedule(int day, int sh, int sm, int eh, int em) {
        this.dayOfWeek = day;
        this.startHour = sh;
        this.startMinute = sm;
        this.endHour = eh;
        this.endMinute = em;
    }

    public LocalTime getStartTime() {
        return LocalTime.of(startHour, startMinute);
    }

    public String toReadableString() {
        return "星期" + dayOfWeek + " " + format(startHour, startMinute) + "~" + format(endHour, endMinute);
    }

    public String toDataString() {
        return dayOfWeek + "-" + startHour + ":" + startMinute + ":" + endHour + ":" + endMinute;
    }

    private String format(int h, int m) {
        return String.format("%02d:%02d", h, m);
    }
}
