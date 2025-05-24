package myPackage;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AddHabitDialog extends JDialog {
    public AddHabitDialog(JFrame parent, HabitManager manager) {
        super(parent, "新增習慣", true);
        setLayout(new GridLayout(0, 1));

        JTextField nameField = new JTextField();
        add(new JLabel("習慣名稱："));
        add(nameField);

        JPanel daysPanel = new JPanel(new GridLayout(1, 7));
        JCheckBox[] dayChecks = new JCheckBox[7];
        String[] days = { "日", "一", "二", "三", "四", "五", "六" };
        for (int i = 0; i < 7; i++) {
            dayChecks[i] = new JCheckBox("週" + days[i]);
            daysPanel.add(dayChecks[i]);
        }
        add(new JLabel("選擇星期："));
        add(daysPanel);

        JComboBox<Integer> startHourBox = new JComboBox<>();
        JComboBox<Integer> startMinuteBox = new JComboBox<>();
        JComboBox<Integer> durationHourBox = new JComboBox<>();
        JComboBox<Integer> durationMinuteBox = new JComboBox<>();

        for (int i = 0; i < 24; i++)
            startHourBox.addItem(i);
        for (int i = 0; i < 60; i += 5)
            startMinuteBox.addItem(i);
        for (int i = 0; i <= 5; i++)
            durationHourBox.addItem(i);
        for (int i = 0; i < 60; i += 5)
            durationMinuteBox.addItem(i);

        add(new JLabel("開始時間："));
        JPanel timePanel = new JPanel();
        timePanel.add(startHourBox);
        timePanel.add(new JLabel(":"));
        timePanel.add(startMinuteBox);
        add(timePanel);

        add(new JLabel("持續時間 (H:Min)："));
        JPanel durationPanel = new JPanel();
        durationPanel.add(durationHourBox);
        durationPanel.add(new JLabel(":"));
        durationPanel.add(durationMinuteBox);
        add(durationPanel);

        JButton confirmBtn = new JButton("新增");
        confirmBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty())
                return;

            Habit habit = new Habit(name);
            int sh = (int) startHourBox.getSelectedItem();
            int sm = (int) startMinuteBox.getSelectedItem();
            int dh = (int) durationHourBox.getSelectedItem();
            int dm = (int) durationMinuteBox.getSelectedItem();

            LocalTime start = LocalTime.of(sh, sm);
            LocalTime end = start.plusHours(dh).plusMinutes(dm);

            for (int i = 0; i < 7; i++) {
                if (dayChecks[i].isSelected()) {
                    habit.addSchedule(i + 1, sh, sm, end.getHour(), end.getMinute());
                }
            }

            manager.addHabit(habit);
            dispose();
        });
        add(confirmBtn);

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
