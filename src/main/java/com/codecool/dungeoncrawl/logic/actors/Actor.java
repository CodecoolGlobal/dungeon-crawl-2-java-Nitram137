package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

import java.util.HashMap;

public abstract class Actor implements Drawable {

    private final HashMap<String, Integer> inventory = new HashMap<>();

    private Cell cell;
    private int health = 10;
    protected int strength;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        if(nextCell == null) return;
        if(this instanceof Player) ((Player) this).attack(nextCell);
        if(nextCell.getType() == CellType.FLOOR && nextCell.getActor() == null) {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        }
    }

    public HashMap<String, Integer> getInventory() {
        return inventory;
    }

    public int getHealth() {
        return health;
    }

    public void modifyHealth(int change) { health += change; }

    public int getDamage() { return strength; }

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
