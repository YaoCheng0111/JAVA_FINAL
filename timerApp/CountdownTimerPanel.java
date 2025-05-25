package myPackage;

import javax.swing.*;
import java.awt.*;

public class CountdownTimerPanel extends JPanel {
    private JLabel timeLabel;
    private JComboBox<Integer> hourBox, minuteBox, secondBox;
    private JButton startButton, pauseButton, resetButton,commonTimesButton;
    private CountdownTimerLogic logic;

    public CountdownTimerPanel() {
        setLayout(new FlowLayout());

        timeLabel = new JLabel("剩餘時間 : 00 小時 00 分鐘 00 秒");
        add(timeLabel,BorderLayout.NORTH);

        hourBox = createComboBox(24);
        minuteBox = createComboBox(60);
        secondBox = createComboBox(60);

        JPanel comboboxPanel = new JPanel();
        comboboxPanel.add(new JLabel("時:")); comboboxPanel.add(hourBox);
        comboboxPanel.add(new JLabel("分:")); comboboxPanel.add(minuteBox);
        comboboxPanel.add(new JLabel("秒:")); comboboxPanel.add(secondBox);
        add(comboboxPanel,BorderLayout.CENTER);

        startButton = new JButton("開始");
        pauseButton = new JButton("暫停");
        resetButton = new JButton("重置");
        commonTimesButton = new JButton("常用時間");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(commonTimesButton);
        add(buttonPanel, BorderLayout.SOUTH);

        add(startButton); add(pauseButton); add(resetButton);add(commonTimesButton);

        logic = new CountdownTimerLogic(new CountdownTimerLogic.CountdownCallback() {
            public void onTick(int remainingSeconds) {
                SwingUtilities.invokeLater(()->updateTimeLabel(remainingSeconds));
            }
            public void onFinish() {
                SwingUtilities.invokeLater(() -> {
                    updateTimeLabel(0);
                    JOptionPane.showMessageDialog(CountdownTimerPanel.this, "時間到!");
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
        commonTimesButton.addActionListener(e -> new CommonTimesDialog(this));

        updateButtons();
    }

    public void startWithPreset(int seconds) {
        logic.reset();
        logic.start(seconds);
        
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
        timeLabel.setText(String.format("剩餘時間: %02d 小時 %02d 分鐘 %02d 秒", h, m, s));
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

        hourBox.setEnabled(!logic.isStarted());
        minuteBox.setEnabled(!logic.isStarted());
        secondBox.setEnabled(!logic.isStarted());
    } 
}
