package com.codecool.dungeoncrawl.logic.items;

public enum WeaponType {
    THUNDERFURY("Thunderfury", 8),
    MJOLNIR("Mjolnir", 12),
    SCYTHE("The Grim Reaper", 16),
    STORMBREAKER("Stormbreaker", 20),
    FROSTMOURNE("Frostmourne", 100),
    STICK("Stick of Truth", 420);


    private final String name;
    private final int damage;

    WeaponType(String name, int damage) {
        this.name = name;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

}
