package myPackage;

import javax.swing.*;
import java.awt.*;

public class HabitTrackerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HabitManager manager = new HabitManager();
            JFrame frame = new JFrame("習慣追蹤器");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            JTabbedPane tabs = new JTabbedPane();

            tabs.add("習慣清單", new HabitListPanel(manager));
            tabs.add("表格排程", new ScheduleTablePanel(manager));

            JButton addBtn = new JButton("新增習慣");
            addBtn.addActionListener(e -> {
                new AddHabitDialog(frame, manager);
                frame.getContentPane().removeAll();
                tabs.setComponentAt(0, new HabitListPanel(manager));
                tabs.setComponentAt(1, new ScheduleTablePanel(manager));
                frame.getContentPane().add(tabs, BorderLayout.CENTER);
                frame.revalidate();
                frame.repaint();
            });

            frame.setLayout(new BorderLayout());
            frame.add(tabs, BorderLayout.CENTER);
            frame.add(addBtn, BorderLayout.SOUTH);

            frame.setVisible(true);
        });
    }
}
