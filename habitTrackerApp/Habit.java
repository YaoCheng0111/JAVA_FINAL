package myPackage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Habit {
    public String name;
    public List<HabitSchedule> schedules = new ArrayList<>();
    private Set<String> checkedDates = new HashSet<>();

    public Habit(String name) {
        this.name = name;
    }

    public void addSchedule(int dayOfWeek, int startHour, int startMinute, int endHour, int endMinute) {
        schedules.add(new HabitSchedule(dayOfWeek, startHour, startMinute, endHour, endMinute));
    }

    public boolean shouldRemindNow() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        int day = today.getDayOfWeek().getValue(); // 1=Monday, 7=Sunday

        for (HabitSchedule s : schedules) {
            if (s.dayOfWeek == day && s.startHour == now.getHour() && s.startMinute == now.getMinute()) {
                return !isChecked(today.toString());
            }
        }
        return false;
    }

    public void checkToday() {
        String today = LocalDate.now().toString();
        checkedDates.add(today);
    }

    public boolean isChecked(String date) {
        return checkedDates.contains(date);
    }

    public String getScheduleText() {
        StringBuilder sb = new StringBuilder();
        for (HabitSchedule s : schedules) {
            sb.append(s).append(" ");
        }
        return sb.toString();
    }

    public String toDataString() {
        StringBuilder sb = new StringBuilder(name);
        for (HabitSchedule s : schedules) {
            sb.append(",").append(s.toDataString());
        }
        return sb.toString();
    }
}
