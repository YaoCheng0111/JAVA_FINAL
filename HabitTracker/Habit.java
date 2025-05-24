package myPackage;

import java.util.*;

public class Habit {
    public String name;
    public List<HabitSchedule> schedules = new ArrayList<>();
    public Set<String> checkIns = new HashSet<>();

    public Habit(String name) {
        this.name = name;
    }

    public void addSchedule(int day, int startHour, int startMinute, int endHour, int endMinute) {
        schedules.add(new HabitSchedule(day, startHour, startMinute, endHour, endMinute));
    }

    public String getScheduleText() {
        if (schedules.isEmpty())
            return "無排程";
        StringBuilder sb = new StringBuilder();
        for (HabitSchedule s : schedules) {
            sb.append(s.toDisplayString()).append(" ");
        }
        return sb.toString().trim();
    }

    public boolean shouldRemindNow() {
        Calendar now = Calendar.getInstance();
        int today = now.get(Calendar.DAY_OF_WEEK);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        for (HabitSchedule s : schedules) {
            if (s.day == today && s.startHour == hour && s.startMinute == minute) {
                return true;
            }
        }
        return false;
    }

    public boolean isCheckedInToday() {
        String today = java.time.LocalDate.now().toString();
        return checkIns.contains(today);
    }

    public void checkInToday() {
        String today = java.time.LocalDate.now().toString();
        checkIns.add(today);
    }
}
