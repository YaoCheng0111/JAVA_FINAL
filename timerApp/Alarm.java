package myPackage;

import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Alarm {
    private LocalTime time;
    private String message;
    private Set<DayOfWeek> repeatDays = new HashSet<>();

    public Alarm(LocalTime time, String message) {
        this.time = time;
        this.message = message;
    }

    public Alarm(LocalTime time, String message,Set<DayOfWeek> repeatDays) {
        this.time = time;
        this.message = message;
        this.repeatDays = repeatDays != null ? repeatDays : new HashSet<>();
    }

    public LocalTime getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public Set<DayOfWeek> getRepeatDays(){
        return repeatDays;
    }

    public void setRepeatDays(Set<DayOfWeek> repeatDays) {
        this.repeatDays = repeatDays;
    }

    @Override
    public String toString() {
    List<DayOfWeek> orderedDays = Arrays.asList(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    );

    String days = orderedDays.stream()
        .filter(repeatDays::contains)
        .map(d -> d.toString().substring(0, 3))
        .reduce((a, b) -> a + "," + b)
        .orElse("");

    return String.format("%02d:%02d - %s (%s)", 
        time.getHour(), time.getMinute(), message,days);
    }

    // 存檔格式: HH:MM|message|MON,TUE,WED
    public String toStorageString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d:%02d", time.getHour(), time.getMinute()))
          .append("|").append(message.replace("|", "｜"));
        if (!repeatDays.isEmpty()) {
            sb.append("|");
            repeatDays.stream()
                .sorted()
                .forEach(day -> sb.append(day.name()).append(","));
            sb.setLength(sb.length() - 1); // 去除最後的逗號
        }
        return sb.toString();
    }

    public static Alarm fromStorageString(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length < 2) return null;

            String[] timeParts = parts[0].split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int min = Integer.parseInt(timeParts[1]);

            String message = parts[1];

            Set<DayOfWeek> repeat = new HashSet<>();
            if (parts.length >= 3) {
                String[] dayParts = parts[2].split(",");
                for (String s : dayParts) {
                    repeat.add(DayOfWeek.valueOf(s));
                }
            }

            return new Alarm(LocalTime.of(hour, min), message, repeat);
        } catch (Exception e) {
            return null;
        }
    }
}
