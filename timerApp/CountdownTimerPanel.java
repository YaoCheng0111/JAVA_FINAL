package myPackage;

import javax.swing.*;
import java.awt.*;

public class CountdownTimerPanel extends JPanel {
    private JLabel timeLabel;
    private JComboBox<Integer> hourBox, minuteBox, secondBox;
    private JButton startButton, pauseButton, resetButton;
    private CountdownTimerLogic logic;

    public CountdownTimerPanel() {
        setLayout(new FlowLayout());

        timeLabel = new JLabel("remain time: 00 hour 00 minute 00 second");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(timeLabel);

        hourBox = createComboBox(24);
        minuteBox = createComboBox(60);
        secondBox = createComboBox(60);

        add(new JLabel("Hour:")); add(hourBox);
        add(new JLabel("Minute:")); add(minuteBox);
        add(new JLabel("Second:")); add(secondBox);

        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        resetButton = new JButton("Reset");

        add(startButton); add(pauseButton); add(resetButton);

        logic = new CountdownTimerLogic(new CountdownTimerLogic.CountdownCallback() {
            public void onTick(int remainingSeconds) {
                SwingUtilities.invokeLater(()->updateTimeLabel(remainingSeconds));
            }
            public void onFinish() {
                SwingUtilities.invokeLater(() -> {
                    updateTimeLabel(0);
                    JOptionPane.showMessageDialog(CountdownTimerPanel.this, "Time's up!");
                    resetUI();
                });
            }
        });

        startButton.addActionListener(e -> {
            if(logic.isPaused()){
                logic.start(logic.getTotalSeconds());
            }else{
                int seconds = getInputInSeconds();
                if (seconds > 0) logic.start(seconds);
            }
            updateButtons();
        });
        pauseButton.addActionListener(e -> {
            logic.pause();
            updateButtons();
        });
        resetButton.addActionListener(e -> {
            logic.reset();
            resetUI();
        });

        updateButtons();
    }

    private JComboBox<Integer> createComboBox(int max) {
        JComboBox<Integer> box = new JComboBox<>();
        for (int i = 0; i < max; i++) box.addItem(i);
        return box;
    }

    private int getInputInSeconds() {
        return (int)hourBox.getSelectedItem() * 3600
             + (int)minuteBox.getSelectedItem() * 60
             + (int)secondBox.getSelectedItem();
    }

    private void updateTimeLabel(int totalSeconds) {
        int h = totalSeconds / 3600;
        int m = (totalSeconds / 60) % 60;
        int s = totalSeconds % 60;
        timeLabel.setText(String.format("remain time: %02d hour %02d minute %02d second", h, m, s));
    }

    private void resetUI() {
        updateTimeLabel(0);
        hourBox.setSelectedIndex(0);
        minuteBox.setSelectedIndex(0);
        secondBox.setSelectedIndex(0);
        updateButtons();
    }

    private void updateButtons() {
        startButton.setEnabled(!logic.isRunning());
        pauseButton.setEnabled(logic.isRunning());
        resetButton.setEnabled(logic.getTotalSeconds() > 0);
    }
}
