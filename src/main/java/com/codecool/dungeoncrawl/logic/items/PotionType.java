package com.codecool.dungeoncrawl.logic.items;

public enum PotionType {POTION("Potion", 5 ),
                        BIG_POTION("Big Potion", 10);

    private final String name;
    private final int healthRegen;

    PotionType(String name, int healthRegen) {
        this.name = name;
        this. healthRegen = healthRegen;
    }

    public String getName() {
        return name;
    }

    public int getHealthRegen() {
        return healthRegen;
    }
}
