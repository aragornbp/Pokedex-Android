package com.example.mypokedex.models;

public class Pokemon {
    private String name;
    private int order;

    private Sprites sprites;
    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public Sprites getSprites() {
        return sprites;
    }
}
