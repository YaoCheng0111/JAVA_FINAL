package myPackage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AccessoryFrame extends JFrame {

    private UserData userData;
    private TokenPanel tokenPanel;
    private AccessoryPanel accessoryPanel;

    public AccessoryFrame(UserData userData) {
        this.userData = userData;
        setTitle("飾品商店");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tokenPanel = new TokenPanel(userData);
        userData.addTokenListener(tokenPanel);
        accessoryPanel = new AccessoryPanel(userData, tokenPanel);

        add(tokenPanel, BorderLayout.NORTH);
        add(accessoryPanel, BorderLayout.CENTER);
    }
}
