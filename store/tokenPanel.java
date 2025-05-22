package myPackage;

import javax.swing.*;

class TokenPanel extends JPanel {

    private JLabel tokenLabel;
    private int tokens = 100; // 初始代幣數量(跟userDate的要同步)=>前端給使用者看的tokens數

    public TokenPanel() {
        tokenLabel = new JLabel("代幣: " + tokens);
        add(tokenLabel);
    }

    public void updateTokens(int amount) {
        if (tokens + amount >= 0) { // 確保代幣不會變成負數
            tokens += amount;
        }
        tokenLabel.setText("代幣: " + tokens);
    }

    public int getTokens() {
        return tokens;
    }
}
