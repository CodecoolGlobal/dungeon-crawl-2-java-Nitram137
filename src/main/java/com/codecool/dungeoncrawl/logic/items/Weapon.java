package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;

public class Weapon extends Item {

    WeaponType type;

    public Weapon(Cell cell, WeaponType type) {
        super(cell);
        this.type = type;
    }

    public Weapon(WeaponType type) {
        super();
        this.type = type;
    }

    public int getDamage() {
        return type.getDamage();
    }

    @Override
    public void useItem(Player player) throws IllegalArgumentException {
        if (player == null) {throw new IllegalArgumentException();}
        player.changeWeapon(this);
    }


    @Override
    public String getTileName() {
        return type.getName();
    }
}
