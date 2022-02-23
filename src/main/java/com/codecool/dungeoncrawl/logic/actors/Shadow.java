package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Shadow extends Actor{

    public Shadow(Cell cell) {
        super(cell);
        strength = cell.getPlayerStrength();
        health = 10;
    }

    @Override
    public String getTileName() {
        return "shadow";
    }

    public void follow() {

    }

    @Override
    public void act() {

    }
}
