package myPackage;

import javax.swing.*;
import java.awt.*;


public class AddCommonTimesDialog extends Dialog{
    private static String result = null;

    public static String showDialog(Window parent) {
        AddCommonTimesDialog dialog = new AddCommonTimesDialog(parent);
        dialog.setVisible(true);
        return result;
    }
    
    public AddCommonTimesDialog(Window parent){
        super(parent , "新增常用時間", ModalityType.APPLICATION_MODAL);
        setSize(300,150);
        setLocationRelativeTo(parent);

        JPanel inputPanel = new JPanel(new FlowLayout());

        JComboBox<Integer> hourBox = createComboBox(24);
        JComboBox<Integer> minuteBox = createComboBox(60);
        JComboBox<Integer> secondBox = createComboBox(60);

        inputPanel.add(new JLabel("時:")); inputPanel.add(hourBox);
        inputPanel.add(new JLabel("分:")); inputPanel.add(minuteBox);
        inputPanel.add(new JLabel("秒:")); inputPanel.add(secondBox);

        add(inputPanel,BorderLayout.CENTER);

        JButton confirmButton = new JButton("確認");
        JButton cancelButton = new JButton("取消");

        confirmButton.addActionListener(e -> {
            int h = (int)hourBox.getSelectedItem();
            int m = (int)minuteBox.getSelectedItem();
            int s = (int)secondBox.getSelectedItem();
            result = String.format("%02d:%02d:%02d",h,m,s);
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
