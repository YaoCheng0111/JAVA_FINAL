package myPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class AddHabitDialog extends JDialog {
    private JTextField nameField;
    private Set<Integer> selectedDays = new HashSet<>();
    private JComboBox<Integer> startHourCombo;
    private JComboBox<Integer> startMinuteCombo;
    private JComboBox<Integer> durationHourCombo;
    private JComboBox<Integer> durationMinuteCombo;

    private Consumer<HabitData> onHabitAdded; // 回呼介面

    public AddHabitDialog(JFrame parent, Consumer<HabitData> onHabitAdded) {
        super(parent, "新增習慣", true);
        this.onHabitAdded = onHabitAdded;

        setLayout(new BorderLayout());

        // 習慣名稱輸入
        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel("習慣名稱："));
        nameField = new JTextField(15);
        namePanel.add(nameField);

        // 星期選擇按鈕組
        JPanel dayPanel = new JPanel();
        dayPanel.setBorder(BorderFactory.createTitledBorder("選擇星期"));
        dayPanel.setLayout(new FlowLayout());
        String[] dayNames = { "一", "二", "三", "四", "五", "六", "日" };
        for (int i = 0; i < dayNames.length; i++) {
            int day = i + 1;
            JToggleButton btn = new JToggleButton(dayNames[i]);
            btn.addItemListener(e -> {
                if (btn.isSelected())
                    selectedDays.add(day);
                else
                    selectedDays.remove(day);
            });
            dayPanel.add(btn);
        }

        // 開始時間選單
        JPanel startTimePanel = new JPanel(new FlowLayout());
        startTimePanel.setBorder(BorderFactory.createTitledBorder("開始時間"));
        startHourCombo = new JComboBox<>();
        startMinuteCombo = new JComboBox<>();
        for (int h = 0; h <= 23; h++)
            startHourCombo.addItem(h);
        for (int m = 0; m < 60; m += 5)
            startMinuteCombo.addItem(m);
        startTimePanel.add(startHourCombo);
        startTimePanel.add(new JLabel(":"));
        startTimePanel.add(startMinuteCombo);

        // 持續時間選單
        JPanel durationPanel = new JPanel(new FlowLayout());
        durationPanel.setBorder(BorderFactory.createTitledBorder("持續時間"));
        durationHourCombo = new JComboBox<>();
        durationMinuteCombo = new JComboBox<>();
        for (int h = 0; h <= 23; h++)
            durationHourCombo.addItem(h);
        for (int m = 0; m < 60; m += 5)
            durationMinuteCombo.addItem(m);
        durationPanel.add(durationHourCombo);
        durationPanel.add(new JLabel(":"));
        durationPanel.add(durationMinuteCombo);

        // 按鈕區
        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("新增");
        JButton cancelBtn = new JButton("取消");
        btnPanel.add(addBtn);
        btnPanel.add(cancelBtn);

        add(namePanel, BorderLayout.NORTH);
        add(dayPanel, BorderLayout.CENTER);

        JPanel timePanel = new JPanel(new GridLayout(2, 1));
        timePanel.add(startTimePanel);
        timePanel.add(durationPanel);
        add(timePanel, BorderLayout.EAST);

        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);

        // 按下新增
        addBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "請輸入習慣名稱！");
                return;
            }
            if (selectedDays.isEmpty()) {
                JOptionPane.showMessageDialog(this, "請選擇至少一天！");
                return;
            }
            if (onHabitAdded != null) {
                onHabitAdded.accept(new HabitData(
                        nameField.getText().trim(),
                        new HashSet<>(selectedDays),
                        (Integer) startHourCombo.getSelectedItem(),
                        (Integer) startMinuteCombo.getSelectedItem(),
                        (Integer) durationHourCombo.getSelectedItem(),
                        (Integer) durationMinuteCombo.getSelectedItem()));
            }
            dispose();
        });

        // 按下取消
        cancelBtn.addActionListener(e -> dispose());
    }

    // 簡單資料包裝類
    public static class HabitData {
        public final String name;
        public final Set<Integer> days;
        public final int startHour, startMinute, durationHour, durationMinute;

        public HabitData(String name, Set<Integer> days, int startHour, int startMinute, int durationHour,
                int durationMinute) {
            this.name = name;
            this.days = days;
            this.startHour = startHour;
            this.startMinute = startMinute;
            this.durationHour = durationHour;
            this.durationMinute = durationMinute;
        }
    }
}
