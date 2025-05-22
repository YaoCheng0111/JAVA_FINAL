package myPackage;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LobbyGUI {

    private JLabel imageLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LobbyGUI().createUI());
    }

    private void createUI() {
        JFrame frame = new JFrame("Desktop Assistant");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        // 初始圖片
        ImageIcon defaultIcon = new ImageIcon("source/oiia_cat.jpg");
        imageLabel = new JLabel(scaleIcon(defaultIcon, 200, 200));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        // 建立按鈕（預設隱藏）
        JButton upButton = new JButton("Button1");
        JButton downButton = new JButton("Button2");
        JButton leftButton = new JButton("Button3");
        JButton rightButton = new JButton("Button4");

        upButton.setVisible(false);
        downButton.setVisible(false);
        leftButton.setVisible(false);
        rightButton.setVisible(false);

        // 按鈕面板
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(imageLabel, BorderLayout.CENTER);
        centerPanel.add(upButton, BorderLayout.NORTH);
        centerPanel.add(downButton, BorderLayout.SOUTH);
        centerPanel.add(leftButton, BorderLayout.WEST);
        centerPanel.add(rightButton, BorderLayout.EAST);

        // 點擊圖片時顯示/隱藏按鈕
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boolean visible = !upButton.isVisible();
                upButton.setVisible(visible);
                downButton.setVisible(visible);
                leftButton.setVisible(visible);
                rightButton.setVisible(visible);
                centerPanel.revalidate();
                centerPanel.repaint();
            }
        });

        // Button1 點擊 → 進入 mainFrame
        upButton.addActionListener(e -> {
            new mainFrame().setVisible(true);

        });

        // 初始時更新圖片（如果已裝備）
        StoreItem equipped = equipItemManager.getEquippedItem();
        if (equipped != null) {
            imageLabel.setIcon(scaleIcon(equipped.getImage(), 200, 200));
        }

        // 綁定裝備變更時圖片更新
        equipItemManager.setOnEquipChanged(() -> {
            StoreItem newItem = equipItemManager.getEquippedItem();
            if (newItem != null) {
                imageLabel.setIcon(scaleIcon(newItem.getImage(), 200, 200));
                imageLabel.revalidate();
                imageLabel.repaint();
            }
        });

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // ✅ 圖片等比例縮放
    private ImageIcon scaleIcon(ImageIcon icon, int maxWidth, int maxHeight) {
        Image img = icon.getImage();
        int imgWidth = icon.getIconWidth();
        int imgHeight = icon.getIconHeight();

        double widthRatio = (double) maxWidth / imgWidth;
        double heightRatio = (double) maxHeight / imgHeight;
        double scale = Math.min(widthRatio, heightRatio); // 取最小比例避免變形

        int newWidth = (int) (imgWidth * scale);
        int newHeight = (int) (imgHeight * scale);

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
