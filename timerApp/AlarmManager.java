package myPackage;

import java.io.*;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;


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

    public void setAlarms(List<Alarm> newAlarms) {
        this.alarms = newAlarms;
    }

    public List<Alarm> checkAlarms(LocalTime currentTime) {
        List<Alarm> triggered = new ArrayList<>();
        DayOfWeek today = LocalTime.now().atDate(java.time.LocalDate.now()).getDayOfWeek();

        for (Alarm alarm : alarms) {
            if (alarm.getTime().getHour() == currentTime.getHour() &&
                alarm.getTime().getMinute() == currentTime.getMinute()) {

                if (alarm.getRepeatDays().isEmpty() || alarm.getRepeatDays().contains(today)) {
                    triggered.add(alarm);
                }
                triggered.add(alarm);
            }
        }
        return triggered;
    }

    public void saveToFile(File file) throws IOException {
        try (PrintWriter pw = new PrintWriter(file)) {
            for (Alarm alarm : alarms) {
                pw.println(alarm.toStorageString());
            }
        }
    }

    public void loadFromFile(File file) throws IOException {
        List<Alarm> loaded = new ArrayList<>();
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Alarm alarm = Alarm.fromStorageString(line);
                if (alarm != null) loaded.add(alarm);
            }
        }
        // 可選：按時間排序
        Collections.sort(loaded, Comparator.comparing(Alarm::getTime));
        alarms = loaded;
    }
}
