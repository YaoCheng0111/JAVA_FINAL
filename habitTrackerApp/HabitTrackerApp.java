package myPackage;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class HabitTrackerApp {
    private JFrame frame;
    private DefaultListModel<String> habitListModel = new DefaultListModel<>();
    private Map<String, Habit> habits = new HashMap<>();
    private final String DATA_FILE = "habits.txt";
    private JList<String> habitList;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HabitTrackerApp().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("習慣追蹤器");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        habitList = new JList<>(habitListModel);
        frame.add(new JScrollPane(habitList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("新增習慣");
        JButton checkBtn = new JButton("打卡");
        JButton viewBtn = new JButton("查看打卡狀態");
        buttonPanel.add(addBtn);
        buttonPanel.add(checkBtn);
        buttonPanel.add(viewBtn);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        loadHabitsFromFile();
        updateHabitList();

        addBtn.addActionListener(e -> {
            AddHabitDialog dialog = new AddHabitDialog(frame, habitData -> {
                Habit habit = habits.getOrDefault(habitData.name, new Habit(habitData.name));
                for (int day : habitData.days) {
                    int startHour = habitData.startHour;
                    int startMin = habitData.startMinute;
                    int endHour = startHour + habitData.durationHour;
                    int endMin = startMin + habitData.durationMinute;
                    if (endMin >= 60) {
                        endHour += endMin / 60;
                        endMin %= 60;
                    }
                    habit.addSchedule(day, startHour, startMin, endHour, endMin);
                }
                habits.put(habit.name, habit);
                updateHabitList();
                saveHabitsToFile();
            });
            dialog.setVisible(true);
        });

        checkBtn.addActionListener(e -> {
            String selected = habitList.getSelectedValue();
            if (selected != null) {
                Habit habit = habits.get(selected.split("：")[0]);
                if (habit != null) {
                    habit.checkToday();
                    updateHabitList();
                }
            }
        });

        viewBtn.addActionListener(e -> {
            String selected = habitList.getSelectedValue();
            if (selected != null) {
                Habit habit = habits.get(selected.split("：")[0]);
                if (habit != null) {
                    String today = LocalDate.now().toString();
                    boolean checked = habit.isChecked(today);
                    JOptionPane.showMessageDialog(frame,
                            "今日「" + habit.name + "」狀態：" + (checked ? "✅ 已完成" : "❌ 未完成"));
                }
            }
        });

        frame.setVisible(true);
    }

    private void updateHabitList() {
        habitListModel.clear();
        for (Habit h : habits.values()) {
            boolean checked = h.isChecked(LocalDate.now().toString());
            habitListModel.addElement(h.name + "：" + (checked ? "✅ 已完成" : "❌ 未完成"));
        }
    }

    private void loadHabitsFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file, "UTF-8")) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    Habit habit = new Habit(parts[0]);
                    for (int i = 1; i < parts.length; i++) {
                        String[] timeParts = parts[i].split("[-:]");
                        int day = Integer.parseInt(timeParts[0]);
                        int sh = Integer.parseInt(timeParts[1]);
                        int sm = Integer.parseInt(timeParts[2]);
                        int eh = Integer.parseInt(timeParts[3]);
                        int em = Integer.parseInt(timeParts[4]);
                        habit.addSchedule(day, sh, sm, eh, em);
                    }
                    habits.put(habit.name, habit);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ 載入失敗：" + e.getMessage());
        }
    }

    private void saveHabitsToFile() {
        try (PrintWriter writer = new PrintWriter(DATA_FILE, "UTF-8")) {
            for (Habit habit : habits.values()) {
                writer.println(habit.toDataString());
            }
        } catch (IOException e) {
            System.out.println("❌ 儲存失敗：" + e.getMessage());
        }
    }
}
