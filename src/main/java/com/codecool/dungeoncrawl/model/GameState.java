package com.codecool.dungeoncrawl.model;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class GameState extends BaseModel {
    private Date savedAt;
    private String currentMap;
    private List<String> discoveredMaps = new ArrayList<>();
    private PlayerModel player;
    private InventoryModel inventory;
    private double Hscroll;
    private double Vscroll;
    private String mapName;

    public GameState(String currentMap, Date savedAt, PlayerModel player, InventoryModel inventory, double Hscroll, double Vscroll, String mapName) {
        this.currentMap = currentMap;
        this.savedAt = savedAt;
        this.player = player;
        this.inventory = inventory;
        this.Hscroll = Hscroll;
        this.Vscroll = Vscroll;
        this.mapName = mapName;
    }

    public String getMapName() {
        return mapName;
    }

    public GameState(String currentMap, PlayerModel player, InventoryModel inventory, double Hscroll, double Vscroll, String mapName) {
        this.currentMap = currentMap;
        this.player = player;
        this.inventory = inventory;
        this.Hscroll = Hscroll;
        this.Vscroll = Vscroll;
        this.mapName = mapName;
    }

    public Date getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Date savedAt) {
        this.savedAt = savedAt;
    }

    public String getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(String currentMap) {
        this.currentMap = currentMap;
    }

    public List<String> getDiscoveredMaps() {
        return discoveredMaps;
    }

    public void addDiscoveredMap(String map) {
        this.discoveredMaps.add(map);
    }

    public PlayerModel getPlayer() {
        return player;
    }

    public void setPlayer(PlayerModel player) {
        this.player = player;
    }

    public InventoryModel getInventory() { return inventory; }

    public void setInventory(InventoryModel inventory) { this.inventory = inventory; }

    public double getHscroll() {
        return Hscroll;
    }

    public double getVscroll() {
        return Vscroll;
    }
}
