package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;

public class Key extends Item{

    KeyType type;

    public Key(Cell cell, KeyType type) {
        super(cell);
        this.type = type;
    }

    public Key(KeyType type) {
        super();
    }

    @Override
    public void useItem(Player player) {
        Cell door = player.getDoorNextToPlayer();
        if (door != null) {
            door.setCellToOpenedDoor();
            player.deleteItemFromInventory(this);
        }
    }

    @Override
    public String getTileName() {
        return type.getName();
    }
}
