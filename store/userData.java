package myPackage;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import java.beans.PropertyChangeListener; // 
import java.beans.PropertyChangeSupport; //
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserData {

    private IntegerProperty tokens = new SimpleIntegerProperty();
    private Set<String> purchasedItems = new HashSet<>();
    private String equippedItem;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private List<Runnable> tokenListeners = new ArrayList<>();
    private StringProperty equippedAccessory = new SimpleStringProperty("");
    private Map<String, StoreItem> ownedAccessories = new HashMap<>();

    //private StringProperty equippedItem = new SimpleStringProperty();
    public UserData(int tokens) {
        this.tokens.set(tokens);
    }

    public int getTokens() {
        return tokens.get();
    }

    public void addTokens(int amount) {
        int oldTokens = tokens.get();
        tokens.set(oldTokens + amount);
        changes.firePropertyChange("tokens", oldTokens, tokens.get()); //  **確保 UI 更新**
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

    public String getEquippedAccessory() {
        return equippedAccessory.get();
    }

    public void setEquippedAccessory(String name) {
        equippedAccessory.set(name);
    }

    public StringProperty equippedAccessoryProperty() {
        return equippedAccessory;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener); //  **讓 UI 監聽代幣變化**
    }

    public void addOwnedAccessory(StoreItem item) {
        ownedAccessories.put(item.getName(), item);
    }

    public StoreItem getEquippedAccessoryItem() {
        String name = getEquippedAccessory();
        return ownedAccessories.get(name);
    }
}
