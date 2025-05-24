package myPackage;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AlarmManager {
    private List<Alarm> alarms = new ArrayList<>();

    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
    }

    public void removeAlarm(Alarm alarm) {
        alarms.remove(alarm);
    }

    public List<Alarm> getAlarms() {
        return alarms;
    }

    public List<Alarm> checkAlarms(LocalTime currentTime) {
        List<Alarm> triggered = new ArrayList<>();
        for (Alarm alarm : alarms) {
            if (alarm.getTime().getHour() == currentTime.getHour() &&
                alarm.getTime().getMinute() == currentTime.getMinute()) {
                triggered.add(alarm);
            }
        }
        return triggered;
    }
}
