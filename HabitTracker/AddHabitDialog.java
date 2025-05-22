package myPackage;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.function.Consumer;

public class AddHabitDialog extends JDialog {
    public AddHabitDialog(JFrame parent, Consumer<Habit> onAdd) {
        super(parent, "新增習慣", true);
        setSize(400, 300);
        setLayout(new BorderLayout());

        JTextField nameField = new JTextField();
        JCheckBox[] dayBoxes = new JCheckBox[7];
        JPanel dayPanel = new JPanel(new GridLayout(1, 7));
        String[] days = { "一", "二", "三", "四", "五", "六", "日" };
        for (int i = 0; i < 7; i++) {
            dayBoxes[i] = new JCheckBox("週" + days[i]);
            dayPanel.add(dayBoxes[i]);
        }

        JComboBox<Integer> startHourBox = new JComboBox<>();
        JComboBox<Integer> startMinuteBox = new JComboBox<>();
        JComboBox<Integer> durHourBox = new JComboBox<>();
        JComboBox<Integer> durMinuteBox = new JComboBox<>();
        for (int i = 0; i < 24; i++)
            startHourBox.addItem(i);
        for (int i = 0; i < 60; i++) {
            startMinuteBox.addItem(i);
            if (i < 12)
                durHourBox.addItem(i);
        }
        for (int i = 0; i < 60; i += 5)
            durMinuteBox.addItem(i);

        JButton addBtn = new JButton("新增");
        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty())
                return;
            Habit habit = new Habit(name);
            int sh = (int) startHourBox.getSelectedItem();
            int sm = (int) startMinuteBox.getSelectedItem();
            int dh = (int) durHourBox.getSelectedItem();
            int dm = (int) durMinuteBox.getSelectedItem();
            LocalTime start = LocalTime.of(sh, sm);
            LocalTime end = start.plusHours(dh).plusMinutes(dm);
            for (int i = 0; i < 7; i++) {
                if (dayBoxes[i].isSelected()) {
                    habit.addSchedule(i + 1, sh, sm, end.getHour(), end.getMinute());
                }
            }
            onAdd.accept(habit);
            dispose();
        });

        JPanel timePanel = new JPanel(new GridLayout(2, 4));
        timePanel.add(new JLabel("開始時"));
        timePanel.add(startHourBox);
        timePanel.add(new JLabel(":"));
        timePanel.add(startMinuteBox);
        timePanel.add(new JLabel("持續時"));
        timePanel.add(durHourBox);
        timePanel.add(new JLabel(":"));
        timePanel.add(durMinuteBox);

        JPanel center = new JPanel(new GridLayout(4, 1));
        center.add(new JLabel("名稱"));
        center.add(nameField);
        center.add(dayPanel);
        center.add(timePanel);

        add(center, BorderLayout.CENTER);
        add(addBtn, BorderLayout.SOUTH);
    }
}
