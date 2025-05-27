package myPackage;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class ScheduleTablePanel extends JPanel {
    private static final String[] WEEKDAYS = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
    private final HabitManager habitManager;
    private final JTable table;
    private final ScheduleTableModel tableModel;

    public ScheduleTablePanel(HabitManager habitManager) {
        this.habitManager = habitManager;
        this.tableModel = new ScheduleTableModel();
        this.table = new JTable(tableModel);
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("新增習慣");
        addButton.addActionListener(e -> {
            String newHabitName = JOptionPane.showInputDialog(this, "輸入新習慣名稱:");
            if (newHabitName != null && !newHabitName.trim().isEmpty()) {
                habitManager.addHabit(new Habit(newHabitName.trim()));
                tableModel.fireTableDataChanged();
            }
        });
        buttonPanel.add(addButton);

        JButton deleteButton = new JButton("刪除習慣");
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            int selectedCol = table.getSelectedColumn();
            if (selectedCol == 0 && selectedRow > -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "確定要刪除選取的習慣嗎？", "確認刪除", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    habitManager.getHabits().remove(selectedRow);
                    tableModel.fireTableDataChanged();
                }
            } else {
                JOptionPane.showMessageDialog(this, "請先選取要刪除的習慣名稱。");
            }
        });
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        table.setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ✅ 修改點擊邏輯
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();

                if (row >= 0 && col >= 1) {
                    int today = LocalDate.now().getDayOfWeek().getValue() % 7;
                    if (col - 1 == today) {
                        if (habitManager.isChecked(row, today)) {
                            String habitName = habitManager.getHabitName(row);
                            JOptionPane.showMessageDialog(ScheduleTablePanel.this,
                                    habitName + " 今天已經打卡過了！");
                        } else {
                            habitManager.toggleCheckIn(row, today);
                            tableModel.fireTableCellUpdated(row, col);
                            JOptionPane.showMessageDialog(ScheduleTablePanel.this, "打卡成功！");
                        }
                    } else {
                        JOptionPane.showMessageDialog(ScheduleTablePanel.this, "只能打卡今天！");
                    }
                }
            }
        });
    }

    private class ScheduleTableModel extends AbstractTableModel {
        @Override
        public int getRowCount() {
            return habitManager.getHabitCount();
        }

        @Override
        public int getColumnCount() {
            return 8;
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0)
                return "Habit";
            return WEEKDAYS[column - 1];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return habitManager.getHabitName(rowIndex);
            } else {
                return habitManager.isChecked(rowIndex, columnIndex - 1) ? "✓" : "";
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}
