package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.NuclearCloud;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Shadow;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.items.*;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap() {
        InputStream is = MapLoader.class.getResourceAsStream("/custom_map.txt");
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '%':
                            cell.setType(CellType.FOREST);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            map.addEnemy(new Skeleton(cell));
                            break;
                        case 'S':
                            cell.setType(CellType.FLOOR);
                            map.addEnemy(new Shadow(cell));
                            break;
                        case 'N':
                            cell.setType(CellType.FLOOR);
                            map.addEnemy((new NuclearCloud(cell)));
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        case 'k':
                            cell.setType(CellType.FLOOR);
                            new Key(cell, KeyType.IRON_KEY);
                            break;
                        case 'K':
                            cell.setType(CellType.FLOOR);
                            new Key(cell, KeyType.GOLDEN_KEY);
                            break;
                        case 't':
                            cell.setType(CellType.FLOOR);
                            new Weapon(cell, WeaponType.THUNDERFURY);
                            break;
                        case 'F':
                            cell.setType(CellType.FLOOR);
                            new Weapon(cell, WeaponType.FROSTMOURNE);
                            break;
                        case 'c':
                            cell.setType(CellType.FLOOR);
                            new Weapon(cell, WeaponType.SCYTHE);
                            break;
                        case 'm':
                            cell.setType(CellType.FLOOR);
                            new Weapon(cell, WeaponType.MJOLNIR);
                            break;
                        case 'T':
                            cell.setType(CellType.FLOOR);
                            new Weapon(cell, WeaponType.STORMBREAKER);
                            break;
                        case 'Q':
                            cell.setType(CellType.FLOOR);
                            new Weapon(cell, WeaponType.STICK);
                            break;
                        case 'p':
                            cell.setType(CellType.FLOOR);
                            new Potion(cell, PotionType.POTION);
                            break;
                        case 'P':
                            cell.setType(CellType.FLOOR);
                            new Potion(cell, PotionType.BIG_POTION);
                            break;
                        case 'D':
                            cell.setType(CellType.CLOSED_DOOR);
                            break;
                        case 'd':
                            cell.setType(CellType.OPENED_DOOR);
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

}
