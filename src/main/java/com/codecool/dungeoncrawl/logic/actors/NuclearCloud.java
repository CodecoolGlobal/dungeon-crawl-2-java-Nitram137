package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class NuclearCloud extends Actor {

    public NuclearCloud(Cell cell) {
        super(cell);
        strength = 1;
        health = 100;
    }

    @Override
    public String getTileName() {
        return "nuclear cloud";
    }

    private void poison() {
        if(getCell().isPlayerNear(2)) {
            getCell().hurtPlayer(strength);
        }
    }

    @Override
    public void act() {
        if(getHealth() > 0) poison();
    }
}
