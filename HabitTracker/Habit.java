package myPackage;

import java.util.ArrayList;
import java.util.List;

public class Habit {
    String name;
    List<HabitSchedule> schedules = new ArrayList<>();
    List<String> checkinDates = new ArrayList<>();

    public Habit(String name) {
        this.name = name;
    }

    public void addSchedule(int dayOfWeek, int startHour, int startMinute, int endHour, int endMinute) {
        schedules.add(new HabitSchedule(dayOfWeek, startHour, startMinute, endHour, endMinute));
    }

    public boolean shouldRemindNow() {
        return schedules.stream().anyMatch(HabitSchedule::isNow);
    }

    public String getScheduleText() {
        if (schedules.isEmpty())
            return "（無排程）";
        StringBuilder sb = new StringBuilder();
        for (HabitSchedule s : schedules) {
            sb.append(s.toString()).append(" ");
        }
        return sb.toString().trim();
    }

    public String toDataString() {
        StringBuilder sb = new StringBuilder(name);
        for (HabitSchedule s : schedules) {
            sb.append(",").append(s.toDataString());
        }
        for (String date : checkinDates) {
            sb.append(",check:").append(date);
        }
        return sb.toString();
    }
}
