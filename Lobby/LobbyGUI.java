package myPackage;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LobbyGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LobbyGUI().createUI());
    }

    private void createUI() {
        JFrame frame = new JFrame("Desktop Assistant");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        //去掉邊框
        // JFrame frame = new JFrame("No Border Frame");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(400, 300);
        // // 移除標題列與邊框（這行要放在 setVisible 之前）
        // frame.setUndecorated(true);
        // 桌寵圖片
        ImageIcon icon;
        try {
            icon = new ImageIcon("source/uiiaCat.png");
            if (icon.getIconWidth() == -1) {
                throw new Exception("圖片載入失敗");
            }
        } catch (Exception e) {
            System.err.println("載入圖片時發生錯誤：" + e.getMessage());
            icon = new ImageIcon(); // 空白圖示
        }
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        // 建立四個按鈕，預設隱藏
        JButton upButton = new JButton("Button1");
        JButton downButton = new JButton("Button2");
        JButton leftButton = new JButton("Button3");
        JButton rightButton = new JButton("Button4");

        upButton.setVisible(false);
        downButton.setVisible(false);
        leftButton.setVisible(false);
        rightButton.setVisible(false);

        // 按鈕面板（用 BorderLayout 放在圖片四周）
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(imageLabel, BorderLayout.CENTER);
        centerPanel.add(upButton, BorderLayout.NORTH);
        centerPanel.add(downButton, BorderLayout.SOUTH);
        centerPanel.add(leftButton, BorderLayout.WEST);
        centerPanel.add(rightButton, BorderLayout.EAST);

        // 點擊圖片時顯示按鈕
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                upButton.setVisible(upButton.isVisible() ? false : true);
                downButton.setVisible(downButton.isVisible() ? false : true);
                leftButton.setVisible(leftButton.isVisible() ? false : true);
                rightButton.setVisible(rightButton.isVisible() ? false : true);
                centerPanel.revalidate(); // 更新 UI
                centerPanel.repaint();
            }
        });

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
