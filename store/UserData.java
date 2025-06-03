package myPackage;

import java.util.*;
import java.nio.file.*;
import java.io.File;
import java.io.IOException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import com.fasterxml.jackson.databind.node.ArrayNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;

public class UserData {

    private static final String SAVE_PATH = "JsonData/userData.json";

    private IntegerProperty tokens;
    private Set<String> purchasedItems;
    private Map<String, StoreItem> ownedAccessories;
    private String equippedItem;
    private String equippedAccessory;
    private StoreItem SI;

    public UserData() {
        this.tokens = new SimpleIntegerProperty(100); // 預設100代幣
        this.purchasedItems = new HashSet<>();
        this.ownedAccessories = new HashMap<>();
        this.equippedItem = null;
        this.equippedAccessory = null;

        loadFromJson(SAVE_PATH);
        //this.SI=SI;
    }

    public UserData(int initialTokens) {
        this.tokens = new SimpleIntegerProperty(initialTokens);
        this.purchasedItems = new HashSet<>();
        this.ownedAccessories = new HashMap<>();
        this.equippedItem = null;
        this.equippedAccessory = null;
        loadFromJson(SAVE_PATH);
        if (purchasedItems.isEmpty() && equippedItem == null) {
            purchasedItems.add("stand_cat");
            equippedItem = "stand_cat";
            System.out.println("預設啟用 stand_cat 並裝備");
            saveToJson(SAVE_PATH);
        }

    }

    // Tokens methods
    public int getTokens() {
        return tokens.get();
    }

    public void setTokens(int tokens) {
        this.tokens.set(tokens);
    }

    public IntegerProperty tokensProperty() {
        return tokens;
    }

    public void addTokens(int amount) {
        setTokens(getTokens() + amount);
        saveToJson(SAVE_PATH);
    }

    public void subtractTokens(int amount) {
        setTokens(getTokens() - amount);
        saveToJson(SAVE_PATH);
    }

    // Item methods
    public boolean hasItem(String itemName) {
        return purchasedItems.contains(itemName) || ownedAccessories.containsKey(itemName);
    }

    public StoreItem getEquippedAccessoryItem() {
        String name = getEquippedAccessory();
        return ownedAccessories.get(name);
    }

    public boolean purchaseItem(StoreItem item) {
        if (getTokens() >= item.getPrice() && !hasItem(item.getName())) {
            subtractTokens(item.getPrice());
            if (isAccessory(item.getName())) {
                purchaseAccessory(item);
            } else {
                purchaseItem(item.getName());
            }
            saveToJson(SAVE_PATH); // 每次購買後自動儲存
            return true;
        }
        return false;
    }

    private boolean isAccessory(String name) {
        // 判斷是否為飾品，以 ownedAccessories 裡有無此名稱判斷
        return ownedAccessories.containsKey(name);
    }

    public void purchaseItem(String itemName) {
        purchasedItems.add(itemName);
    }

    public void purchaseAccessory(StoreItem item) {
        ownedAccessories.put(item.getName(), item);
    }

    public void equipItem(String itemName) {
        this.equippedItem = itemName;
        saveToJson(SAVE_PATH);
    }

    public void equipAccessory(String accessoryName) {
        this.equippedAccessory = accessoryName;
        saveToJson(SAVE_PATH);
    }

    public String getEquippedItem() {
        return equippedItem;
    }

    public String getEquippedAccessory() {
        return equippedAccessory;
    }

    public Set<String> getPurchasedItems() {
        return purchasedItems;
    }

    public Map<String, StoreItem> getOwnedAccessories() {
        return ownedAccessories;
    }

    public void addOwnedAccessory(StoreItem item) {
        ownedAccessories.put(item.getName(), item);
    }

    public void setEquippedItem(String itemName) {
        this.equippedItem = itemName;
    }

    public void setEquippedAccessory(String accessoryName) {
        this.equippedAccessory = accessoryName;
    }

    // === JSON Save / Load ===
    public void saveToJson(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        root.put("tokens", getTokens());
        root.put("equippedItem", equippedItem != null ? equippedItem : "");
        root.put("equippedAccessory", equippedAccessory != null ? equippedAccessory : "");

        // 儲存已購買的 item 名稱清單
        root.putArray("purchasedItems").addAll(
                purchasedItems.stream()
                        .map(item -> mapper.convertValue(item, JsonNode.class))
                        .toList()
        );

        // 儲存 ownedAccessories 為物件陣列（包含所有屬性）
        ArrayNode accessoriesArray = mapper.createArrayNode();
        for (StoreItem item : ownedAccessories.values()) {
            ObjectNode accNode = mapper.createObjectNode();
            accNode.put("name", item.getName());
            accNode.put("price", item.getPrice());
            accNode.put("imagePath", item.getImagePath());
            accNode.put("offsetX", item.getOffsetX());
            accNode.put("offsetY", item.getOffsetY());
            accessoriesArray.add(accNode);
        }
        root.set("ownedAccessories", accessoriesArray);

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), root);
            System.out.println("已儲存 UserData");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //-----save-----
    //-----load-----
    public void loadFromJson(String filePath) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                System.out.println("找不到檔案，使用預設代幣100");
                return;
            }
            String content = new String(Files.readAllBytes(path));
            if (content.trim().isEmpty()) {
                System.out.println("檔案為空，使用預設代幣100");
                return;
            }

            JsonNode root = mapper.readTree(content);

            if (root.has("tokens") && root.get("tokens").isInt()) {
                tokens.set(root.get("tokens").asInt());
                System.out.println("讀取代幣數: " + tokens.get());
            } else {
                System.out.println("JSON中沒有tokens，使用預設代幣100");
            }

            equippedItem = root.path("equippedItem").asText(null);
            if ("".equals(equippedItem)) {
                equippedItem = null;
            }

            equippedAccessory = root.path("equippedAccessory").asText(null);
            if ("".equals(equippedAccessory)) {
                equippedAccessory = null;
            }

            // 讀取已購買的 item 名稱
            if (root.has("purchasedItems") && root.get("purchasedItems").isArray()) {
                purchasedItems.clear();
                for (JsonNode itemNode : root.get("purchasedItems")) {
                    purchasedItems.add(itemNode.asText());
                }
            }

            // 讀取 ownedAccessories（完整資訊）
            if (root.has("ownedAccessories") && root.get("ownedAccessories").isArray()) {
                ownedAccessories.clear();
                for (JsonNode accNode : root.get("ownedAccessories")) {
                    String name = accNode.path("name").asText();
                    int price = accNode.path("price").asInt(0);
                    String imagePath = accNode.path("imagePath").asText("");
                    double offsetX = accNode.path("offsetX").asDouble(0);
                    double offsetY = accNode.path("offsetY").asDouble(0);
                    ownedAccessories.put(name, new StoreItem(name, price, imagePath, offsetX, offsetY));
                }
            }

        } catch (IOException e) {
            System.out.println("讀檔錯誤，使用預設代幣100");
            e.printStackTrace();
        }
    }

}
