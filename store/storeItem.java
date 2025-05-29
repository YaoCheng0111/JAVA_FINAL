package myPackage;

import javafx.scene.image.Image;

public class StoreItem {

    private String name;
    private int price;
    private String imagePath;

    public StoreItem(String name, int price, String imagePath) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Image getJavaFXImage() {
        return new Image("file:" + imagePath);
    }
}
