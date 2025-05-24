package myPackage;

import java.time.LocalTime;

public class Alarm {
    private LocalTime time;
    private String message;

    public Alarm(LocalTime time, String message) {
        this.time = time;
        this.message = message;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return String.format("%02d:%02d - %s", time.getHour(), time.getMinute(), message);
    }
}
