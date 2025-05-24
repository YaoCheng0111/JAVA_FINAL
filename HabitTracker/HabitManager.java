package myPackage;

import java.io.*;
import java.util.*;

public class HabitManager {
    private final Map<String, Habit> habits = new HashMap<>();
    private final String DATA_FILE = "habits.txt";

    public HabitManager() {
        loadFromFile();
    }

    public void addHabit(Habit habit) {
        habits.put(habit.name, habit);
        saveToFile();
    }

    public void deleteHabit(String name) {
        habits.remove(name);
        saveToFile();
    }

    public Collection<Habit> getAllHabits() {
        return habits.values();
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(DATA_FILE, "UTF-8")) {
            for (Habit habit : habits.values()) {
                writer.print(habit.name);
                for (HabitSchedule s : habit.schedules) {
                    writer.print("," + s.toDataString());
                }
                writer.println();
            }
        } catch (IOException e) {
            System.out.println("儲存失敗：" + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists())
            return;

        try (Scanner fileScanner = new Scanner(file, "UTF-8")) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    Habit habit = new Habit(parts[0]);
                    for (int i = 1; i < parts.length; i++) {
                        String[] segment = parts[i].split("[-:]");
                        if (segment.length < 5)
                            continue;
                        int day = Integer.parseInt(segment[0]);
                        int startHour = Integer.parseInt(segment[1]);
                        int startMinute = Integer.parseInt(segment[2]);
                        int endHour = Integer.parseInt(segment[3]);
                        int endMinute = Integer.parseInt(segment[4]);
                        habit.addSchedule(day, startHour, startMinute, endHour, endMinute);
                    }
                    habits.put(habit.name, habit);
                }
            }
        } catch (IOException e) {
            System.out.println("讀取失敗：" + e.getMessage());
        }
    }
}
