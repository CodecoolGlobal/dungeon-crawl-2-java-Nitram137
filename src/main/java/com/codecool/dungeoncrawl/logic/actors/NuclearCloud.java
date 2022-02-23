package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class NuclearCloud extends Actor{

    public NuclearCloud(Cell cell) {
        super(cell);
        strength = 1;
    }

    @Override
    public String getTileName() {
        return "nuclear cloud";
    }

    @Override
    public void act() {

    }
}
