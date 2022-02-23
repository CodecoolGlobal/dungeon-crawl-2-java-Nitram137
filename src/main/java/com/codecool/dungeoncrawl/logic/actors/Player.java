package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

import java.util.ArrayList;
import java.util.List;

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

    public void pickUpItem() {
        Item item = getCell().getItem();
        String itemName = item.getTileName();
        if (inventory.containsKey(itemName)) {
            List<Item> items = inventory.get(itemName);
            items.add(item);
        } else {
            List<Item> items = new ArrayList<>();
            items.add(item);
            inventory.put(itemName, items);
        }
    }

    public boolean isPlayerStandingInItem() {
        return this.getCell().getItem() != null;
    }

}
