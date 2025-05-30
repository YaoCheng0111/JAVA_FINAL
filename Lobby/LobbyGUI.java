package myPackage;

import java.util.Stack;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.scene.layout.Pane;

public class LobbyGUI extends Application {

    private Button upButton;
    private Button downButton;
    private Button leftButton;
    private Button rightButton;
    private ImageView imageView;
    private StackPane root;
    private UserData userData;
    private TokenPane tokenPane;
    private ImageView accessoryImageView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        userData = new UserData(100);
        tokenPane = new TokenPane(userData);

        imageView = new ImageView(new Image("file:source/stand_cat.png"));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setLayoutX(100);
        imageView.setLayoutY(100);

        accessoryImageView = new ImageView();
        accessoryImageView.setFitWidth(100);
        accessoryImageView.setFitHeight(100);

        //accessoryImageView.setLayoutX()
        Pane imageLayer = new Pane(imageView, accessoryImageView);
        updateImage();

        upButton = createDirectionButton("↑", e -> {
            MainStage mainStage = new MainStage(userData, this);
            mainStage.show();

        });
        downButton = createDirectionButton("↓", "你點擊了下方按鈕");
        leftButton = createDirectionButton("←", "你點擊了左方按鈕");
        rightButton = createDirectionButton("→", "你點擊了右方按鈕");

        upButton.setPrefSize(200, 50);
        downButton.setPrefSize(200, 50);
        leftButton.setPrefSize(50, 200);
        rightButton.setPrefSize(50, 200);

        //StackPane imageLayer = new StackPane(imageView);
        imageLayer.setPrefSize(300, 300);

        root = new StackPane(imageLayer, upButton, downButton, leftButton, rightButton);
        StackPane.setAlignment(upButton, Pos.TOP_CENTER);
        StackPane.setAlignment(downButton, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(leftButton, Pos.CENTER_LEFT);
        StackPane.setAlignment(rightButton, Pos.CENTER_RIGHT);

        hideDirectionButtons();

        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> toggleButtonsVisibility());

        Scene scene = new Scene(root, 400, 400);
        scene.getStylesheets().add(getClass().getResource("css/Lobbystyle.css").toExternalForm());
        primaryStage.setTitle("Lobby GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createDirectionButton(String label, String message) {
        Button button = new Button(label);
        button.setVisible(false);
        button.setOnAction(e -> showAlert(message));
        return button;
    }

    private Button createDirectionButton(String label, EventHandler<ActionEvent> handler) {
        Button button = new Button(label);
        button.setVisible(false);
        button.setOnAction(handler);
        return button;
    }

    private void toggleButtonsVisibility() {
        boolean showing = upButton.isVisible();
        upButton.setVisible(!showing);
        downButton.setVisible(!showing);
        leftButton.setVisible(!showing);
        rightButton.setVisible(!showing);
    }

    private void hideDirectionButtons() {
        upButton.setVisible(false);
        downButton.setVisible(false);
        leftButton.setVisible(false);
        rightButton.setVisible(false);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("按鈕點擊");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * 同步裝備圖片用 *
     */
    public void updateImage() {
        String equipped = userData.getEquippedItem();
        if (equipped != null) {
            Image image = new Image("file:source/" + equipped + ".png");
            imageView.setImage(image);
        }

        // 飾品圖
        String accessory = userData.getEquippedAccessory();
        StoreItem accessoryItem = userData.getEquippedAccessoryItem();
        if (accessory != null && !accessory.isEmpty()) {        //
            if (accessory.equals("tie")) {
                accessoryImageView.setImage(new Image("file:source/tie_equip.png"));
                accessoryImageView.setFitWidth(100);
                accessoryImageView.setFitHeight(100);
            } else if (accessory.equals("necklace")) {
                accessoryImageView.setImage(new Image("file:source/necklace2.png"));
                accessoryImageView.setFitWidth(80);
                accessoryImageView.setFitHeight(80);

            } else {
                accessoryImageView.setImage(new Image("file:source/" + accessory + ".png"));
                accessoryImageView.setFitWidth(100);
                accessoryImageView.setFitHeight(100);
            }
            accessoryImageView.setLayoutX(accessoryItem.getOffsetX());
            accessoryImageView.setLayoutY(accessoryItem.getOffsetY());

        } else {
            accessoryImageView.setImage(null);
        }
    }

    public UserData getUserData() {
        return userData;
    }

    public TokenPane getTokenPane() {
        return tokenPane;
    }
}
