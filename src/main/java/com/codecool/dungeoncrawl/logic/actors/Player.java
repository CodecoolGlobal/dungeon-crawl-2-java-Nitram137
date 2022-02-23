package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import java.util.HashMap;

public class Player extends Actor {

    public Player(Cell cell) {
        super(cell);
        strength = 5;
    }

    void attack(Cell nextCell) {
        Actor enemy = nextCell.getActor();
        if(enemy != null) {
            enemy.modifyHealth(-strength);
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

    @Override
    public void act() {

    }

    public void pickUpItem() {
        HashMap<String, Integer> inventory = this.getInventory();
        String itemName = this.getCell().getItem().getClass().getSimpleName();
        if (inventory.containsKey(itemName)) {
            Integer counter = inventory.get(itemName);
            inventory.put(itemName, counter + 1);
        } else {
            inventory.put(itemName, 1);
        }
    }

    public boolean isPlayerStandingInItem() {
        return this.getCell().getItem() != null;
    }

}
