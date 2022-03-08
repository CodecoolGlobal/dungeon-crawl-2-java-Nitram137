package com.codecool.dungeoncrawl.model;

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
}
