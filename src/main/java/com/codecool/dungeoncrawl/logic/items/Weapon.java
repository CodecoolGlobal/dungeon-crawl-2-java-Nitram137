package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;

public class Weapon extends Item {

    WeaponType type;

    public Weapon(Cell cell, WeaponType type) {
        super(cell);
        this.type = type;
    }

    @Override
    public void useItem(Player player) {

    }


    @Override
    public String getTileName() {
        return type.getName();
    }
}
