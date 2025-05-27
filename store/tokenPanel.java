package myPackage;

import javax.swing.*;
import org.w3c.dom.UserDataHandler;

class TokenPanel extends JPanel {

    private JLabel tokenLabel;
    private int tokens; // 初始代幣數量(跟userDate的要同步)=>前端給使用者看的tokens數
    private UserData userData;

    public TokenPanel(UserData userData) {
        this.userData = userData;
        this.tokens = userData.getTokens();
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
