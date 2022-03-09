package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.InventoryModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class GameDatabaseManager {
    private PlayerDao playerDao;
    private InventoryDao inventoryDao;
    private GameStateDao gameStateDao;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        inventoryDao = new InventoryDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource, playerDao, inventoryDao);
    }

    public void saveGame(Player player, GameMap map, double Hscroll, double Vscroll, String mapName) {
        PlayerModel playerModel = new PlayerModel(player);
        InventoryModel inventoryModel = new InventoryModel(player.getInventory());
        GameState gameState = new GameState(map.toString(), playerModel, inventoryModel, Hscroll, Vscroll, mapName);
        playerDao.add(playerModel);
        inventoryDao.add(inventoryModel);
        gameStateDao.add(gameState);
    }

    public void updateSavedGame(Player player) {

    }

    public boolean isPlayerExists(String playerName) {
        List<PlayerModel> players = playerDao.getAll();
        for (PlayerModel player : players) {
            if (player.getPlayerName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    public List<GameState> getAllGameState() {
        return gameStateDao.getAll();
    }


    private DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = System.getenv("PSQL_DB_NAME");
        String user = System.getenv("PSQL_USER_NAME");
        String password = System.getenv("PSQL_PASSWORD");

        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");

        return dataSource;
    }
}
