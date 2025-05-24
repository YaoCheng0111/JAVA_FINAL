package myPackage;

import javax.swing.*;
import java.awt.*;

public class HabitListPanel extends JPanel {
    public HabitListPanel(HabitManager manager) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for (Habit habit : manager.getAllHabits()) {
            JPanel row = new JPanel(new BorderLayout());
            String status = habit.isCheckedInToday() ? "已完成" : "未完成";
            JLabel label = new JLabel(habit.name + " - " + status);
            JButton checkBtn = new JButton("打卡");
            checkBtn.addActionListener(e -> {
                habit.checkInToday();
                manager.saveToFile();
                label.setText(habit.name + " - 已完成");
            });
            row.add(label, BorderLayout.CENTER);
            row.add(checkBtn, BorderLayout.EAST);
            add(row);
        }
    }
}
