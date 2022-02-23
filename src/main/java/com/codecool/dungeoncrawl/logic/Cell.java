package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.items.Item;

public class Cell implements Drawable {
    private CellType type;
    private Item item;
    private Actor actor;
    private GameMap gameMap;
    private int x, y;

    Cell(GameMap gameMap, int x, int y, CellType type) {
        this.gameMap = gameMap;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public boolean isPlayerNear(int tiles) {
        return Math.abs(x - gameMap.getPlayer().getX()) <= tiles && Math.abs(y - gameMap.getPlayer().getY()) <= tiles;
    }

    public void hurtPlayer(int damage) {
        gameMap.getPlayer().modifyHealth(-damage);
    }

    public int getPlayerX() { return gameMap.getPlayer().getX(); }

    public int getPlayerY() { return gameMap.getPlayer().getY(); }

    public int getPlayerStrength() { return gameMap.getPlayer().getDamage(); }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Actor getActor() {
        return actor;
    }

    public Cell getNeighbor(int dx, int dy) {
        try {
            return gameMap.getCell(x + dx, y + dy);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public String getTileName() {
        return type.getTileName();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
