package myPackage;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class ItemPane extends GridPane {

    private UserData userData;
    private ArrayList<StoreItem> items;
    private StoreItem equippedItem = null;
    private ArrayList<Button> equipButtons = new ArrayList<>();
    private TokenPane tokenPane;
    private LobbyGUI lobbyGUI;

    public ItemPane(UserData userData, TokenPane tokenPane, LobbyGUI lobbyGUI) {
        this.userData = userData;
        this.tokenPane = tokenPane;
        this.lobbyGUI = lobbyGUI;
        items = new ArrayList<>();

        items.add(new StoreItem("stand_cat", 0, "source/stand_cat.png", 100, 100));
        items.add(new StoreItem("new_cat", 50, "source/new_cat.png", 100, 100));
        items.add(new StoreItem("red_cat", 50, "source/red_cat.png", 100, 100));
        items.add(new StoreItem("rainbow_cat", 50, "source/rainbow_cat.png", 100, 100));
        items.add(new StoreItem("RGB_cat", 50, "source/RGB_cat.png", 100, 100));
        items.add(new StoreItem("RGB2_cat", 50, "source/RGB2_cat.png", 100, 100));

        String equippedName = userData.getEquippedItem();
        for (StoreItem item : items) {
            if (item.getName().equals(equippedName)) {
                equippedItem = item;
                break;
            }
        }

        setHgap(10);
        setVgap(10);
        getStyleClass().add("store-grid");

        int col = 0, row = 0;
        for (StoreItem item : items) {
            VBox box = new VBox(5);
            box.setAlignment(Pos.CENTER);
            box.getStyleClass().add("store-box");

            ImageView imageView = new ImageView(item.getJavaFXImage());
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);

            Label nameLabel = new Label(item.getName() + " - " + item.getPrice() + " 代幣");
            nameLabel.getStyleClass().add("item-label");

            Button buyButton = new Button("購買");
            Button equipButton = new Button("裝備");
            buyButton.getStyleClass().add("buy-button");
            equipButton.getStyleClass().add("equip-button");

            equipButton.setVisible(false);
            equipButtons.add(equipButton);

            buyButton.setDisable(userData.getTokens() < item.getPrice());

            buyButton.setOnAction(e -> {
                if (userData.getTokens() >= item.getPrice() && userData.purchaseItem(item)) {
                    //userData.addTokens(-item.getPrice());
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
                equippedItem = item;
                userData.equipItem(item.getName());
                updateEquipButtons();
                lobbyGUI.updateImage();
            });

            if (userData.hasItem(item.getName())) {
                buyButton.setText("已購買");
                buyButton.setDisable(true);
                equipButton.setVisible(true);
                if (item == equippedItem) {
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

    public void updateEquipButtons() {
        for (int i = 0; i < items.size(); i++) {
            StoreItem item = items.get(i);
            Button equipButton = equipButtons.get(i);
            if (equipButton.isVisible()) {
                equipButton.setDisable(item == equippedItem);
                equipButton.setText(item == equippedItem ? "已裝備" : "裝備");
            }
        }
    }
}
