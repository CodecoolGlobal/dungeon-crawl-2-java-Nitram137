package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Player extends Actor {
    private int damage = -5;

    public Player(Cell cell) {
        super(cell);
    }

    void attack(Cell nextCell) {
        Actor enemy = nextCell.getActor();
        if(enemy != null) {
            enemy.modifyHealth(damage);
            if(enemy.getHealth() > 0) {
                this.modifyHealth(enemy.getDamage());
            } else {
                enemy.die();
            }
        }
    }

    public String getTileName() {
        return "player";
    }
}
