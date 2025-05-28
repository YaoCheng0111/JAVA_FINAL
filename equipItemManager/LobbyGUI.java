package myPackage;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import timerApp.AlarmDialog;
import timerApp.CountdownTimeDialog;

public class LobbyGUI extends Application {

    private ImageView imageLabel;
    private HBox southPanel;
    private Button downButton;
    private Button alarmButton;
    private Button timerButton;
    private UserData userData;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Desktop Assistant");
        userData = new UserData(100);

        // 圖片初始化
        Image defaultImage = new Image("file:source/oiia_cat.jpg");
        imageLabel = new ImageView(scaleImage(defaultImage, 200, 200));
        imageLabel.setPreserveRatio(true);

        Button upButton = new Button("Button1");
        downButton = new Button("時間");
        Button leftButton = new Button("Button3");
        Button rightButton = new Button("Button4");
        alarmButton = new Button("鬧鐘");
        timerButton = new Button("倒數計時");

        upButton.setVisible(false);
        downButton.setVisible(false);
        leftButton.setVisible(false);
        rightButton.setVisible(false);

        BorderPane centerPanel = new BorderPane();
        centerPanel.setCenter(imageLabel);
        centerPanel.setTop(upButton);
        centerPanel.setLeft(leftButton);
        centerPanel.setRight(rightButton);

        southPanel = new HBox(10);
        southPanel.setAlignment(Pos.CENTER);
        southPanel.getChildren().add(downButton);
        centerPanel.setBottom(southPanel);

        // hover 時切換
        downButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            southPanel.getChildren().setAll(alarmButton, timerButton);
        });

        alarmButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> restoreTimeButton());
        timerButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> restoreTimeButton());

        alarmButton.setOnAction(e -> new AlarmDialog().show());
        timerButton.setOnAction(e -> new CountdownTimeDialog().show());

        imageLabel.setOnMouseClicked(e -> {
            boolean visible = !upButton.isVisible();
            upButton.setVisible(visible);
            downButton.setVisible(visible);
            leftButton.setVisible(visible);
            rightButton.setVisible(visible);
        });

        upButton.setOnAction(e -> new mainFrame(userData).start(new Stage()));

        StoreItem equipped = equipItemManager.getEquippedItem();
        if (equipped != null) {
            imageLabel.setImage(scaleImage(equipped.getImage(), 200, 200));
        }

        equipItemManager.setOnEquipChanged(() -> {
            StoreItem newItem = equipItemManager.getEquippedItem();
            if (newItem != null) {
                imageLabel.setImage(scaleImage(newItem.getImage(), 200, 200));
            }
        });

        Scene scene = new Scene(centerPanel, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void restoreTimeButton() {
        southPanel.getChildren().setAll(downButton);
    }

    private Image scaleImage(Image image, int maxWidth, int maxHeight) {
        double width = image.getWidth();
        double height = image.getHeight();
        double ratio = Math.min(maxWidth / width, maxHeight / height);
        return new Image(image.getUrl(), width * ratio, height * ratio, true, true);
    }
}
