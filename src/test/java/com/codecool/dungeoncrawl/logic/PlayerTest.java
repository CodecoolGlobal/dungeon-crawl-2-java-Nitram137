package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Potion;
import com.codecool.dungeoncrawl.logic.items.PotionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    GameMap gameMap;
    Player player;

    @BeforeEach
    void setUp() {
        gameMap = new GameMap(3, 3, CellType.FLOOR);
        player = new Player(gameMap.getCell(1,1));
    }

    @Test
    void playerAttackEnemyThanPlayerAndEnemyLoseHealth() {
        Skeleton skeleton = new Skeleton(gameMap.getCell(1,2));
        player.move(0,1);
        int exceptedPlayerHealth = 45;
        int exceptedSkeletonHealth = 10;

        assertEquals(exceptedPlayerHealth, player.getHealth());

        assertEquals(exceptedSkeletonHealth, skeleton.getHealth());
    }

    @Test
    void playerHitSkeletonThreeTimesThanSkeletonDies() {
        Skeleton skeleton = new Skeleton(gameMap.getCell(1,2));
        player.move(0,1);
        player.move(0,1);
        player.move(0,1);

        assertEquals(player, skeleton.getCell().getActor());
    }

    @Test
    void isPlayerStandsOnItemReturnsTrueWhenPlayersCellHasAnItem() {
        Potion potion = new Potion(gameMap.getCell(1,1), PotionType.POTION);
        assertTrue(player.isPlayerStandingInItem());
    }

    @Test
    void isPlayerStandOnItemReturnsFalseWhenPlayersCellItemIsNull() {
        Potion potion = new Potion(gameMap.getCell(1, 2), PotionType.POTION);
        assertFalse(player.isPlayerStandingInItem());
    }

    @Test
    void pickUpItemPushOneKeyWithOneLengthArrayListWhenPlayerPickUpItem() {
        Potion potion = new Potion(gameMap.getCell(1,1), PotionType.POTION);
        player.pickUpItem();
        int exceptedKeysNumber = 1;
        String exceptedItemName = potion.getTileName();
        List<Item> exceptedList = List.of(potion);

        assertEquals(exceptedList, player.getInventory().get(exceptedItemName));
        assertEquals(exceptedKeysNumber, player.getInventory().keySet().size());
    }
}
