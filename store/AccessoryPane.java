package myPackage;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.ArrayList;

public class AccessoryPane extends GridPane {

    private UserData userData;
    private ArrayList<StoreItem> accessories;
    private StoreItem equippedAccessory = null;
    private ArrayList<Button> equipButtons = new ArrayList<>();
    private TokenPane tokenPane;
    private LobbyGUI lobbyGUI;

    public AccessoryPane(UserData userData, TokenPane tokenPane, LobbyGUI lobbyGUI) {
        this.userData = userData;
        this.tokenPane = tokenPane;
        this.lobbyGUI = lobbyGUI;
        this.accessories = new ArrayList<>();

        // 初始化配件清單
        accessories.add(new StoreItem("sunglasses", 10, "source/sunglasses.png"));
        accessories.add(new StoreItem("hat", 10, "source/hat.png"));
        accessories.add(new StoreItem("necklace", 10, "source/necklace.png"));
        accessories.add(new StoreItem("shoes", 10, "source/shoes.png"));
        accessories.add(new StoreItem("watch", 10, "source/watch.png"));
        accessories.add(new StoreItem("belt", 10, "source/belt.png"));

        // 讀取已裝備的配件
        String equippedName = userData.getEquippedItem();
        for (StoreItem item : accessories) {
            if (item.getName().equals(equippedName)) {
                equippedAccessory = item;
                break;
            }
        }

        setHgap(10);
        setVgap(10);

        int col = 0, row = 0;
        for (StoreItem accessory : accessories) {
            VBox box = new VBox(5);
            box.setAlignment(Pos.CENTER);

            ImageView imageView = new ImageView(accessory.getJavaFXImage());
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);

            Label nameLabel = new Label(accessory.getName() + " - " + accessory.getPrice() + " 代幣");

            Button buyButton = new Button("購買");
            Button equipButton = new Button("裝備");
            equipButton.setVisible(false);

            equipButtons.add(equipButton);

            // 購買按鈕初始狀態
            buyButton.setDisable(userData.getTokens() < accessory.getPrice());

            buyButton.setOnAction(e -> {
                if (userData.getTokens() >= accessory.getPrice() && userData.purchaseItem(accessory)) {
                    userData.addTokens(-accessory.getPrice());  // 這裡才是真正扣代幣！
                    buyButton.setText("已購買");
                    buyButton.setDisable(true);
                    equipButton.setVisible(true);
                    updateEquipButtons();
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("提醒");
                    alert.setHeaderText(null);
                    alert.setContentText("代幣不足！");
                    alert.showAndWait();
                }
            });

            equipButton.setOnAction(e -> {
                equippedAccessory = accessory;
                userData.setEquippedItem(accessory.getName());
                updateEquipButtons();
                lobbyGUI.updateImage(); // 更新畫面
            });

            // 如果已購買，更新按鈕狀態
            if (userData.hasItem(accessory.getName())) {
                buyButton.setText("已購買");
                buyButton.setDisable(true);
                equipButton.setVisible(true);
                if (accessory == equippedAccessory) {
                    equipButton.setText("已裝備");
                    equipButton.setDisable(true);
                } else {
                    equipButton.setText("裝備");
                    equipButton.setDisable(false);
                }
            }

            box.getChildren().addAll(imageView, nameLabel, buyButton, equipButton);
            add(box, col, row);

            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }
    }

    // 更新裝備按鈕狀態
    public void updateEquipButtons() {
        for (int i = 0; i < accessories.size(); i++) {
            StoreItem item = accessories.get(i);
            Button equipButton = equipButtons.get(i);
            if (equipButton.isVisible()) {
                equipButton.setDisable(item == equippedAccessory);
                equipButton.setText(item == equippedAccessory ? "已裝備" : "裝備");
            }
        }
    }
}
