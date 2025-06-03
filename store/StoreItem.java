package myPackage;

import javafx.scene.image.Image;

public class StoreItem {

    private String name;
    private int price;
    private String imagePath;

    private double offsetX;
    private double offsetY;

    public StoreItem(String name, int price, String imagePath, double offsetX, double offsetY) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Image getJavaFXImage() {
        return new Image(imagePath);
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }
}
