package myPackage;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class AddAlarmTimeDialog extends JDialog{
    
    private Alarm result;
    private Map<DayOfWeek,JCheckBox> dayCheckBoxes = new EnumMap<>(DayOfWeek.class);

    public static Alarm showAddDialog(Component parent) {
        Window window = SwingUtilities.getWindowAncestor(parent);
        AddAlarmTimeDialog dialog = new AddAlarmTimeDialog(window);
        dialog.setVisible(true);
        return dialog.result;
    }
    
    public AddAlarmTimeDialog(Window parent){
        super(parent , "新增鬧鐘", ModalityType.APPLICATION_MODAL);
        setSize(450,150);
        setLocationRelativeTo(parent);

        JPanel inputPanel = new JPanel(new FlowLayout());

        JComboBox<Integer> hourBox = createComboBox(24);
        JComboBox<Integer> minuteBox = createComboBox(60);
        JTextField messageField = new JTextField(15);
        JCheckBox enableCheckBox = new JCheckBox("啟用", true);

        inputPanel.add(new JLabel("時:")); inputPanel.add(hourBox);
        inputPanel.add(new JLabel("分:")); inputPanel.add(minuteBox);
        inputPanel.add(new JLabel("訊息:")); inputPanel.add(messageField);
        inputPanel.add(enableCheckBox);

        add(inputPanel,BorderLayout.CENTER);
        
        JPanel repeatPanel = new JPanel();
        repeatPanel.add(new JLabel("重複:"));
        for (DayOfWeek day : DayOfWeek.values()) {
            JCheckBox checkBox = new JCheckBox(day.getDisplayName(TextStyle.SHORT, Locale.getDefault()));
            dayCheckBoxes.put(day, checkBox);
            repeatPanel.add(checkBox);
        }
        add(repeatPanel, BorderLayout.NORTH);

        JButton confirmButton = new JButton("確認");
        JButton cancelButton = new JButton("取消");

        confirmButton.addActionListener(e -> {
            int h = (int)hourBox.getSelectedItem();
            int m = (int)minuteBox.getSelectedItem();
            String msg = messageField.getText().isEmpty() ? "時間到了!" : messageField.getText(); 
            boolean enabled = enableCheckBox.isSelected();

            Set<DayOfWeek> selectedDays = new HashSet<>();
            for (Map.Entry<DayOfWeek, JCheckBox> entry : dayCheckBoxes.entrySet()) {
                if (entry.getValue().isSelected()) {
                    selectedDays.add(entry.getKey());
                }
            }  

            result = new Alarm(LocalTime.of(h,m),msg,selectedDays,enabled);
            dispose();
        });

        cancelButton.addActionListener(e -> {
            result = null;
            dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel,BorderLayout.SOUTH);
    }

    private JComboBox<Integer> createComboBox(int max) {
        JComboBox<Integer> box = new JComboBox<>();
        for (int i = 0; i < max; i++) box.addItem(i);
        return box;
    }
}
