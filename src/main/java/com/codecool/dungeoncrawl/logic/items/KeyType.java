package com.codecool.dungeoncrawl.logic.items;

public enum KeyType {
    IRON_KEY("Iron Key"),
    GOLDEN_KEY("Golden key");

    private final String name;

    KeyType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
