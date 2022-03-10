package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.NuclearCloud;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Shadow;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.items.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameMapTest {

    GameMap mapUnderTest;

    @BeforeEach
    void initMap() {
        mapUnderTest = new GameMap(4, 5, CellType.EMPTY);
        mapUnderTest.getCell(0, 1).setType(CellType.FLOOR);
        mapUnderTest.getCell(0, 2).setType(CellType.WALL);
        mapUnderTest.getCell(0, 3).setType(CellType.FOREST);
        mapUnderTest.getCell(0, 4).setType(CellType.CLOSED_DOOR);

        mapUnderTest.getCell(1, 0).setType(CellType.OPENED_DOOR);
        mapUnderTest.getCell(1, 1).setType(CellType.STAIRS);
        mapUnderTest.getCell(1, 2).setType(CellType.FLOOR);
        mapUnderTest.getCell(1, 3).setType(CellType.FLOOR);
        mapUnderTest.getCell(1, 4).setType(CellType.FLOOR);

        mapUnderTest.getCell(2, 0).setType(CellType.FLOOR);
        mapUnderTest.getCell(2, 1).setType(CellType.FLOOR);
        mapUnderTest.getCell(2, 2).setType(CellType.FLOOR);
        mapUnderTest.getCell(2, 3).setType(CellType.FLOOR);
        mapUnderTest.getCell(2, 4).setType(CellType.FLOOR);

        mapUnderTest.getCell(3, 0).setType(CellType.FLOOR);
        mapUnderTest.getCell(3, 1).setType(CellType.FLOOR);
        mapUnderTest.getCell(3, 2).setType(CellType.FLOOR);
        mapUnderTest.getCell(3, 3).setType(CellType.FLOOR);
        mapUnderTest.getCell(3, 4).setType(CellType.FLOOR);

        mapUnderTest.addEnemy(new Skeleton(mapUnderTest.getCell(1, 2)));
        mapUnderTest.addEnemy(new Shadow(mapUnderTest.getCell(1, 3)));
        mapUnderTest.addEnemy((new NuclearCloud(mapUnderTest.getCell(1, 4))));

        mapUnderTest.setPlayer(new Player(mapUnderTest.getCell(2, 0)));
        new Key(mapUnderTest.getCell(2, 1), KeyType.IRON_KEY);
        new Potion(mapUnderTest.getCell(2, 2), PotionType.POTION);
        new Potion(mapUnderTest.getCell(2, 3), PotionType.BIG_POTION);
        new Weapon(mapUnderTest.getCell(2, 4), WeaponType.THUNDERFURY);

        new Weapon(mapUnderTest.getCell(3, 0), WeaponType.MJOLNIR);
        new Weapon(mapUnderTest.getCell(3, 1), WeaponType.SCYTHE);
        new Weapon(mapUnderTest.getCell(3, 2), WeaponType.STORMBREAKER);
        new Weapon(mapUnderTest.getCell(3, 3), WeaponType.FROSTMOURNE);
        new Weapon(mapUnderTest.getCell(3, 4), WeaponType.STICK);
    }

    @Test
    void getStairs() {
        List<Cell> actual = mapUnderTest.getStairs();
        assertEquals(1, actual.size());
    }

    @Test
    void testToString() {
        String expected = "4 5\n d@m\n.Lkc\n#spT\n%SPF\nDNtQ\n\n";
        String actual = mapUnderTest.toString();
        assertEquals(expected, actual);
    }
}