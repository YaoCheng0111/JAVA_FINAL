package myPackage;

import javax.swing.*;
import java.awt.*;

public class AlarmDialog extends JDialog{
    
    public AlarmDialog(JFrame parent) {
        super(parent, "鬧鐘", true); // true = modal 對話視窗
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setContentPane(new AlarmPanel());
    }

}
