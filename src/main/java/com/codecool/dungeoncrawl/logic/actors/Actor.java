package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;
import com.codecool.dungeoncrawl.logic.items.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Actor implements Drawable {

    protected Map<String, List<Item>> inventory = new HashMap<>();

    protected Cell cell;
    protected int health = 10;
    protected int strength;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public void move(int dx, int dy) {
        if (health <= 0) { return; }
        Cell nextCell = cell.getNeighbor(dx, dy);
        if(nextCell == null) return;
        if(this instanceof Player) ((Player) this).attack(nextCell);
        if (nextCell.isCellFreeToMove()) {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        }
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public abstract void act();

    public Map<String, List<Item>> getInventory() {
        return inventory;
    }

    public int getHealth() {
        return health;
    }

    public void modifyHealth(int change) { health += change; }

    public int getDamage() { return -strength; }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public void die() { cell.setActor(null); }
}
