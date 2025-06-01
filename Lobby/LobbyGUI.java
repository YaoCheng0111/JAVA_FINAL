package myPackage;

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
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LobbyGUI extends Application {

    private Button upButton;
    private Button downButton;
    private Button leftButton;
    private Button rightButton;
    private ImageView imageView;
    private StackPane root;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 請確保圖片路徑正確
        imageView = new ImageView(new Image("file:source/oiia_cat.jpg"));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);

        upButton = createDirectionButton("↑", e->showAlert("你點擊了上方按鈕"));
        downButton = createDirectionButton("鬧鐘/計時器", e->{
            TimerApp timerApp = new TimerApp();
            Stage newStage = new Stage();
            timerApp.start(newStage);
        });
        leftButton = createDirectionButton("←",e->showAlert("你點擊了左方按鈕"));
        rightButton = createDirectionButton("→",e->showAlert ("你點擊了右方按鈕"));
        upButton.setPrefSize(200, 50);
        downButton.setPrefSize(200, 50);
        leftButton.setPrefSize(50, 200);
        rightButton.setPrefSize(50, 200);

        // 包住圖片的 StackPane
        StackPane imageLayer = new StackPane(imageView);
        imageLayer.setPrefSize(300, 300);

        root = new StackPane(imageLayer, upButton, downButton, leftButton, rightButton);
        StackPane.setAlignment(upButton, Pos.TOP_CENTER);
        StackPane.setAlignment(downButton, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(leftButton, Pos.CENTER_LEFT);
        StackPane.setAlignment(rightButton, Pos.CENTER_RIGHT);

        hideDirectionButtons();

        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> toggleButtonsVisibility());

        Scene scene = new Scene(root, 400, 400);
        scene.getStylesheets().add(getClass().getResource("css/Lobbystyle.css").toExternalForm());// 引入CSS
        primaryStage.setTitle("Lobby GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
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
}
