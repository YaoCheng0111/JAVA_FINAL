package myPackage;

import javax.swing.*;

public class CountdownTimeDialog extends JDialog{
    public CountdownTimeDialog(JFrame parent) {
        super(parent, "倒數計時器", true); // true = modal dialog
        setSize(300, 150);
        setLocationRelativeTo(parent);
        add(new CountdownTimerPanel());
    }    
}
