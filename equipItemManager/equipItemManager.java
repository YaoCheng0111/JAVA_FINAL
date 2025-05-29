package myPackage;

import javafx.scene.image.Image;

public class equipItemManager {

    private static StoreItem equippedItem;

    public static void setEquippedItem(StoreItem item) {
        equippedItem = item;
    }

    public static StoreItem getEquippedItem() {
        return equippedItem;
    }

    public static Image getEquippedJavaFXImage() {
        if (equippedItem != null) {
            return equippedItem.getJavaFXImage(); // 確保 StoreItem 有這個方法
        } else {
            // 預設圖片
            return new Image("file:source/stand_cat.png");
        }
    }
}

