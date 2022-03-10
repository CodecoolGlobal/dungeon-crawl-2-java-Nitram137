package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;

public class Potion extends Item{

    PotionType type;

    public Potion(Cell cell, PotionType type) {
        super(cell);
        this.type = type;
    }

    public Potion(PotionType type) {
        super();
        this.type = type;
    }

    @Override
    public void useItem(Player player) throws IllegalArgumentException {
        if (player == null) {throw new IllegalArgumentException();}
        player.drinkPotion(this);
    }

    @Override
    public String getTileName() {
        return type.getName();
    }

    public int getHealthRegen() {
        return type.getHealthRegen();
    }
}
