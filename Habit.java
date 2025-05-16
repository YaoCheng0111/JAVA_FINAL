package myPackage;

import java.time.*;
import java.util.*;

public class Habit {
    String name;
    List<HabitSchedule> schedules = new ArrayList<>();
    Set<String> checkins = new HashSet<>();

    public Habit(String name) {
        this.name = name;
    }

    public void addSchedule(int day, int sh, int sm, int eh, int em) {
        schedules.add(new HabitSchedule(day, sh, sm, eh, em));
    }

    public String getScheduleText() {
        if (schedules.isEmpty())
            return "無排程";
        StringBuilder sb = new StringBuilder();
        for (HabitSchedule s : schedules) {
            sb.append(s.toReadableString()).append(" ");
        }
        return sb.toString();
    }

    public boolean shouldRemindNow() {
        LocalDateTime now = LocalDateTime.now();
        int today = now.getDayOfWeek().getValue();
        LocalTime current = now.toLocalTime().withSecond(0).withNano(0);
        for (HabitSchedule s : schedules) {
            if (s.dayOfWeek == today && current.equals(s.getStartTime())) {
                return true;
            }
        }
        return false;
    }

    public void checkInToday() {
        String today = LocalDate.now().toString();
        checkins.add(today);
    }

    public boolean isCheckedInToday() {
        return checkins.contains(LocalDate.now().toString());
    }

    public String getCheckinStatusText() {
        return isCheckedInToday() ? "已完成" : "未完成";
    }

    public String getCheckinsDataString() {
        return String.join(";", checkins);
    }

    public void loadCheckinsFromString(String data) {
        if (data != null && !data.isEmpty()) {
            checkins.addAll(Arrays.asList(data.split(";")));
        }
    }
}
