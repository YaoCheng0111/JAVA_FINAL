package myPackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

class AccessoryPanel extends JPanel {

    private UserData userData;
    private ArrayList<StoreItem> accessories;
    private StoreItem equippedAccessory = null; // 目前裝備中的飾品
    private ArrayList<JButton> equipButtons = new ArrayList<>();

    public AccessoryPanel(UserData userData, TokenPanel tokenPanel) {
        this.userData = userData;
        this.accessories = new ArrayList<>();

        // 加入飾品項目
        accessories.add(new StoreItem("glasses", 10, "source/sunglasses.png"));
        accessories.add(new StoreItem("hat", 10, "source/hat.png"));
        accessories.add(new StoreItem("bow", 10, "source/bow.png"));
        accessories.add(new StoreItem("shoes", 10, "source/shoes.png"));
        // 預設贈送免費飾品
        // StoreItem defaultAccessory = new StoreItem("none", 0, "source/default_accessory.png");
        // accessories.add(defaultAccessory);
        // userData.purchaseItem(defaultAccessory);

        String equippedName = userData.getEquippedItem();
        for (StoreItem item : accessories) {
            if (item.getName().equals(equippedName)) {
                equippedAccessory = item;
                break;
            }
        }

        setLayout(new GridLayout(2, 3, 10, 10));

        for (StoreItem accessory : accessories) {
            JPanel accessoryPanel = new JPanel();
            accessoryPanel.setLayout(new BorderLayout());

            // 調整圖片大小
            ImageIcon originalIcon = accessory.getImage();
            Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(scaledImage);

            JLabel imageLabel = new JLabel(resizedIcon);
            JLabel nameLabel = new JLabel(accessory.getName() + " - " + accessory.getPrice() + " 代幣:", JLabel.CENTER);

            JButton buyButton = new JButton("購買");
            JButton equipButton = new JButton("裝備");
            equipButton.setVisible(false); // 預設隱藏

            equipButtons.add(equipButton);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(buyButton);
            buttonPanel.add(equipButton);

            buyButton.setEnabled(userData.getTokens() >= accessory.getPrice());

            buyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (userData.getTokens() >= accessory.getPrice() && userData.purchaseItem(accessory)) {
                        //tokenPanel.updateTokens(-accessory.getPrice());
                        buyButton.setText("已購買");
                        buyButton.setEnabled(false);
                        equipButton.setVisible(true);
                        JOptionPane.showMessageDialog(null, "成功購買：" + accessory.getName());
                        updateButtons();
                    } else {
                        JOptionPane.showMessageDialog(null, "代幣不足！");
                    }
                }
            });

            equipButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    equippedAccessory = accessory;
                    equipItemManager.setEquippedItem(accessory);
                    userData.setEquippedItem(accessory.getName());
                    JOptionPane.showMessageDialog(null, "已裝備：" + accessory.getName());
                    updateEquipButtons();
                }
            });

            if (userData.hasItem(accessory.getName())) {
                buyButton.setText("已購買");
                buyButton.setEnabled(false);
                equipButton.setVisible(true);

                if (accessory == equippedAccessory) {
                    equipButton.setText("已裝備");
                    equipButton.setEnabled(false);
                } else {
                    equipButton.setText("裝備");
                    equipButton.setEnabled(true);
                }
            }

            accessoryPanel.add(imageLabel, BorderLayout.NORTH);
            accessoryPanel.add(nameLabel, BorderLayout.CENTER);
            accessoryPanel.add(buttonPanel, BorderLayout.SOUTH);
            add(accessoryPanel);
        }
    }

    public void updateButtons() {
        for (int i = 0; i < accessories.size(); i++) {
            StoreItem item = accessories.get(i);
            Component panel = getComponent(i);
            if (panel instanceof JPanel) {
                JPanel itemPanel = (JPanel) panel;
                for (Component comp : ((JPanel) itemPanel.getComponent(2)).getComponents()) {
                    if (comp instanceof JButton) {
                        JButton buyButton = (JButton) comp;
                        // 依需求補上動態 enable 的邏輯
                    }
                }
            }
        }
    }

    public void updateEquipButtons() {
        for (int i = 0; i < accessories.size(); i++) {
            StoreItem item = accessories.get(i);
            JButton equipButton = equipButtons.get(i);
            if (equipButton.isVisible()) {
                equipButton.setEnabled(item != equippedAccessory);
                equipButton.setText(item == equippedAccessory ? "已裝備" : "裝備");
            }
        }
    }
}
