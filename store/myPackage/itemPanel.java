package myPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class ItemPanel extends JPanel {

    private UserData userData;
    private ArrayList<StoreItem> storeItems;

    public ItemPanel(TokenPanel tokenPanel) {
        this.userData = new UserData(100);
        this.storeItems = new ArrayList<>();

        storeItems.add(new StoreItem("huh", 50, "myPackage/images/huh_cat.jpg"));
        storeItems.add(new StoreItem("cute", 50, "myPackage/images/Cute_cat.jpg"));
        storeItems.add(new StoreItem("pop", 50, "myPackage/images/pop_cat.png"));
        storeItems.add(new StoreItem("okay", 50, "myPackage/images/okay_cat.jpg"));
        storeItems.add(new StoreItem("serious", 50, "myPackage/images/serious_cat.jpg"));
        storeItems.add(new StoreItem("oiia", 50, "myPackage/images/oiia_cat.jpg"));

        setLayout(new GridLayout(2, 3, 10, 10)); // row=2,col=3排列

        for (StoreItem item : storeItems) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BorderLayout());

            // 調整圖片大小，確保顯示完整
            ImageIcon originalIcon = item.getImage();
            Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(scaledImage);

            JLabel itemImage = new JLabel(resizedIcon);
            JLabel itemLabel = new JLabel(item.getName() + " - " + item.getPrice() + " 代幣", JLabel.CENTER);

            JButton buyButton = new JButton("購買");

            // **初始化時，檢查是否有足夠代幣**
            buyButton.setEnabled(userData.getTokens() >= item.getPrice());

            buyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (userData.getTokens() >= item.getPrice() && userData.purchaseItem(item)) {
                        tokenPanel.updateTokens(-item.getPrice());
                        buyButton.setText("已購買");
                        buyButton.setEnabled(false);
                        JOptionPane.showMessageDialog(null, "成功購買：" + item.getName());

                        updateButtons();
                    } else {
                        JOptionPane.showMessageDialog(null, "代幣不足！");
                    }
                }
            });

            itemPanel.add(itemImage, BorderLayout.NORTH);
            itemPanel.add(itemLabel, BorderLayout.CENTER);
            itemPanel.add(buyButton, BorderLayout.SOUTH);

            add(itemPanel);
        }

    }

    public void updateButtons() {
        for (Component component : getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                JButton buyButton = null;
                int itemPrice = 0;

                for (Component child : panel.getComponents()) {
                    if (child instanceof JButton) {
                        buyButton = (JButton) child;
                    }
                    if (child instanceof JLabel) {
                        String text = ((JLabel) child).getText();
                        for (StoreItem item : storeItems) {
                            if (text.contains(item.getName())) {
                                itemPrice = item.getPrice();
                                break;
                            }
                        }
                    }
                }

                if (buyButton != null) {
                    buyButton.setEnabled(userData.getTokens() >= itemPrice);
                }
            }
        }

    }

}
