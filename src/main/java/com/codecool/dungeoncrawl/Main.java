package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Potion;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

public class Main extends Application {
    ScrollPane scrollPane = new ScrollPane();
    GridPane ui = new GridPane();
    Button pickUpItem = new Button("Pick up");
    String mapName = "/map1.txt";
    GameMap map = MapLoader.loadMap(mapName);
    Player player = map.getPlayer();
    BorderPane borderPane = new BorderPane();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
           map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label weaponLabel = new Label();
    Label damageLabel = new Label();
    double Hscroll = 0.035;
    double Vscroll = 0.060;
    GameDatabaseManager dbManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupDbManager();
        ui.setPrefWidth(300);
        ui.setPadding(new Insets(10));

        displayUI();

        addEventListenerToPickUpButton();
        disablePickUpButton();

        scrollPane.pannableProperty().set(true);
        scrollPane.setContent(canvas);

        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        borderPane.setCenter(scrollPane);
        borderPane.setRight(ui);

        borderPane.setPrefHeight(640);
        borderPane.setPrefWidth(1100);


        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
        displayInventory();
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        KeyCombination exitCombinationMac = new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
        KeyCombination exitCombinationWin = new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);
        if (exitCombinationMac.match(keyEvent)
                || exitCombinationWin.match(keyEvent)
                || keyEvent.getCode() == KeyCode.ESCAPE) {
            exit();
        }
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        Player player = map.getPlayer();
        Cell lastCell =  player.getCell();
        if (player.isPlayerHasStick()) {
            return;
        }
        switch (keyEvent.getCode()) {
            case UP:
                player.move(0, -1);
                if(lastCell != player.getCell())
                    scrollPane.setVvalue(scrollPane.getVvalue() - Vscroll);
                break;
            case DOWN:
                player.move(0, 1);
                if(lastCell != player.getCell())
                    scrollPane.setVvalue(scrollPane.getVvalue() + Vscroll);
                break;
            case LEFT:
                player.move(-1, 0);
                if(lastCell != player.getCell())
                    scrollPane.setHvalue(scrollPane.getHvalue() - Hscroll);
                break;
            case RIGHT:
                player.move(1,0);
                if(lastCell != player.getCell())
                    scrollPane.setHvalue(scrollPane.getHvalue() + Hscroll);
                break;
            case S:
                dbManager.saveGame(player);
                break;
        }
        if (!player.isPlayerAlive()) {
            gameOver();
            return;
        }
        if (player.isPlayerStandOnStairs()) { changeMap(); }
        refresh();
        displayUI();
        checkForItems();
        handleEnemies();
    }

    private void refresh() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
    }

    private void displayUI() {
        ui.getChildren().clear();

        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 1, 0);
        healthLabel.setText("" + map.getPlayer().getHealth());

        ui.add(new Label("Damage: "), 0, 1);
        damageLabel.setText("" + Math.abs(map.getPlayer().getDamage()));
        ui.add(damageLabel, 1, 1);

        ui.add(new Label("Weapon: "), 0, 2);
        weaponLabel.setText("" + map.getPlayer().getWeaponName());
        ui.add(weaponLabel, 1, 2);

        ui.add(new Label("Inventory: "), 0, 4);

        ui.add(pickUpItem, 0, 3);

        displayInventory();
    }

    private void handleEnemies() {
        List<Actor> enemies = map.getEnemies();
        for(Actor enemy : enemies) enemy.act();
    }

    private void checkForItems() {
        disablePickUpButton();
        if (map.getPlayer().isPlayerStandingInItem()) {
            activatePickUpButton();
        }
    }

    private void addEventListenerToPickUpButton() {
        pickUpItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Player player = map.getPlayer();
                player.pickUpItem();
                if (player.isPlayerHasStick()) {
                    winTheGame();
                    return;
                }
                map.getPlayer().getCell().setItem(null);
                displayUI();
                disablePickUpButton();
            }
        });
    }

    private void activatePickUpButton() {
        pickUpItem.setDisable(false);
        pickUpItem.setFocusTraversable(false);
    }

    private void disablePickUpButton() {
        if (!pickUpItem.isDisabled()) {
            pickUpItem.setDisable(true);
        }
    }

    private void displayInventory() {
        Map<String, List<Item>> inventory = map.getPlayer().getInventory();
        int rowCounter = 5;
        if (inventory.isEmpty()) {
            ui.add(new Label("Empty"), 1, rowCounter);
            return;
        }
        for (String key: inventory.keySet()) {
            ui.add(new Label(key + ": " + inventory.get(key).size()), 1, rowCounter);
            Button useButton = new Button("Use");
            useButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Item item = inventory.get(key).get(0);
                    item.useItem(map.getPlayer());
                    displayUI();
                    refresh();
                }
            });
            if (inventory.get(key).get(0) instanceof Potion) {
                if (map.getPlayer().isPlayerHealthFull()) {
                    useButton.setDisable(true);
                }
            }
            useButton.setFocusTraversable(false);
            ui.add(useButton, 3, rowCounter);
            rowCounter++;
        }
    }

    private void changeMap() {
        switch (mapName) {
            case "/map1.txt":
                mapName = "/map2.txt";
                Hscroll = 0.018;
                Vscroll = 0.045;
                break;
            case "/map2.txt":
                mapName = "/map3.txt";
                Hscroll = 0.035;
                Vscroll = 0.030;
                break;
        }
        map = MapLoader.loadMap(mapName);
        Cell cell = map.getPlayer().getCell();
        player.setCell(cell);
        map.setPlayer(player);
        canvas = new Canvas(
                map.getWidth() * Tiles.TILE_WIDTH,
                map.getHeight() * Tiles.TILE_WIDTH);
        context = canvas.getGraphicsContext2D();

        scrollPane = new ScrollPane();

        scrollPane.pannableProperty().set(true);
        scrollPane.setContent(canvas);

        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        borderPane.setCenter(scrollPane);

        refresh();
        displayUI();
    }

    public void gameOver() {
        Label gameOver = new Label("Game Over");
        ui.add(gameOver, 1, 10);
        scrollPane.setContent(null);
    }

    public void winTheGame() {
        Label win = new Label("Congratulations!!!!!!\n You have found \nThe STICK OF TRUTH!\n Whoever has the stick\n controls the universe");
        ui.add(win, 1, 10);
        scrollPane.setContent(null);
    }

    private void setupDbManager() {
        dbManager = new GameDatabaseManager();
        try {
            dbManager.setup();
        } catch (SQLException ex) {
            System.out.println("Cannot connect to database.");
        }
    }

    private void exit() {
        try {
            stop();
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
