package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.items.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryModel extends BaseModel {
    private int ironKey;
    private int potion;
    private int bigPotion;
    private boolean thunderfury;
    private boolean mjolnir;
    private boolean theGrimReaper;
    private boolean stormbreaker;
    private boolean frostmourne;
    private boolean stickOfTruth;

    public InventoryModel(Map<String, List<Item>> inventory) {
        for(String key : inventory.keySet()) {
            switch (key) {
                case "Iron Key": ironKey = inventory.get(key).size();
                    break;
                case "Potion": potion = inventory.get(key).size();
                    break;
                case "Big Potion": bigPotion = inventory.get(key).size();
                    break;
                case "Thunderfury": thunderfury = inventory.get(key).size() == 1;
                    break;
                case "Mjolnir": mjolnir = inventory.get(key).size() == 1;
                    break;
                case "The Grim Reaper": theGrimReaper = inventory.get(key).size() == 1;
                    break;
                case "Stormbreaker": stormbreaker = inventory.get(key).size() == 1;
                    break;
                case "Frostmourne": frostmourne = inventory.get(key).size() == 1;
                    break;
                case "Stick of Truth": stickOfTruth = inventory.get(key).size() == 1;
                    break;
            }
        }
    }

    public InventoryModel(int ironKey, int potion, int bigPotion, boolean thunderfury, boolean mjolnir, boolean theGrimReaper, boolean stormbreaker, boolean frostmourne, boolean stickOfTruth) {
        this.ironKey = ironKey;
        this.potion = potion;
        this.bigPotion = bigPotion;
        this.thunderfury = thunderfury;
        this.mjolnir = mjolnir;
        this.theGrimReaper = theGrimReaper;
        this.stormbreaker = stormbreaker;
        this.frostmourne = frostmourne;
        this.stickOfTruth = stickOfTruth;
    }

    public int getIronKey() {
        return ironKey;
    }

    public void setIronKey(int ironKey) {
        this.ironKey = ironKey;
    }

    public int getPotion() {
        return potion;
    }

    public void setPotion(int potion) {
        this.potion = potion;
    }

    public int getBigPotion() {
        return bigPotion;
    }

    public void setBigPotion(int bigPotion) {
        this.bigPotion = bigPotion;
    }

    public boolean hasThunderfury() {
        return thunderfury;
    }

    public void setThunderfury(boolean thunderfury) {
        this.thunderfury = thunderfury;
    }

    public boolean hasMjolnir() {
        return mjolnir;
    }

    public void setMjolnir(boolean mjolnir) {
        this.mjolnir = mjolnir;
    }

    public boolean hasTheGrimReaper() {
        return theGrimReaper;
    }

    public void setTheGrimReaper(boolean theGrimReaper) {
        this.theGrimReaper = theGrimReaper;
    }

    public boolean hasStormbreaker() {
        return stormbreaker;
    }

    public void setStormbreaker(boolean stormbreaker) {
        this.stormbreaker = stormbreaker;
    }

    public boolean hasFrostmourne() {
        return frostmourne;
    }

    public void setFrostmourne(boolean frostmourne) {
        this.frostmourne = frostmourne;
    }

    public boolean hasStickOfTruth() {
        return stickOfTruth;
    }

    public void setStickOfTruth(boolean stickOfTruth) {
        this.stickOfTruth = stickOfTruth;
    }

    public Map<String, List<Item>> convertToInventory() {
        Map<String, List<Item>> inventory = new HashMap<>();
        if (ironKey != 0) {
            inventory.put("Iron Key", createKeys(ironKey));
        }
        if (potion != 0) {
            inventory.put("Potion", createPotions(potion));
        }
        if (bigPotion != 0) {
            inventory.put("Big Potion", createBigPotions(bigPotion));
        }
        if (thunderfury) {
            inventory.put("Thunderfury", createThunderfury());
        }
        return inventory;
    }

    private List<Item> createThunderfury() {
        List<Item> weapon = new ArrayList<>();
        Weapon thunderfury = new Weapon(WeaponType.THUNDERFURY);
        weapon.add(thunderfury);
        return weapon;
    }

    private List<Item> createKeys(int numberOfKeys) {
        List<Item> keys = new ArrayList<>();
        for(int i = 0; i < numberOfKeys; i++) {
            Key key = new Key(KeyType.IRON_KEY);
            keys.add(key);
        }
        return keys;
    }

    private List<Item> createPotions(int numberOfPotions) {
        List<Item> potions = new ArrayList<>();
        for (int i = 0; i < numberOfPotions; i++) {
            Potion potion = new Potion(PotionType.POTION);
            potions.add(potion);
        }
        return potions;
    }

    private List<Item> createBigPotions(int numberOfBigPotions) {
        List<Item> bigPotions = new ArrayList<>();
        for (int i = 0; i < numberOfBigPotions; i++) {
            Potion bigPotion = new Potion(PotionType.BIG_POTION);
            bigPotions.add(bigPotion);
        }
        return bigPotions;
    }
}
