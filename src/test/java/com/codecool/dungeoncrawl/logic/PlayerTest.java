package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.items.*;
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

    @Test
    void pickUpMultipleItemFromSameTypeIncreaseTheListSize() {
        List<Item> exceptedList = new ArrayList<>();
        Potion potion = new Potion(gameMap.getCell(1,1), PotionType.POTION);
        exceptedList.add(potion);
        player.pickUpItem();
        potion = new Potion(gameMap.getCell(1,1), PotionType.POTION);
        exceptedList.add(potion);
        player.pickUpItem();
        int exceptedKeysNumber = 1;
        String exceptedItemName = potion.getTileName();

        assertEquals(exceptedList, player.getInventory().get(exceptedItemName));
        assertEquals(exceptedKeysNumber, player.getInventory().keySet().size());
    }

    @Test
    void changeWeaponEquipNewWeaponWhenPlayerWeaponIsNotNull() {
        Weapon weapon = new Weapon(gameMap.getCell(1, 1), WeaponType.SCYTHE);
        player.pickUpItem();
        weapon.useItem(player);
        Weapon weapon1 = new Weapon(gameMap.getCell(1, 1), WeaponType.THUNDERFURY);
        player.pickUpItem();
        weapon1.useItem(player);
        int exceptedStrength = 13;

        assertEquals(exceptedStrength, Math.abs(player.getDamage()));
    }

    @Test
    void changeWeaponWhenWeaponIsNotNullTakeBackTheOldWeaponToInventory() {
        Weapon weapon = new Weapon(gameMap.getCell(1, 1), WeaponType.SCYTHE);
        player.pickUpItem();
        weapon.useItem(player);
        Weapon weapon1 = new Weapon(gameMap.getCell(1, 1), WeaponType.THUNDERFURY);
        player.pickUpItem();
        weapon1.useItem(player);

        assertEquals(weapon, player.getInventory().get(weapon.getTileName()).get(0));
    }

    @Test
    void getWeaponNameReturnsWeaponTileNameWhenPlayerHasWeapon() {
        Weapon weapon = new Weapon(gameMap.getCell(1, 1), WeaponType.SCYTHE);
        player.pickUpItem();
        weapon.useItem(player);

        String excepted = weapon.getTileName();
        assertEquals(excepted, player.getWeaponName());
    }

    @Test
    void getWeaponNameReturnsNoWeaponWhenPlayerHasNoWeapon() {
        String excepted = "No weapon";
        assertEquals(excepted, player.getWeaponName());
    }

    @Test
    void drinkPotionDontChangePlayerHealthWhenItsFull() {
        Potion potion = new Potion(gameMap.getCell(1,1), PotionType.POTION);
        player.pickUpItem();
        player.drinkPotion(potion);

        int excepted = Player.MAX_HEALTH;

        assertEquals(excepted, player.getHealth());
    }

    @Test
    void drinkPotionDoesNotSetUpHigherHealthThanMAX_HEALTH() {
        Potion potion = new Potion(gameMap.getCell(1,1), PotionType.POTION);
        player.setHealth(45);
        player.pickUpItem();
        player.drinkPotion(potion);

        int excepted = Player.MAX_HEALTH;

        assertEquals(excepted, player.getHealth());
    }

    @Test
    void drinkPotionIncreaseHealthWithPotionHealthRegen() {
        int health = 30;
        Potion potion = new Potion(gameMap.getCell(1,1), PotionType.POTION);
        player.setHealth(health);
        player.pickUpItem();
        player.drinkPotion(potion);

        int excepted = health + potion.getHealthRegen();

        assertEquals(excepted, player.getHealth());
    }

    @Test
    void getDoorNextToPlayerReturnsNullWhenThereIsNoDoorNextToPlayer() {
        Cell cell = player.getDoorNextToPlayer();
        assertNull(cell);
    }
}
