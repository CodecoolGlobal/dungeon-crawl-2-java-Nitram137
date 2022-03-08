package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.InventoryModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDaoJdbc implements InventoryDao{

    private DataSource dataSource;

    public InventoryDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(InventoryModel inventory) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO inventory (iron_key, potion, big_potion, thunderfury, mjolnir, the_grim_reaper, stormbreaker, frostmourne, stick_of_truth) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, inventory.getIronKey());
            statement.setInt(2, inventory.getPotion());
            statement.setInt(3, inventory.getBigPotion());
            statement.setBoolean(4, inventory.hasThunderfury());
            statement.setBoolean(5, inventory.hasMjolnir());
            statement.setBoolean(6, inventory.hasTheGrimReaper());
            statement.setBoolean(7, inventory.hasStormbreaker());
            statement.setBoolean(8, inventory.hasFrostmourne());
            statement.setBoolean(9, inventory.hasStickOfTruth());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            inventory.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(InventoryModel inventory) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE inventory SET iron_key = ?, potion = ?, big_potion = ?, thunderfury = ?, mjolnir = ?, the_grim_reaper = ?, stormbreaker = ?, frostmourne = ?, stick_of_truth = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, inventory.getIronKey());
            statement.setInt(2, inventory.getPotion());
            statement.setInt(3, inventory.getBigPotion());
            statement.setBoolean(4, inventory.hasThunderfury());
            statement.setBoolean(5, inventory.hasMjolnir());
            statement.setBoolean(6, inventory.hasTheGrimReaper());
            statement.setBoolean(7, inventory.hasStormbreaker());
            statement.setBoolean(8, inventory.hasFrostmourne());
            statement.setBoolean(9, inventory.hasStickOfTruth());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InventoryModel get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT iron_key, potion, big_potion, thunderfury, mjolnir, the_grim_reaper, stormbreaker, frostmourne, stick_of_truth FROM inventory WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()) return null;
            InventoryModel inventory = new InventoryModel(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getBoolean(4), resultSet.getBoolean(5), resultSet.getBoolean(6), resultSet.getBoolean(7), resultSet.getBoolean(8), resultSet.getBoolean(9));
            inventory.setId(id);
            return inventory;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<InventoryModel> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, iron_key, potion, big_potion, thunderfury, mjolnir, the_grim_reaper, stormbreaker, frostmourne, stick_of_truth FROM inventory";
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            List<InventoryModel> result = new ArrayList<>();
            while(resultSet.next()) {
                InventoryModel inventory = new InventoryModel(resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getBoolean(5), resultSet.getBoolean(6), resultSet.getBoolean(7), resultSet.getBoolean(8), resultSet.getBoolean(9), resultSet.getBoolean(10));
                inventory.setId(resultSet.getInt(1));
                result.add(inventory);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
