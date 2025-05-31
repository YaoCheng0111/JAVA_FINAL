package myPackage;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AccessoryStage extends Stage {

    private UserData userData;
    private TokenPane tokenPane;
    private AccessoryPane accessoryPane;
    private LobbyGUI lobbyGUI;

    public AccessoryStage(UserData userData, TokenPane tokenPane, LobbyGUI lobbyGUI) {
        this.userData = userData;
        this.tokenPane = tokenPane; // **使用共享 TokenPane**
        this.lobbyGUI = lobbyGUI;

        setTitle("飾品商店");
        setWidth(800);
        setHeight(600);

        BorderPane root = new BorderPane();

        // **建立 AccessoryPane，並使用共享 TokenPane**
        accessoryPane = new AccessoryPane(userData, tokenPane, lobbyGUI);

        // **確保 TokenPane 正確顯示**
        root.setTop(tokenPane);
        root.setCenter(accessoryPane);

        Scene scene = new Scene(root, 800, 600); //  **修正 Scene 設置**
        scene.getStylesheets().add(getClass().getResource("css/storeStyle.css").toExternalForm());
        setScene(scene);
        show(); //  視窗顯示
    }
}
