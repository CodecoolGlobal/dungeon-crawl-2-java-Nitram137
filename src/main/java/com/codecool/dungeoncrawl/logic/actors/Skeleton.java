package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Skeleton extends Actor {
    private int damage;

    public Skeleton(Cell cell) {
        super(cell);
        damage = -2;
    }

    public int getDamage() { return damage; }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
