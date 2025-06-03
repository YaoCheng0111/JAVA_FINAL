package myPackage;

import javax.script.Bindings;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;

public class MainStage extends Stage {

    private UserData userData;
    private TokenPane tokenPane;
    private ItemPane itemPane;
    private Button openAccessoryStoreButton;
    private LobbyGUI lobbyGUI;

    public MainStage(UserData userData, LobbyGUI lobbyGUI) {
        this.userData = userData;
        this.lobbyGUI = lobbyGUI;

        setTitle("商品商店");
        setWidth(800);
        setHeight(600);

        BorderPane root = new BorderPane();

        tokenPane = new TokenPane(userData);  // 各自擁有自己的 TokenPane，但都綁定同一個 userData
        itemPane = new ItemPane(userData, tokenPane, lobbyGUI);

        openAccessoryStoreButton = new Button("飾品商店");
        openAccessoryStoreButton.setOnAction(e -> {

            AccessoryStage accessoryStage = new AccessoryStage(userData, tokenPane, lobbyGUI);
            accessoryStage.show();
        });

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(itemPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:transparent;");

        root.setTop(tokenPane);
        root.setCenter(scrollPane);
        root.setBottom(openAccessoryStoreButton);

        tokenPane.getTokenLabel().textProperty().bind(userData.tokensProperty().asString());

        // 把 tokenPane 加入場景
        Scene scene = new Scene(root, 800, 600); //  正確設定 Scene
        scene.getStylesheets().add(getClass().getResource("/css/storeStyle.css").toExternalForm());
        setScene(scene);
        show();
    }

    public TokenPane getTokenPane() {
        return tokenPane;
    }
}
