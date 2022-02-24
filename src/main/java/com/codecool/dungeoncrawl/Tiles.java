package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    public static int TILE_WIDTH = 32;

    private static Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);
    private static Map<String, Tile> tileMap = new HashMap<>();
    public static class Tile {
        public final int x, y, w, h;
        Tile(int i, int j) {
            x = i * (TILE_WIDTH + 2);
            y = j * (TILE_WIDTH + 2);
            w = TILE_WIDTH;
            h = TILE_WIDTH;
        }
    }

    static {
        tileMap.put("empty", new Tile(0, 0));
        tileMap.put("wall", new Tile(10, 17));
        tileMap.put("floor", new Tile(2, 0));
        tileMap.put("player", new Tile(27, 0));
        tileMap.put("skeleton", new Tile(29, 6));
        tileMap.put("Iron Key", new Tile(17, 23));
        tileMap.put("Golden key", new Tile(16,23));
        tileMap.put("Potion", new Tile(23, 23));
        tileMap.put("Big Potion", new Tile(26, 23));
        tileMap.put("Thunderfury", new Tile(4, 29));
        tileMap.put("Frostmourne", new Tile(2, 30));
        tileMap.put("The Grim Reaper", new Tile(3, 24));
        tileMap.put("Mjolnir", new Tile(5, 29));
        tileMap.put("Stormbreaker", new Tile(8, 29));
        tileMap.put("Stick of Truth", new Tile(0, 25));
        tileMap.put("shadow", new Tile(24, 8));
        tileMap.put("nuclear cloud", new Tile(4, 2));
        tileMap.put("closed_door", new Tile(3, 9));
        tileMap.put("opened_door", new Tile(6, 9));
    }

    public static void drawTile(GraphicsContext context, Drawable d, int x, int y) {
        Tile tile = tileMap.get(d.getTileName());
        context.drawImage(tileset, tile.x, tile.y, tile.w, tile.h,
                x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
    }
}
