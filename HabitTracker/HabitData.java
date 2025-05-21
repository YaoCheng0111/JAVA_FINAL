package myPackage;

import java.io.*;
import java.util.*;

public class HabitData {
    private static final String FILE_NAME = "habits.txt";

    public static void save(Map<String, Habit> habits) {
        try (PrintWriter writer = new PrintWriter(FILE_NAME, "UTF-8")) {
            for (Habit habit : habits.values()) {
                writer.println(habit.toDataString());
            }
        } catch (IOException e) {
            System.out.println("儲存錯誤：" + e.getMessage());
        }
    }

    public static Map<String, Habit> load() {
        Map<String, Habit> habits = new HashMap<>();
        File file = new File(FILE_NAME);
        if (!file.exists())
            return habits;

        try (Scanner scanner = new Scanner(file, "UTF-8")) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length < 1)
                    continue;
                Habit habit = new Habit(parts[0]);

                for (int i = 1; i < parts.length; i++) {
                    if (parts[i].startsWith("check:")) {
                        habit.checkinDates.add(parts[i].substring(6));
                    } else {
                        String[] s = parts[i].split("[-:]");
                        int day = Integer.parseInt(s[0]);
                        int sh = Integer.parseInt(s[1]);
                        int sm = Integer.parseInt(s[2]);
                        int eh = Integer.parseInt(s[3]);
                        int em = Integer.parseInt(s[4]);
                        habit.addSchedule(day, sh, sm, eh, em);
                    }
                }
                habits.put(habit.name, habit);
            }
        } catch (IOException e) {
            System.out.println("讀取錯誤：" + e.getMessage());
        }

        return habits;
    }
}
