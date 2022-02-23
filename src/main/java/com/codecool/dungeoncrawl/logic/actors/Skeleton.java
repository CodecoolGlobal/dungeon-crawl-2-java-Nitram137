package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Skeleton extends Actor {

    public Skeleton(Cell cell) {
        super(cell);
        strength = 2;
    }

    public int getDamage() { return -strength; }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
