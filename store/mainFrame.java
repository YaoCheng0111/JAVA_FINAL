package myPackage;

import javax.swing.*;
import java.awt.*;

public class mainFrame extends JFrame {

    private UserData userData;
    private TokenPanel tokenPanel;
    private ItemPanel itemPanel;

    public mainFrame(UserData userData) {
        this.userData = userData;
        setTitle("商店介面");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tokenPanel = new TokenPanel(userData);
        itemPanel = new ItemPanel(userData, tokenPanel);
        userData.addTokenListener(tokenPanel);
        JButton accessoryButton = new JButton("前往飾品店");
        accessoryButton.addActionListener(e -> {
            new AccessoryFrame(userData).setVisible(true);
        });

        add(tokenPanel, BorderLayout.NORTH);
        add(itemPanel, BorderLayout.CENTER);
        add(accessoryButton, BorderLayout.SOUTH);

    }

    public static void main(String[] args) {
        UserData userData = new UserData(100);
        SwingUtilities.invokeLater(() -> new mainFrame(userData).setVisible(true));
    }
}
