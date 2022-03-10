package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Potion;
import com.codecool.dungeoncrawl.logic.items.PotionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PotionTest {

    GameMap gameMap = new GameMap(3, 3, CellType.FLOOR);

    @Test
    void useItemThrowIllegalArgumentExceptionWhenPlayerIsNull() {
        Potion potion = new Potion(PotionType.BIG_POTION);
        assertThrows(IllegalArgumentException.class, () -> potion.useItem(null));
    }

    @Test
    void bigPotionRegenerateTwentyFiveHealth() {
        Player player = new Player(gameMap.getCell(1,1));
        player.setHealth(10);
        Potion potion = new Potion(gameMap.getCell(1, 1),PotionType.BIG_POTION);
        player.pickUpItem();
        potion.useItem(player);
        int excepted = 35;
        assertEquals(excepted, player.getHealth());
    }
}
