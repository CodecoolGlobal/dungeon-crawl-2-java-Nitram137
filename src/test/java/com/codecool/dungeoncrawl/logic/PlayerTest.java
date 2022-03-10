package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
