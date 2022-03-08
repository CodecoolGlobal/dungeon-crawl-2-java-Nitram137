package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.InventoryModel;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {

    private DataSource dataSource;
    private PlayerDao playerDao;
    private InventoryDao inventoryDao;

    public GameStateDaoJdbc(DataSource dataSource, PlayerDao playerDao, InventoryDao inventoryDao) {
        this.dataSource = dataSource;
        this.playerDao = playerDao;
        this.inventoryDao = inventoryDao;
    }

    @Override
    public void add(GameState state) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO game_state (current_map, player_id, inventory_id) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, state.getCurrentMap());
            statement.setInt(2, state.getPlayer().getId());
            statement.setInt(3, state.getInventory().getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            state.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(GameState state) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE game_state SET current_map = ?, player_id = ?, inventory_id = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, state.getCurrentMap());
            statement.setInt(2, state.getPlayer().getId());
            statement.setInt(3, state.getInventory().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameState get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT current_map, saved_at, player_id, inventory_id FROM game_state WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()) return null;
            int playerId = resultSet.getInt(3);
            int inventoryId = resultSet.getInt(4);
            PlayerModel player = playerDao.get(playerId);
            InventoryModel inventory = inventoryDao.get(inventoryId);
            GameState state = new GameState(resultSet.getString(1), resultSet.getDate(2), player, inventory);
            state.setId(id);
            return state;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameState> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, current_map, saved_at, player_id, inventory_id FROM game_state";
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            List<GameState> result = new ArrayList<>();
            while(resultSet.next()) {
                int playerId = resultSet.getInt(4);
                int inventoryId = resultSet.getInt(5);
                PlayerModel player = playerDao.get(playerId);
                InventoryModel inventory = inventoryDao.get(inventoryId);
                GameState state = new GameState(resultSet.getString(2), resultSet.getDate(3), player, inventory);
                state.setId(resultSet.getInt(1));
                result.add(state);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
