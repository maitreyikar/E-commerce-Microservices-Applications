package com.mymart.userservice.model;

//import jakarta.persistence.*;

public class Product {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String name;
    private double price;

    public Product(){}

    public Product(Long id, String brand, String name, double price){
        this.id = id;
        this.brand = brand;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString(){
        return "{" + String.valueOf(this.id) + ", " + this.brand + ", " + this.name + ", " + String.valueOf(this.price) + "}";
    }
}
