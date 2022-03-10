package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameMap {
    private int width;
    private int height;
    private Cell[][] cells;

    private Player player;
    private List<Actor> enemies = new ArrayList<>();

    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public void addEnemy(Actor enemy) {
        enemies.add(enemy);
    }

    public List<Actor> getEnemies() {
        return enemies;
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() { return player; }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Cell> getStairs() {
        List<Cell> stairs = new ArrayList<>();
        for(Cell[] cellRow : cells) {
            for(Cell cell : cellRow) {
                if(cell.isCellStairs()) {
                    stairs.add(cell);
                }
            }
        }
        return stairs;
    }

    @Override
    public String toString() {
        StringBuilder mapAsString = new StringBuilder(String.format("%d %d\n", width, height));
        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                Cell cell = cells[j][i];
                if(cell.getActor() != null)
                switch (cell.getActor().getTileName()) {
                    case "player": mapAsString.append("@");
                        break;
                    case "skeleton": mapAsString.append("s");
                        break;
                    case "shadow": mapAsString.append("S");
                        break;
                    case "nuclear cloud": mapAsString.append("N");
                        break;
                }
                else if(cell.getItem() != null)
                switch (cell.getItem().getTileName()) {
                    case "Thunderfury": mapAsString.append("t");
                        break;
                    case "Mjolnir": mapAsString.append("m");
                        break;
                    case "The Grim Reaper": mapAsString.append("c");
                        break;
                    case "Stormbreaker": mapAsString.append("T");
                        break;
                    case "Frostmourne": mapAsString.append("F");
                        break;
                    case "Stick of Truth": mapAsString.append("Q");
                        break;
                    case "Iron Key": mapAsString.append("k");
                        break;
                    case "Potion": mapAsString.append("p");
                        break;
                    case "Big Potion": mapAsString.append("P");
                        break;
                }
                else
                switch (cell.getTileName()) {
                    case "empty": mapAsString.append(" ");
                        break;
                    case "wall": mapAsString.append("#");
                        break;
                    case "forest": mapAsString.append("%");
                        break;
                    case "floor": mapAsString.append(".");
                        break;
                    case "closed_door": mapAsString.append("D");
                        break;
                    case "opened_door": mapAsString.append("d");
                        break;
                    case "stairs": mapAsString.append("L");
                        break;
                    default: throw new RuntimeException("Missing tile type");
                }
            }
            mapAsString.append("\n");
        }
        mapAsString.append("\n");
        return mapAsString.toString();
    }

    public double getHorizontalScroll() {
        return 1.44 / width;
    }
    public double getVerticalScroll() {
        return 1.98 / height;
    }
}
