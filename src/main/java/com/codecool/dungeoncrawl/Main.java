package com.codecool.dungeoncrawl;

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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;



public class Main extends Application {
    ScrollPane scrollPane = new ScrollPane();
    GridPane ui = new GridPane();
    Button pickUpItem = new Button("Pick up");
    GameMap map = MapLoader.loadMap();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
           map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label weaponLabel = new Label();
    Label damageLabel = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        ui.setPrefWidth(300);
        ui.setPadding(new Insets(10));

        displayUI();

        addEventListenerToPickUpButton();
        disablePickUpButton();

        BorderPane borderPane = new BorderPane();


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
        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
        displayInventory();
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                map.getPlayer().move(0, -1);
                scrollPane.setVvalue(scrollPane.getVvalue() - 0.05);
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(0, 1);
                refresh();
                scrollPane.setVvalue(scrollPane.getVvalue() + 0.05);
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
                scrollPane.setHvalue(scrollPane.getHvalue() - 0.02);
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1,0);
                scrollPane.setHvalue(scrollPane.getHvalue() + 0.02);
                refresh();
                break;
        }
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
        damageLabel.setText("" + map.getPlayer().getDamage());
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
}
