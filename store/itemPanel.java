package myPackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

class ItemPanel extends JPanel {

    private UserData userData;
    private ArrayList<StoreItem> storeItems;
    private StoreItem equippedItem = null; // 目前裝備中的商品
    private ArrayList<JButton> equipButtons = new ArrayList<>();

    public ItemPanel(TokenPanel tokenPanel) {
        this.userData = new UserData(100);
        this.storeItems = new ArrayList<>();

        storeItems.add(new StoreItem("huh", 50, "img_source/huh_cat.jpg"));
        storeItems.add(new StoreItem("cute", 50, "img_source/Cute_cat.jpg"));
        storeItems.add(new StoreItem("pop", 50, "img_source/pop_cat.png"));
        storeItems.add(new StoreItem("okay", 50, "img_source/okay_cat.jpg"));
        storeItems.add(new StoreItem("serious", 50, "img_source/serious_cat.jpg"));
        storeItems.add(new StoreItem("oiia", 50, "img_source/oiia_cat.jpg"));

        setLayout(new GridLayout(2, 3, 10, 10)); // row=2, col=3 排列

        for (StoreItem item : storeItems) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BorderLayout());

            // 調整圖片大小
            ImageIcon originalIcon = item.getImage();
            Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(scaledImage);

            JLabel itemImage = new JLabel(resizedIcon);
            JLabel itemLabel = new JLabel(item.getName() + " - " + item.getPrice() + " 代幣", JLabel.CENTER);

            JButton buyButton = new JButton("購買");
            JButton equipButton = new JButton("裝備");
            equipButton.setVisible(false); // 預設不顯示裝備按鈕

            // 把裝備按鈕加入追蹤清單
            equipButtons.add(equipButton);

            // 水平排列按鈕
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(buyButton);
            buttonPanel.add(equipButton);

            // 購買按鈕狀態檢查
            buyButton.setEnabled(userData.getTokens() >= item.getPrice());

            // 購買邏輯
            buyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (userData.getTokens() >= item.getPrice() && userData.purchaseItem(item)) {
                        tokenPanel.updateTokens(-item.getPrice());
                        buyButton.setText("已購買");
                        buyButton.setEnabled(false);
                        equipButton.setVisible(true); // 顯示裝備按鈕
                        JOptionPane.showMessageDialog(null, "成功購買：" + item.getName());
                        updateButtons();
                    } else {
                        JOptionPane.showMessageDialog(null, "代幣不足！");
                    }
                }
            });

            // 裝備邏輯
            equipButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    equippedItem = item;
                    JOptionPane.showMessageDialog(null, "已裝備：" + item.getName());
                    updateEquipButtons();
                }
            });

            itemPanel.add(itemImage, BorderLayout.NORTH);
            itemPanel.add(itemLabel, BorderLayout.CENTER);
            itemPanel.add(buttonPanel, BorderLayout.SOUTH);
            add(itemPanel);
        }
    }

    // 更新購買按鈕
    public void updateButtons() {
        for (int i = 0; i < storeItems.size(); i++) {
            StoreItem item = storeItems.get(i);
            Component panel = getComponent(i);
            if (panel instanceof JPanel) {
                JPanel itemPanel = (JPanel) panel;
                for (Component comp : ((JPanel) itemPanel.getComponent(2)).getComponents()) {
                    if (comp instanceof JButton) {
                        JButton buyButton = (JButton) comp;
                        // if (buyButton.getText().equals("購買")) {
                        //     buyButton.setEnabled(userData.getTokens() >= item.getPrice());
                        // }
                    }
                }
            }
        }
    }

    // 更新裝備按鈕（只能裝備一個）
    public void updateEquipButtons() {
        for (int i = 0; i < storeItems.size(); i++) {
            StoreItem item = storeItems.get(i);
            JButton equipButton = equipButtons.get(i);
            if (equipButton.isVisible()) {
                equipButton.setEnabled(item != equippedItem);   //已裝備item的equipButton要黑掉
                equipButton.setText(item == equippedItem ? "已裝備" : "裝備");
            }
        }
    }
}
