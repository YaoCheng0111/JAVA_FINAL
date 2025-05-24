package myPackage;

public class HabitSchedule {
    public int day, startHour, startMinute, endHour, endMinute;

    public HabitSchedule(int day, int startHour, int startMinute, int endHour, int endMinute) {
        this.day = day;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    public String toDisplayString() {
        String[] week = { "日", "一", "二", "三", "四", "五", "六" };
        return "週" + week[(day - 1) % 7] + " " +
                String.format("%02d:%02d", startHour, startMinute) + "~" +
                String.format("%02d:%02d", endHour, endMinute);
    }

    public String toDataString() {
        return day + "-" + startHour + ":" + startMinute + "-" + endHour + ":" + endMinute;
    }
}
