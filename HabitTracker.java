package myPackage;

import java.util.*;

public class HabitTracker {
    static Scanner scanner = new Scanner(System.in);
    static Map<String, Habit> habits = new HashMap<>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Habit Tracker ===");
            System.out.println("1. 新增習慣");
            System.out.println("2. 打卡今日習慣");
            System.out.println("3. 查看打卡紀錄");
            System.out.println("0. 離開");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addHabit();
                case 2 -> markHabit();
                case 3 -> showHabits();
                case 0 -> {
                    System.out.println("再見！");
                    return;
                }
                default -> System.out.println("無效選項");
            }
        }
    }

    static void addHabit() {
        System.out.print("輸入習慣名稱：");
        String name = scanner.nextLine();
        habits.put(name, new Habit(name));
        System.out.println("已新增！");
    }

    static void markHabit() {
        System.out.print("輸入要打卡的習慣名稱：");
        String name = scanner.nextLine();
        Habit habit = habits.get(name);
        if (habit != null) {
            habit.markDoneToday();
            System.out.println("今日已打卡！");
        } else {
            System.out.println("找不到此習慣！");
        }
    }

    static void showHabits() {
        if (habits.isEmpty()) {
            System.out.println("尚未新增任何習慣。");
            return;
        }

        String today = java.time.LocalDate.now().toString();
        System.out.println("今日打卡狀況：");
        for (Habit habit : habits.values()) {
            String status = habit.isDone(today) ? "已完成" : "未完成";
            System.out.println(habit.name + " : " + status);
        }
    }

}
