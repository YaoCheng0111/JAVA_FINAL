package myPackage;

import javax.swing.*;

public class CountdownTimeDialog extends JDialog{
    public CountdownTimeDialog(JFrame parent) {
        super(parent, "Countdown Timer", true); // true = modal dialog
        setSize(400, 200);
        setLocationRelativeTo(parent);
        add(new CountdownTimerPanel());
    }    
}
