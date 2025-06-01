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

        accessories.add(new StoreItem("sunglasses", 10, "source/sunglasses.png", 141, 115));
        accessories.add(new StoreItem("hat", 10, "source/hat.png", 137, 82));
        accessories.add(new StoreItem("necklace", 10, "source/necklace.png", 153, 160));
        accessories.add(new StoreItem("tie", 10, "source/tie.png", 143, 173));
        accessories.add(new StoreItem("bobble_hat", 10, "source/bobble_hat.png", 144, 63));
        accessories.add(new StoreItem("ball", 10, "source/ball.png", 70, 80));
        accessories.add(new StoreItem("bat", 10, "source/bat.png", 115, 75));
        accessories.add(new StoreItem("gold_necklace", 10, "source/gold_necklace.png", 155, 190));

        String equippedName = userData.getEquippedAccessory();
        if (equippedName != null) {
            for (StoreItem item : accessories) {
                if (item.getName().equals(equippedName)) {
                    equippedAccessory = item;
                    break;
                }
            }

        }

        setHgap(10);
        setVgap(10);

        int col = 0, row = 0;
        for (StoreItem accessory : accessories) {
            VBox box = new VBox(5);
            box.setAlignment(Pos.CENTER);
            box.getStyleClass().add("item-card");

            ImageView imageView = new ImageView(accessory.getJavaFXImage());
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            imageView.getStyleClass().add("image-view");

            Label nameLabel = new Label(accessory.getName() + " - " + accessory.getPrice() + " 代幣");

            Button buyButton = new Button("購買");
            buyButton.getStyleClass().add("button-buy");

            Button equipButton = new Button("裝備");
            equipButton.getStyleClass().add("button-equip");
            equipButton.setVisible(false);

            equipButtons.add(equipButton);

            buyButton.setDisable(userData.getTokens() < accessory.getPrice());

            buyButton.setOnAction(e -> {
                if (userData.getTokens() >= accessory.getPrice() && userData.purchaseItem(accessory)) {
                    userData.addTokens(-accessory.getPrice());
                    buyButton.setText("已購買");
                    buyButton.setDisable(true);
                    equipButton.setVisible(true);
                    userData.addOwnedAccessory(accessory);
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
                //userData.setEquippedItem(accessory.getName());
                userData.setEquippedAccessory(accessory.getName());
                updateEquipButtons();
                lobbyGUI.updateImage();
            });

            if (userData.hasItem(accessory.getName())) {
                buyButton.setText("已購買");
                buyButton.setDisable(true);
                equipButton.setVisible(true);
                if (accessory == equippedAccessory) {
                    equipButton.setText("已裝備");
                    equipButton.setDisable(true);
                    equipButton.getStyleClass().add("button-selected");
                } else {
                    equipButton.setText("裝備");
                    equipButton.setDisable(false);
                }
            }

            box.getChildren().addAll(imageView, nameLabel, buyButton, equipButton);
            add(box, col, row);

            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }
        updateEquipButtons();
    }

    public void updateEquipButtons() {
        for (int i = 0; i < accessories.size(); i++) {
            StoreItem item = accessories.get(i);
            Button equipButton = equipButtons.get(i);
            if (equipButton.isVisible()) {
                equipButton.setDisable(item == equippedAccessory);
                equipButton.setText(item == equippedAccessory ? "已裝備" : "裝備");
                equipButton.getStyleClass().remove("button-selected");
                if (item == equippedAccessory) {
                    equipButton.getStyleClass().add("button-selected");
                }
            }
        }
    }
}
