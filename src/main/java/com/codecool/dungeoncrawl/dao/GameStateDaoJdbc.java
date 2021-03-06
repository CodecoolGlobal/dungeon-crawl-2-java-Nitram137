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
            String sql = "INSERT INTO game_state (current_map, player_id, inventory_id, hscroll, vscroll, map_name) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, state.getCurrentMap());
            statement.setInt(2, state.getPlayer().getId());
            statement.setInt(3, state.getInventory().getId());
            statement.setDouble(4, state.getHscroll());
            statement.setDouble(5, state.getVscroll());
            statement.setString(6, state.getMapName());
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
            String sql = "UPDATE game_state SET current_map = ?, player_id = ?, inventory_id = ?, hscroll = ?, vscroll = ?, map_name = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, state.getCurrentMap());
            statement.setInt(2, state.getPlayer().getId());
            statement.setInt(3, state.getInventory().getId());
            statement.setDouble(4, state.getHscroll());
            statement.setDouble(5, state.getVscroll());
            statement.setString(6, state.getMapName());
            statement.setInt(7, state.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameState getGameStateByPlayerName(String playerName) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT game_state.id, current_map, saved_at, player_id, inventory_id, hscroll, vscroll, " +
                    "map_name FROM game_state JOIN player p on p.id = game_state.player_id WHERE player_name = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, playerName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) return null;
            int playerId = resultSet.getInt(4);
            int inventoryId = resultSet.getInt(5);
            double Hscroll = resultSet.getDouble(6);
            double Vscroll = resultSet.getDouble(7);
            String mapName = resultSet.getString(8);
            PlayerModel player = playerDao.get(playerId);
            InventoryModel inventory = inventoryDao.get(inventoryId);
            GameState state = new GameState(resultSet.getString(2), resultSet.getDate(3), player, inventory, Hscroll, Vscroll, mapName);
            state.setId(resultSet.getInt(1));
            return state;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameState get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT current_map, saved_at, player_id, inventory_id, hscroll, vscroll, map_name FROM game_state WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()) return null;
            int playerId = resultSet.getInt(3);
            int inventoryId = resultSet.getInt(4);
            double Hscroll = resultSet.getDouble(5);
            double Vscroll = resultSet.getDouble(6);
            String mapName = resultSet.getString(7);
            PlayerModel player = playerDao.get(playerId);
            InventoryModel inventory = inventoryDao.get(inventoryId);
            GameState state = new GameState(resultSet.getString(1), resultSet.getDate(2), player, inventory, Hscroll, Vscroll, mapName);
            state.setId(id);
            return state;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameState> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, current_map, saved_at, player_id, inventory_id, hscroll, vscroll, map_name FROM game_state";
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            List<GameState> result = new ArrayList<>();
            while(resultSet.next()) {
                int playerId = resultSet.getInt(4);
                int inventoryId = resultSet.getInt(5);
                double Hscroll = resultSet.getDouble(6);
                double Vscroll = resultSet.getDouble(7);
                String mapName = resultSet.getString(8);
                PlayerModel player = playerDao.get(playerId);
                InventoryModel inventory = inventoryDao.get(inventoryId);
                GameState state = new GameState(resultSet.getString(2), resultSet.getDate(3), player, inventory, Hscroll, Vscroll, mapName);
                state.setId(resultSet.getInt(1));
                result.add(state);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
