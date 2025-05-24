package myPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

import java.awt.*;

public class CommonTimesDialog extends JDialog{
    private String newTime;
    
    public CommonTimesDialog(CountdownTimerPanel parentPanel) {
        super(SwingUtilities.getWindowAncestor(parentPanel), "常用時間", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        setSize(300, 400);
        setLocationRelativeTo(parentPanel);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> timeList = new JList<>(model);
        loadCommonTimes(model);

        timeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(timeList);
        add(scrollPane, BorderLayout.CENTER);

        JButton startButton = new JButton("開始倒數");
        JButton addButton = new JButton("新增");
        JButton editButton = new JButton("修改");
        JButton deleteButton = new JButton("刪除");

        startButton.addActionListener(e -> {
            String selected = timeList.getSelectedValue();
            if (selected != null) {
                int seconds = parseTimeToSeconds(selected);
                parentPanel.startWithPreset(seconds);
                dispose();
            }
        });

        addButton.addActionListener(e -> {
            String time = AddCommonTimesDialog.showDialog(this);
            if (time != null) {
                model.addElement(time);
                saveCommonTimes(model);
            }
        });

        editButton.addActionListener(e -> {
            String selected = timeList.getSelectedValue(); 
            if (selected != null) {
                String[] splitSelected = selected.split(":");
                int editHour = Integer.parseInt(splitSelected[0]);
                int editMinute = Integer.parseInt(splitSelected[1]);
                int editSecond = Integer.parseInt(splitSelected[2]);
                String edited = EditCommonTimesDialog.showDialog(this,editHour,editMinute,editSecond);
                if (edited != null) {
                    int idx = timeList.getSelectedIndex();
                    model.set(idx, edited);
                    saveCommonTimes(model);
                }
            }
        });

        deleteButton.addActionListener(e -> {
            String selected = timeList.getSelectedValue();
            if (selected != null) {
                model.removeElement(selected);
                saveCommonTimes(model);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(startButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadCommonTimes(DefaultListModel<String> model) {
        try (BufferedReader reader = new BufferedReader(new FileReader("times.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                model.addElement(line.trim());
            }
        } catch (IOException e) {
            // 初次建立或檔案不存在
        }
    }

    private void saveCommonTimes(DefaultListModel<String> model) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("times.txt"))) {
            for (int i = 0; i < model.size(); i++) {
                writer.write(model.get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int parseTimeToSeconds(String time) {
        String[] parts = time.split(":");
        int h = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        int s = Integer.parseInt(parts[2]);
        return h * 3600 + m * 60 + s;
    }

    private void setNewTime(int newHour,int newMinute,int newSecond){
        this.newTime  = String.format("%02d:%02d:%02d", newHour,newMinute,newSecond);
    }
}
