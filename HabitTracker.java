package myPackage;

import java.time.*;
import java.util.*;
import java.io.*;

public class HabitTracker {
    static Map<String, Habit> habits = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);
    static final String DATA_FILE = "habits.txt";

    public static void main(String[] args) {
        loadHabitsFromFile();
        startReminderThread();

        while (true) {
            System.out.println("\n=== 習慣追蹤器 ===");
            System.out.println("1. 新增習慣");
            System.out.println("2. 查看所有習慣");
            System.out.println("3. 習慣打卡");
            System.out.println("0. 離開");
            System.out.print("選擇功能：");

            String input = scanner.nextLine();
            switch (input) {
                case "1" -> addHabit();
                case "2" -> showHabits();
                case "3" -> checkIn();
                case "0" -> {
                    saveHabitsToFile();
                    System.out.println("已儲存並結束程式。");
                    return;
                }
                default -> System.out.println("無效選項！");
            }
        }
    }

    static void addHabit() {
        String name;
        Habit habit;
        while (true) {
            System.out.print("輸入習慣名稱：");
            name = scanner.nextLine();
            habit = habits.getOrDefault(name, new Habit(name));

            System.out.print("請輸入星期（可複數，例如 1,4）：");
            String[] days = scanner.nextLine().split(",");

            System.out.print("請輸入開始時間（24小時制, 例如 15:30）：");
            String[] startTimeParts = scanner.nextLine().split(":");
            int startHour = Integer.parseInt(startTimeParts[0]);
            int startMinute = Integer.parseInt(startTimeParts[1]);

            System.out.print("請輸入持續時間（例如 1:30 表示 1小時30分鐘）：");
            String[] durationParts = scanner.nextLine().split(":");
            int durationHours = Integer.parseInt(durationParts[0]);
            int durationMinutes = Integer.parseInt(durationParts[1]);

            LocalTime startTime = LocalTime.of(startHour, startMinute);
            LocalTime endTime = startTime.plusHours(durationHours).plusMinutes(durationMinutes);
            int endHour = endTime.getHour();
            int endMinute = endTime.getMinute();

            for (String dayStr : days) {
                int day = Integer.parseInt(dayStr.trim());
                habit.addSchedule(day, startHour, startMinute, endHour, endMinute);
            }

            habits.put(name, habit);
            System.out.print("繼續新增排程？(y/n)：");
            String ans = scanner.nextLine();
            if (!ans.equalsIgnoreCase("y"))
                break;
        }

        System.out.println("已新增習慣：" + name);
    }

    static void showHabits() {
        if (habits.isEmpty()) {
            System.out.println("尚未新增任何習慣。");
            return;
        }

        System.out.println("\n目前所有習慣與排程：");
        for (Habit habit : habits.values()) {
            System.out.println("- " + habit.name + " ：" + habit.getScheduleText()
                    + " ｜ 今日狀態：" + habit.getCheckinStatusText());
        }
    }

    static void checkIn() {
        if (habits.isEmpty()) {
            System.out.println("尚未新增任何習慣。");
            return;
        }

        System.out.println("請選擇要打卡的習慣：");
        int i = 1;
        List<String> names = new ArrayList<>(habits.keySet());
        for (String name : names) {
            System.out.println(i + ". " + name);
            i++;
        }

        System.out.print("輸入編號：");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > names.size()) {
                System.out.println("無效編號！");
                return;
            }

            String selectedHabit = names.get(choice - 1);
            habits.get(selectedHabit).checkInToday();
            System.out.println("已對「" + selectedHabit + "」完成今日打卡！");
        } catch (NumberFormatException e) {
            System.out.println("請輸入有效數字！");
        }
    }

    static void startReminderThread() {
        new Thread(() -> {
            while (true) {
                LocalDateTime now = LocalDateTime.now();
                for (Habit habit : habits.values()) {
                    if (habit.shouldRemindNow()) {
                        System.out.println("提醒你：該去做「" + habit.name + "」囉！現在時間：" +
                                now.getDayOfWeek() + " " + now.toLocalTime().withSecond(0).withNano(0));
                    }
                }
                try {
                    Thread.sleep(60_000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();
    }

    static void saveHabitsToFile() {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(DATA_FILE), "UTF-8"))) {
            for (Habit habit : habits.values()) {
                writer.print(habit.name);
                for (HabitSchedule s : habit.schedules) {
                    writer.print("," + s.toDataString());
                }
                writer.print("|" + habit.getCheckinsDataString());
                writer.println();
            }
        } catch (IOException e) {
            System.out.println("儲存失敗：" + e.getMessage());
        }
    }

    static void loadHabitsFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists())
            return;

        try (Scanner fileScanner = new Scanner(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] habitParts = line.split("\\|");
                String[] parts = habitParts[0].split(",");

                if (parts.length >= 1) {
                    Habit habit = new Habit(parts[0]);
                    for (int i = 1; i < parts.length; i++) {
                        String[] segment = parts[i].split("[-:]");
                        if (segment.length < 5)
                            continue;
                        int day = Integer.parseInt(segment[0]);
                        int sh = Integer.parseInt(segment[1]);
                        int sm = Integer.parseInt(segment[2]);
                        int eh = Integer.parseInt(segment[3]);
                        int em = Integer.parseInt(segment[4]);
                        habit.addSchedule(day, sh, sm, eh, em);
                    }

                    if (habitParts.length > 1) {
                        habit.loadCheckinsFromString(habitParts[1]);
                    }

                    habits.put(habit.name, habit);
                }
            }
        } catch (IOException e) {
            System.out.println("讀取失敗：" + e.getMessage());
        }
    }

}
