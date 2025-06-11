package myPackage;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class LobbyGUI extends Application {

    private Button Button_1;
    private Button Button_2;
    private Button Button_3;
    private Button Button_4;
    private ImageView imageView;
    private ImageView accessoryImageView;
    private UserData userData;
    private TokenPane tokenPane;

    private double xOffset = 0;
    private double yOffset = 0;
    private double dragStartX, dragStartY;
    private boolean dragged = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        userData = new UserData(100);
        tokenPane = new TokenPane(userData);

        //工作列icon
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Resource/images/stand_cat.png")));

        // 主角貓咪圖片
        imageView = new ImageView(new Image("Resource/images/stand_cat.png"));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);

        // 飾品圖片
        accessoryImageView = new ImageView();
        accessoryImageView.setFitWidth(100);
        accessoryImageView.setFitHeight(100);

        // 圖片層，用 Pane 方便自由定位飾品
        Pane imageLayer = new Pane(imageView, accessoryImageView);
        imageLayer.setPrefSize(220, 220);

        updateImage();

        // 建立按鈕及事件
        Button_1 = createDirectionButton("代幣商店", e -> {
            MainStage mainStage = new MainStage(userData, this);
            mainStage.show();
        });

        Button_2 = createDirectionButton("鬧鐘/計時器", e -> {            
            TimerApp timerApp = new TimerApp();
            Stage newStage = new Stage();
            timerApp.start(newStage);
        });

        Button_3 = createDirectionButton("習慣追蹤器", e -> {
            HabitTrackerApp habitTrackerApp = new HabitTrackerApp();
            Stage newStage = new Stage();
            habitTrackerApp.start(newStage, userData);
        });

        Button_4 = createDirectionButton("關閉程式", e -> {
            System.exit(0);
        });

        // 按鈕尺寸統一
        Button_1.setPrefWidth(150);
        Button_2.setPrefWidth(150);
        Button_3.setPrefWidth(150);
        Button_4.setPrefWidth(150);
        Button_4.getStyleClass().add("ShutDown-Button");

        // 按鈕垂直排列，間距10px
        VBox buttonBox = new VBox(10, Button_1, Button_2, Button_3, Button_4);
        buttonBox.setAlignment(Pos.TOP_CENTER);
        buttonBox.setPrefWidth(170);

        // 主容器橫向排列，左圖右按鈕
        HBox root = new HBox(0, imageLayer, buttonBox);
        root.setStyle("-fx-background-color: transparent;");
        root.setPrefSize(400, 250);
        root.setAlignment(Pos.CENTER_LEFT);

        hideDirectionButtons();

        // 點擊貓咪圖片切換按鈕顯示
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> toggleButtonsVisibility());

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        // 讓視窗可以拖曳
        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        
        scene.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        imageView.setOnMousePressed(e -> {
            dragStartX = e.getSceneX() - imageView.getLayoutX();
            dragStartY = e.getSceneY() - imageView.getLayoutY();
            dragged = true;
        });

        imageView.setOnMouseDragged(e -> {
            imageView.setLayoutX(e.getSceneX() - dragStartX);
            imageView.setLayoutY(e.getSceneY() - dragStartY);
            dragged = false;
        });

        imageView.setOnMouseReleased(e -> {
            if (!dragged) {
                toggleButtonsVisibility();
            }
        });

        scene.getStylesheets().add(getClass().getResource("/css/Lobbystyle.css").toExternalForm());

        primaryStage.setTitle("Lobby GUI");
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();

        // 視窗靠右下角
        Screen screen = Screen.getPrimary(); 
        Rectangle2D bounds = screen.getVisualBounds(); 

        primaryStage.setX(bounds.getMaxX() - primaryStage.getWidth());
        primaryStage.setY(bounds.getMaxY() - primaryStage.getHeight());
    }

    private Button createDirectionButton(String label, EventHandler<ActionEvent> handler) {
        Button button = new Button(label);
        button.setVisible(false);
        button.setOnAction(handler);
        return button;
    }

    private void toggleButtonsVisibility() {
        boolean showing = Button_1.isVisible();
        Button_1.setVisible(!showing);
        Button_2.setVisible(!showing);
        Button_3.setVisible(!showing);
        Button_4.setVisible(!showing);
    }

    private void hideDirectionButtons() {
        Button_1.setVisible(false);
        Button_2.setVisible(false);
        Button_3.setVisible(false);
        Button_4.setVisible(false);
    }

    /*-----同步store圖片跟Lobby圖片用----- */
    public void updateImage() {
        String equipped = userData.getEquippedItem();
        if (equipped != null) {
            Image image = new Image("Resource/images/" + equipped + ".png");
            imageView.setImage(image);
        }

        // 飾品圖
        String accessory = userData.getEquippedAccessory();
        StoreItem accessoryItem = userData.getEquippedAccessoryItem();
        if (accessory != null && !accessory.isEmpty()) {
            if (accessory.equals("tie")) {
                accessoryImageView.setImage(new Image("Resource/images/tie_equip.png"));
                accessoryImageView.setFitWidth(100);
                accessoryImageView.setFitHeight(100);
            } else if (accessory.equals("necklace")) {
                accessoryImageView.setImage(new Image("Resource/images/necklace2.png"));
                accessoryImageView.setFitWidth(80);
                accessoryImageView.setFitHeight(80);
            } else if (accessory.equals("gold_necklace")) {
                accessoryImageView.setImage(new Image("Resource/images/" + accessory + ".png"));
                accessoryImageView.setFitWidth(80);
                accessoryImageView.setFitHeight(80);
            }else if (accessory.equals("ball")) {
                accessoryImageView.setImage(new Image("Resource/images/" + accessory + ".png"));                
                accessoryImageView.setFitWidth(50);
                accessoryImageView.setFitHeight(50);
            }else if (accessory.equals("bat")) {
                accessoryImageView.setImage(new Image("Resource/images/" + accessory + ".png"));                
                accessoryImageView.setFitWidth(70);
                accessoryImageView.setFitHeight(70);
            }else if (accessory.equals("bobble_hat")) {
                accessoryImageView.setImage(new Image("Resource/images/" + accessory + ".png"));                
                accessoryImageView.setFitWidth(50);
                accessoryImageView.setFitHeight(50);
            }else {
                accessoryImageView.setImage(new Image("Resource/images/" + accessory + ".png"));
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
