package myPackage;

import javax.swing.*;
// import myPackage.equipItemManager;
// import myPackage.mainFrame;

class StoreItem {

    private String name;
    private int price;
    private ImageIcon image; //  ImageIcon 

    public StoreItem(String name, int price, String imagePath) {
        this.name = name;
        this.price = price;
        this.image = new ImageIcon(imagePath); //
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public ImageIcon getImage() {
        return image;
    }
}
