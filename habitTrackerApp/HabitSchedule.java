package habitTrackerApp.myPackage;

public class HabitSchedule {
    public int dayOfWeek;
    public int startHour, startMinute;
    public int endHour, endMinute;

    public HabitSchedule(int dayOfWeek, int startHour, int startMinute, int endHour, int endMinute) {
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    @Override
    public String toString() {
        return "星期" + dayOfWeek + " " +
                String.format("%02d:%02d", startHour, startMinute) +
                "~" + String.format("%02d:%02d", endHour, endMinute);
    }

    public String toDataString() {
        return dayOfWeek + "-" + startHour + ":" + startMinute + "-" + endHour + ":" + endMinute;
    }
}
