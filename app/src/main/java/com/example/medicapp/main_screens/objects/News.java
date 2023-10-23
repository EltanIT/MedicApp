package com.example.medicapp.main_screens.objects;

public class News {
    private String id;
    private String title;
    private String description;
    private int price;
    private String image;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public News(String id, String title, String description, int price, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.image = image;
    }
}
