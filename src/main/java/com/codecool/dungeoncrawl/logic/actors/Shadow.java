package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Shadow extends Actor{

    public Shadow(Cell cell) {
        super(cell);
        strength = cell.getPlayerStrength();
        health = 25;
    }

    @Override
    public String getTileName() {
        return "shadow";
    }

    public void follow() {
        int playerX = getCell().getPlayerX();
        int playerY = getCell().getPlayerY();
        if(getY() > playerY) move(0, -1);
        if(getY() < playerY) move(0, 1);
        if(getX() > playerX) move(-1, 0);
        if(getX() < playerX) move(1, 0);
    }

    @Override
    public void act() {
        if(getHealth() > 0 && getCell().isPlayerNear(5)) follow();
    }
}
