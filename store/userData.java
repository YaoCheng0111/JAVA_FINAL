package myPackage;

import java.util.HashSet;
import java.util.prefs.Preferences;
import java.util.ArrayList;

class UserData {

    private int tokens;     //後端紀錄的tokens數
    private HashSet<String> ownedItems;
    private String equippedItem;
    private Preferences prefs;
    private ArrayList<TokenListener> listeners = new ArrayList<>();

    public UserData(int initialTokens) {
        this.tokens = initialTokens;
        this.ownedItems = new HashSet<>();
        this.prefs = Preferences.userNodeForPackage(UserData.class);
        this.equippedItem = prefs.get("equippedItem", "oiia");
    }

    public int getTokens() {

        return tokens;

    }

    public boolean purchaseItem(StoreItem item) {
        if (tokens < item.getPrice()) {
            return false; // 代幣不足，無法購買
        }
        tokens -= item.getPrice();
        ownedItems.add(item.getName());
        notifyTokenChanged();
        return true;
    }

    public boolean hasItem(String itemName) {
        return ownedItems.contains(itemName);
    }

    public void addTokens(int amount) {
        tokens += amount;
        notifyTokenChanged();
    }

    public void setEquippedItem(String itemName) {
        if (hasItem(itemName)) {
            this.equippedItem = itemName;
            prefs.put("equippedItem", itemName); // 存入本地
        }
    }

    public String getEquippedItem() {
        return equippedItem;
    }

    public void addTokenListener(TokenListener listener) {
        listeners.add(listener);
    }

    private void notifyTokenChanged() {
        for (TokenListener listener : listeners) {
            listener.onTokenChanged(tokens);
        }
    }
}
