package myPackage;

public class equipItemManager {

    private static StoreItem equippedItem;
    private static Runnable onEquipChanged;

    public static void setEquippedItem(StoreItem item) {
        equippedItem = item;
        if (onEquipChanged != null) {
            onEquipChanged.run(); // 通知界面更新圖片
        }
    }

    public static StoreItem getEquippedItem() {
        return equippedItem;
    }

    public static void setOnEquipChanged(Runnable listener) {
        onEquipChanged = listener;
    }
}
