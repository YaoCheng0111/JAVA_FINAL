package myPackage;

import java.util.HashSet;

class UserData {

    private int tokens;
    private HashSet<String> ownedItems;

    public UserData(int initialTokens) {
        this.tokens = initialTokens;
        this.ownedItems = new HashSet<>();
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
        return true;
    }

    public boolean hasItem(String itemName) {
        return ownedItems.contains(itemName);
    }

    public void addTokens(int amount) {
        tokens += amount;
    }
}
