package com.codecool.dungeoncrawl.logic.items;

public enum WeaponType {
    THUNDERFURY("Thunderfury", 60),
    FROSTMOURNE("Frostmourne", 500),
    SCYTHE("The Grim Reaper", 30),
    MJOLNIR("Mjolnir", 60),
    STORMBREAKER("Stormbreaker", 100),
    STICK("Stick of Truth", 7);


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
