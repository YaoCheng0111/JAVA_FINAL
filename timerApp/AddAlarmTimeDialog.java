package myPackage;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

public class AddAlarmTimeDialog extends JDialog{
    
    private Alarm result;

    public static Alarm showAddDialog(Component parent) {
        Window window = SwingUtilities.getWindowAncestor(parent);
        AddAlarmTimeDialog dialog = new AddAlarmTimeDialog(window);
        dialog.setVisible(true);
        return dialog.result;
    }
    
    public AddAlarmTimeDialog(Window parent){
        super(parent , "新增鬧鐘", ModalityType.APPLICATION_MODAL);
        setSize(300,150);
        setLocationRelativeTo(parent);

        JPanel inputPanel = new JPanel(new FlowLayout());

        JComboBox<Integer> hourBox = createComboBox(24);
        JComboBox<Integer> minuteBox = createComboBox(60);
        JTextField messageField = new JTextField(15);

        inputPanel.add(new JLabel("時:")); inputPanel.add(hourBox);
        inputPanel.add(new JLabel("分:")); inputPanel.add(minuteBox);
        inputPanel.add(new JLabel("訊息:")); inputPanel.add(messageField);

        add(inputPanel,BorderLayout.CENTER);

        JButton confirmButton = new JButton("確認");
        JButton cancelButton = new JButton("取消");

        confirmButton.addActionListener(e -> {
            int h = (int)hourBox.getSelectedItem();
            int m = (int)minuteBox.getSelectedItem();
            String msg = messageField.getText().isEmpty() ? "時間到了!" : messageField.getText(); 
            result = new Alarm(LocalTime.of(h,m),msg);
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
