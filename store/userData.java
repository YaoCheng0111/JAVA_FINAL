package myPackage;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.beans.PropertyChangeListener; // 
import java.beans.PropertyChangeSupport; //
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserData {

    private IntegerProperty tokens = new SimpleIntegerProperty();
    private Set<String> purchasedItems = new HashSet<>();
    private String equippedItem;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private List<Runnable> tokenListeners = new ArrayList<>();

    public UserData(int tokens) {
        this.tokens.set(tokens);
    }

    public int getTokens() {
        return tokens.get();
    }

    public void addTokens(int amount) {
        int oldTokens = tokens.get();
        tokens.set(oldTokens + amount);
        changes.firePropertyChange("tokens", oldTokens, tokens.get()); // ✅ **確保 UI 更新**
    }

    public IntegerProperty tokensProperty() {
        return tokens;
    }

    public boolean purchaseItem(StoreItem item) {
        if (getTokens() >= item.getPrice()) {
            purchasedItems.add(item.getName());
            return true;
        }
        return false;
    }

    public boolean hasItem(String itemName) {
        return purchasedItems.contains(itemName);
    }

    public String getEquippedItem() {
        return equippedItem;
    }

    public void setEquippedItem(String name) {
        equippedItem = name;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener); // ✅ **讓 UI 監聽代幣變化**
    }
}
