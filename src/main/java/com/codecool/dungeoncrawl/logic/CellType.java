package com.codecool.dungeoncrawl.logic;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    WALL("wall"),
    FOREST("forest"),
    CLOSED_DOOR("closed_door"),
    OPENED_DOOR("opened_door"),
    STAIRS("stairs");

    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}
