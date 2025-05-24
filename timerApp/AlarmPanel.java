package myPackage;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.List;
import java.io.*;

public class AlarmPanel extends JPanel {
    private DefaultListModel<Alarm> listModel;
    private JList<Alarm> alarmList;
    private AlarmManager alarmManager;
    private File saveFile = new File("alarms.txt");

    public AlarmPanel() {
        setLayout(new BorderLayout());

        alarmManager = new AlarmManager();
        listModel = new DefaultListModel<>();
        alarmList = new JList<>(listModel);

        JScrollPane scrollPane = new JScrollPane(alarmList);
        add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("新增鬧鐘");
        JButton deleteButton = new JButton("刪除選取");

        addButton.addActionListener(e -> {
            Alarm alarm = AddAlarmTimeDialog.showAddDialog(this);
            if (alarm != null) {
                alarmManager.addAlarm(alarm);
                listModel.addElement(alarm);
                saveAlarms();
            }
        });
        deleteButton.addActionListener(e -> {
            Alarm selected = alarmList.getSelectedValue();
            if (selected != null) {
                listModel.removeElement(selected);
                alarmManager.removeAlarm(selected);
                saveAlarms();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 啟動定時檢查鬧鐘
        new Timer(1000 * 60, e -> checkAlarms()).start(); // 每分鐘檢查一次
        try {
            alarmManager.loadFromFile(saveFile);
            for (Alarm alarm : alarmManager.getAlarms()) {
                listModel.addElement(alarm);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JComboBox<Integer> createComboBox(int max) {
        JComboBox<Integer> box = new JComboBox<>();
        for (int i = 0; i < max; i++) box.addItem(i);
        return box;
    }

    private void checkAlarms() {
        List<Alarm> triggered = alarmManager.checkAlarms(LocalTime.now());
        for (Alarm alarm : triggered) {
            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, alarm.getMessage(), "鬧鐘", JOptionPane.INFORMATION_MESSAGE)
            );
        }
    }

    private void saveAlarms() {
        try {
            alarmManager.saveToFile(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
