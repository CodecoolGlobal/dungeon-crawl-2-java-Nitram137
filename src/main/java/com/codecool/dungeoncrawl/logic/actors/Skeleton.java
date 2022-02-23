package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

import java.util.Random;

public class Skeleton extends Actor {

    public Skeleton(Cell cell) {
        super(cell);
        strength = 2;
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT;

        public static Direction getRandomDirection() {
            return values()[new Random().nextInt(values().length)];
        }
    }

    private void moveRandom() {
        switch (Direction.getRandomDirection()) {
            case UP: move(0, -1);
                break;
            case DOWN: move(0, 1);
                break;
            case LEFT: move(-1, 0);
                break;
            case RIGHT: move(1, 0);
                break;
        }
    }

    @Override
    public void act() {
        if(getHealth() > 0 && getCell().isPlayerNear(2)) moveRandom();
    }

    public int getDamage() { return -strength; }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
