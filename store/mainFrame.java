package myPackage;

import javax.swing.*;
import java.awt.*;

public class mainFrame extends JFrame {

    private TokenPanel tokenPanel;
    private ItemPanel itemPanel;

    public mainFrame() {
        setTitle("商店介面");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tokenPanel = new TokenPanel();
        itemPanel = new ItemPanel(tokenPanel);

        add(tokenPanel, BorderLayout.NORTH);
        add(itemPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new mainFrame().setVisible(true));
    }
}
