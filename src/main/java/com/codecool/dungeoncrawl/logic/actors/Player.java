package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import java.util.HashMap;

public class Player extends Actor {
    public Player(Cell cell) {
        super(cell);
    }

    public String getTileName() {
        return "player";
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

}
