package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Weapon;
import com.codecool.dungeoncrawl.logic.items.WeaponType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeaponTest {

    GameMap gameMap = new GameMap(3, 3, CellType.FLOOR);

    @Test
    void useItemThrowIllegalArgumentExceptionWhenPlayerIsNull() {
        Weapon weapon = new Weapon(WeaponType.STICK);
        assertThrows(IllegalArgumentException.class, () -> weapon.useItem(null));
    }

    @Test
    void scytheTypeWeaponAddSixteenToPlayerDamage() {
        int excepted = 21;
        Player player = new Player(gameMap.getCell(1,1));
        Weapon weapon = new Weapon(gameMap.getCell(1, 1), WeaponType.SCYTHE);
        player.pickUpItem();
        weapon.useItem(player);
        assertEquals(excepted, Math.abs(player.getDamage()));
    }
}
