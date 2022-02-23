package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class Main extends Application {
    GridPane ui = new GridPane();
    Button pickUpItem = new Button("Pick up");
    Label inventoryLabel = new Label();
    GameMap map = MapLoader.loadMap();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        ui.setPrefWidth(300);
        ui.setPadding(new Insets(10));

        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 1, 0);

        ui.add(new Label("Inventory: "), 0, 2);
        ui.add(inventoryLabel, 1, 3);

        ui.add(pickUpItem, 0, 1);
        addEventListenerToPickUpButton();
        disablePickUpButton();

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

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
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(0, 1);
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1,0);
                refresh();
                break;
        }
        checkForItems();
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
                displayInventory();
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
        StringBuilder sb = new StringBuilder();
        if (inventory.size() == 0) {
            sb.append("Empty!");
        }
        for (String key : inventory.keySet()) {
            sb.append(key).append(": ").append(inventory.get(key).size());
            sb.append("\n");
        }
        inventoryLabel.setText(sb.toString());
    }
}
