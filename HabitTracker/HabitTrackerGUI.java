package myPackage;

import javax.swing.*;

public class HabitTrackerGUI extends JFrame {
    public HabitTrackerGUI(HabitManager habitManager) {
        setTitle("習慣追蹤器");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        ScheduleTablePanel panel = new ScheduleTablePanel(habitManager);
        add(panel);

        setVisible(true);
    }
}
