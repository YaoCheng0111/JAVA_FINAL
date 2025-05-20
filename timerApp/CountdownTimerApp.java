package myPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class CountdownTimerApp extends JFrame {
    private JLabel timeLabel;
    private JTextField timeInput;
    private JButton startButton;
    private JButton pauseButton;
    private JButton resetButton;

    private Timer timer;
    private TimerTask task;
    private int timeInSeconds;
    private int showInSeconds;
    private int showInMinute;
    private int showInHour;
    private boolean isRunning = false;

    public CountdownTimerApp() {
        setTitle("Countdown Timer");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        timeLabel = new JLabel("remain time: 0 second");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(timeLabel);

        add(new JLabel("Please input time:"));
        timeInput = new JTextField(10);
        add(timeInput);

        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        resetButton = new JButton("Reset");

        add(startButton);
        add(pauseButton);
        add(resetButton);

        pauseButton.setEnabled(false);
        resetButton.setEnabled(false);

        // 按鈕功能設定
        startButton.addActionListener(e -> startTimer());
        pauseButton.addActionListener(e -> pauseTimer());
        resetButton.addActionListener(e -> resetTimer());
    }

    private void startTimer() {
        if (!isRunning) {
            try {
                if (task == null) { // 第一次啟動或重設後啟動
                    timeInSeconds = Integer.parseInt(timeInput.getText());
                    if (timeInSeconds < 0) throw new NumberFormatException();
                }
                isRunning = true;
                timer = new Timer();
                task = new TimerTask() {
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(() -> {
                            if (timeInSeconds >= 0) {
                                showInSeconds = timeInSeconds%60;
                                showInMinute = (timeInSeconds/60)%60;
                                showInHour = timeInSeconds/3600;

                                timeLabel.setText("remain time: " + showInHour + " hour "+ showInMinute + " minute "+ showInSeconds + " second");
                                timeInSeconds--;
                            } else {
                                timeLabel.setText("Time's up!");
                                stopTimer();
                            }
                        });
                    }
                };
                timer.scheduleAtFixedRate(task, 0, 1000);
                startButton.setEnabled(false);
                pauseButton.setEnabled(true);
                resetButton.setEnabled(true);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "請輸入有效的整數秒數！");
            }
        }
    }

    private void pauseTimer() {
        if (isRunning) {
            stopTimer();
            isRunning = false;
            startButton.setEnabled(true);
        }
    }

    private void resetTimer() {
        stopTimer();
        task = null;
        timeLabel.setText("remain time: 0 second");
        timeInput.setText("");
        isRunning = false;
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        resetButton.setEnabled(false);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CountdownTimerApp().setVisible(true));
    }
}
