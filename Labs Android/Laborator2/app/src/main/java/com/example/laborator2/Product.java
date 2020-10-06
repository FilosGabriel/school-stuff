package com.example.laborator2;

import java.io.Serializable;

public class Product implements Serializable {
    private String name;
    private String price;
    private String size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product(String name, String price, String size) {
        this.name = name;
        this.price = price;
        this.size = size;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    public Product(String name, Double price, double size) {
        this.name = name;
        this.price = price.toString();
        this.size = String.valueOf(size);
    }


}

