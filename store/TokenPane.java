package myPackage;

//import javax.script.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.beans.binding.Bindings;

public class TokenPane extends HBox {

    private Label tokenLabel;
    private UserData userData;

    public TokenPane(UserData userData) {
        this.userData = userData;
        tokenLabel = new Label();
        setAlignment(Pos.CENTER);
        getChildren().add(tokenLabel);

        // 直接綁定
        tokenLabel.textProperty().bind(Bindings.format("代幣：%d", userData.tokensProperty()));

    }

    // 已不再需要手動 refresh
    public void updateTokens(int delta) {
        userData.addTokens(delta);
    }

    public void refresh() {
        // 不需要手動呼叫
    }

    public Label getTokenLabel() {
        return tokenLabel;
    }

}
