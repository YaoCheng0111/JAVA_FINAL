package myPackage;

import java.io.*;
import java.util.*;

public class HabitStorage {
    private static final String FILE = "habits.txt";

    public static void saveHabits(Map<String, Habit> habits) {
        try (PrintWriter writer = new PrintWriter(FILE)) {
            for (Habit habit : habits.values()) {
                writer.print(habit.name);
                for (HabitSchedule s : habit.schedules) {
                    writer.print("," + s.toDataString());
                }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Habit> loadHabits() {
        Map<String, Habit> habits = new HashMap<>();
        File file = new File(FILE);
        if (!file.exists())
            return habits;

        try (Scanner fileScanner = new Scanner(file, "UTF-8")) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    Habit habit = new Habit(parts[0]);
                    for (int i = 1; i < parts.length; i++) {
                        String[] segment = parts[i].split(":");
                        if (segment.length == 5) {
                            int day = Integer.parseInt(segment[0]);
                            int startHour = Integer.parseInt(segment[1]);
                            int startMinute = Integer.parseInt(segment[2]);
                            int endHour = Integer.parseInt(segment[3]);
                            int endMinute = Integer.parseInt(segment[4]);
                            habit.addSchedule(day, startHour, startMinute, endHour, endMinute);
                        }
                    }
                    habits.put(habit.name, habit);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return habits;
    }
}