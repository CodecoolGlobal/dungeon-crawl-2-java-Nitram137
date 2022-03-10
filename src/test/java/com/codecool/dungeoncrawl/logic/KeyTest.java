package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.KeyType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KeyTest {

    GameMap gameMap = new GameMap(3, 3, CellType.FLOOR);

    @Test
    void useItemThrowIllegalArgumentExceptionWhenPlayerIsNull() {
        Key key = new Key(KeyType.IRON_KEY);
        assertThrows(IllegalArgumentException.class, () -> key.useItem(null));
    }

    @Test
    void useKeyOpensADoorNextToPlayer() {
        Player player = new Player(gameMap.getCell(1,1));
        Key key = new Key(gameMap.getCell(1, 1), KeyType.IRON_KEY);
        player.pickUpItem();
        gameMap.getCell(2,1).setType(CellType.CLOSED_DOOR);
        key.useItem(player);
        assertTrue(gameMap.getCell(2, 1).isCellOpenedDoor());
    }
}
