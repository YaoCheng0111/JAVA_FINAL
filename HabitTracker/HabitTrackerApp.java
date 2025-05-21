package myPackage;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;

public class HabitTrackerApp {
    private static Map<String, Habit> habits;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            habits = HabitData.load();
            JFrame frame = new JFrame("習慣追蹤器");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400);
            frame.setLayout(new BorderLayout());

            DefaultListModel<String> listModel = new DefaultListModel<>();
            JList<String> habitList = new JList<>(listModel);
            JScrollPane scrollPane = new JScrollPane(habitList);
            frame.add(scrollPane, BorderLayout.CENTER);

            JButton addBtn = new JButton("新增習慣");
            JButton checkBtn = new JButton("打卡");
            JPanel btnPanel = new JPanel();
            btnPanel.add(addBtn);
            btnPanel.add(checkBtn);
            frame.add(btnPanel, BorderLayout.SOUTH);

            Runnable refreshList = () -> {
                listModel.clear();
                for (Habit h : habits.values()) {
                    String today = LocalDate.now().toString();
                    String status = h.checkinDates.contains(today) ? "✅" : "❌";
                    listModel.addElement(h.name + " " + status + " " + h.getScheduleText());
                }
            };

            addBtn.addActionListener(e -> {
                new AddHabitDialog(frame, habit -> {
                    habits.put(habit.name, habit);
                    HabitData.save(habits);
                    refreshList.run();
                }).setVisible(true);
            });

            checkBtn.addActionListener(e -> {
                String selected = habitList.getSelectedValue();
                if (selected == null)
                    return;
                String name = selected.split(" ")[0];
                Habit h = habits.get(name);
                String today = LocalDate.now().toString();
                if (!h.checkinDates.contains(today))
                    h.checkinDates.add(today);
                HabitData.save(habits);
                refreshList.run();
            });

            refreshList.run();
            frame.setVisible(true);
        });
    }
}
