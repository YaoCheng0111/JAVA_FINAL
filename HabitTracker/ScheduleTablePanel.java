package myPackage;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class ScheduleTablePanel extends JPanel {
    public ScheduleTablePanel(HabitManager manager) {
        String[] days = { "週日", "週一", "週二", "週三", "週四", "週五", "週六" };
        String[] columnNames = new String[8];
        columnNames[0] = "時間";
        System.arraycopy(days, 0, columnNames, 1, 7);

        String[][] table = new String[24][7];
        for (int hour = 0; hour < 24; hour++) {
            for (int d = 0; d < 7; d++) {
                table[hour][d] = "";
            }
        }

        for (Habit habit : manager.getAllHabits()) {
            for (HabitSchedule s : habit.schedules) {
                int start = s.startHour;
                int end = s.endHour;
                if (start >= 0 && end <= 24) {
                    for (int h = start; h < end; h++) {
                        table[h][s.day - 1] += habit.name + "\n";
                    }
                }
            }
        }

        Object[][] data = new Object[24][8];
        for (int i = 0; i < 24; i++) {
            data[i][0] = String.format("%02d:00", i);
            for (int j = 0; j < 7; j++) {
                data[i][j + 1] = table[i][j];
            }
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable jTable = new JTable(model);
        jTable.setRowHeight(40);
        jTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(jTable);
        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
    }
}
