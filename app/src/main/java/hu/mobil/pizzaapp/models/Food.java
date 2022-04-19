package hu.mobil.pizzaapp.models;

import java.io.Serializable;

public class Food implements Serializable {
    private String category;
    private String name;
    private String description;
    private int price;
    private String imageSrc;

    public Food() {
    }

    public Food(String category, String name, String description, int price, String imageSrc) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageSrc = imageSrc;
    }

    public Food(Food food) {
        this.category = food.category;
        this.name = food.name;
        this.description = food.description;
        this.price = food.price;
        this.imageSrc = food.imageSrc;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getImageSrc() {
        return imageSrc;
    }
}
