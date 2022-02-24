package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Player extends Actor {

    public static final int MAX_HEALTH = 50;
    private Weapon weapon;

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

    private void deleteItemFromInventory(Item item) {
        List<Item> itemList = inventory.get(item.getTileName());
        itemList.remove(item);
        if (itemList.size() == 0) {
            inventory.remove(item.getTileName());
        }
    }

    public String getWeaponName() {
        if (weapon != null) {
            return weapon.getTileName();
        }
        return "No weapon";
    }

    public void changeWeapon(Weapon weapon) {
        if (this.weapon != null) {
            strength = strength - this.weapon.getDamage();
            takeBackWeaponToInventory(this.weapon);
        }
        strength = strength + weapon.getDamage();
        this.weapon = weapon;
        deleteItemFromInventory(weapon);
    }

    private void takeBackWeaponToInventory(Item item) {
        String key = item.getTileName();
            List<Item> items = new ArrayList<>();
            items.add(item);
            inventory.put(key, items);
        }
}
