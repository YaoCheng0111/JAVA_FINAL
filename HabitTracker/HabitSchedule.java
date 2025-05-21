package myPackage;

import java.time.*;

public class HabitSchedule {
    int dayOfWeek;
    int startHour, startMinute, endHour, endMinute;

    public HabitSchedule(int dayOfWeek, int startHour, int startMinute, int endHour, int endMinute) {
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    public boolean isNow() {
        LocalDateTime now = LocalDateTime.now();
        if (now.getDayOfWeek().getValue() != dayOfWeek)
            return false;
        LocalTime nowTime = now.toLocalTime();
        LocalTime start = LocalTime.of(startHour, startMinute);
        LocalTime end = LocalTime.of(endHour, endMinute);
        return !nowTime.isBefore(start) && !nowTime.isAfter(end);
    }

    public String toString() {
        return "星期" + dayOfWeek + " "
                + String.format("%02d:%02d~%02d:%02d", startHour, startMinute, endHour, endMinute);
    }

    public String toDataString() {
        return dayOfWeek + "-" + startHour + ":" + startMinute + "-" + endHour + ":" + endMinute;
    }
}
