package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Potion;
import com.codecool.dungeoncrawl.model.GameState;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class Main extends Application {
    ScrollPane scrollPane = new ScrollPane();
    GridPane ui = new GridPane();
    Button pickUpItem = new Button("Pick up");
    String mapName = "src/main/resources/map1.txt";
    GameMap map = MapLoader.loadCurrentMap(mapName);
    Player player = map.getPlayer();
    BorderPane borderPane = new BorderPane();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
           map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label weaponLabel = new Label();
    Label damageLabel = new Label();
    GameDatabaseManager dbManager;
    Stage primaryStage;
    Stage saveLoadPopUp = new Stage();
    Button loadGame = new Button("Load Game");

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
        loadGame.setOnAction(e -> createModalForLoad());
        loadGame.setFocusTraversable(false);

        scrollPane.pannableProperty().set(true);
        scrollPane.setContent(canvas);

        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        borderPane.setCenter(scrollPane);
        borderPane.setRight(ui);

        borderPane.setPrefHeight(640);
        borderPane.setPrefWidth(1100);


        Scene scene = new Scene(borderPane);
        this.primaryStage = primaryStage;
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
        KeyCombination saveCombination = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        if (exitCombinationMac.match(keyEvent)
                || exitCombinationWin.match(keyEvent)
                || keyEvent.getCode() == KeyCode.ESCAPE) {
            exit();
        } else if (saveCombination.match(keyEvent)) {
            createModalForSaving();
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
                    scrollPane.setVvalue(scrollPane.getVvalue() - map.getVerticalScroll());
                break;
            case DOWN:
                player.move(0, 1);
                if(lastCell != player.getCell())
                    scrollPane.setVvalue(scrollPane.getVvalue() + map.getVerticalScroll());
                break;
            case LEFT:
                player.move(-1, 0);
                if(lastCell != player.getCell())
                    scrollPane.setHvalue(scrollPane.getHvalue() - map.getHorizontalScroll());
                break;
            case RIGHT:
                player.move(1,0);
                if(lastCell != player.getCell())
                    scrollPane.setHvalue(scrollPane.getHvalue() + map.getHorizontalScroll());
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
        ui.add(loadGame, 1, 3);

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
            case "src/main/resources/map1.txt":
                mapName = "src/main/resources/map2.txt";
                break;
            case "src/main/resources/map2.txt":
                mapName = "src/main/resources/map3.txt";
                break;
        }
        map = MapLoader.loadCurrentMap(mapName);
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

    private void showModal(GridPane modalUi, Button button, String title) {
        saveLoadPopUp = new Stage();
        saveLoadPopUp.setTitle(title);
        BorderPane modal = new BorderPane();

        modalUi.setPadding(new Insets(10, 10, 10, 10));
        modalUi.setVgap(5);
        modalUi.setHgap(5);
        modalUi.setPrefWidth(300);
        modalUi.setPrefHeight(100);

        GridPane buttons = new GridPane();
        buttons.setPadding(new Insets(0, 10, 10, 57));
        buttons.setVgap(5);
        buttons.setHgap(30);
        buttons.setPrefWidth(300);
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> saveLoadPopUp.close());
        button.setFocusTraversable(false);
        cancel.setFocusTraversable(false);
        buttons.add(button, 0, 0);
        buttons.add(cancel, 1, 0);

        modal.setCenter(modalUi);
        modal.setBottom(buttons);

        Scene modalScene = new Scene(modal);
        saveLoadPopUp.setScene(modalScene);

        saveLoadPopUp.initOwner(primaryStage);
        saveLoadPopUp.initModality(Modality.APPLICATION_MODAL);
        saveLoadPopUp.showAndWait();

    }

    private void createModalForSaving() {
        GridPane modalUi = new GridPane();

        final TextField playerNameInput = new TextField();
        playerNameInput.setPromptText("Enter your name:");
        playerNameInput.setFocusTraversable(false);

        modalUi.add(playerNameInput, 0, 0);
        modalUi.setAlignment(Pos. CENTER);
        GridPane.setHalignment(playerNameInput, HPos. CENTER);

        Button saveGame = new Button("Save Game");
        saveGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String playerName = playerNameInput.getText();
                player.setName(playerName);
                if (dbManager.isPlayerExists(playerName)) {
                    createAlertWindow();
                } else {
                    dbManager.saveGame(player, map, scrollPane.getHvalue(), scrollPane.getVvalue(), mapName);
                    saveLoadPopUp.close();
                }
            }
        });

        showModal(modalUi, saveGame, "Save Game");
    }

    private void createAlertWindow() {
        Stage alertWindow = new Stage();
        BorderPane modal = new BorderPane();

        GridPane alertUi = new GridPane();

        Label text = new Label("Would you like to overwrite the already existing state?");
        alertUi.add(text,0, 0);
        alertUi.setAlignment(Pos.CENTER);
        GridPane.setHalignment(text, HPos.CENTER);

        GridPane buttons = new GridPane();
        buttons.setPadding(new Insets(10, 10, 10, 140));
        buttons.setVgap(5);
        buttons.setHgap(5);
        buttons.setPrefWidth(300);
        Button yes = new Button("Yes");
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                dbManager.updateSavedGame(player);
                saveLoadPopUp.close();
                alertWindow.close();
            }
        });
        Button no = new Button("No");
        no.setOnAction(e -> alertWindow.close());
        yes.setFocusTraversable(false);
        no.setFocusTraversable(false);
        buttons.add(yes, 0, 0);
        buttons.add(no, 1, 0);

        modal.setCenter(alertUi);
        modal.setBottom(buttons);

        Scene modalScene = new Scene(modal);
        alertWindow.setScene(modalScene);

        alertWindow.setTitle("Overwrite Save");
        alertWindow.initOwner(primaryStage);
        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.showAndWait();
    }

    private void createModalForLoad() {
        GridPane modalUi = new GridPane();

        ListView<String> list = new ListView<>();
        List<GameState> gameStates = dbManager.getAllGameState();

        ObservableList<String> items = FXCollections.observableArrayList (getListWithPlayerNames(gameStates));

        list.setItems(items);
        list.setFocusTraversable(false);
        modalUi.add(list, 0, 0);
        modalUi.setAlignment(Pos. CENTER);
        GridPane.setHalignment(list, HPos. CENTER);

        list.setPrefWidth(200);
        list.setPrefHeight(80);

        Button loadGame = new Button("Load Game");
        loadGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                    String playerName = list.getFocusModel().getFocusedItem();
                    GameState gameState = getGameStateByPlayerName(gameStates, playerName);
                    saveLoadPopUp.close();
                    loadGame(gameState);
            }
        });

        showModal(modalUi, loadGame, "Load Game");
    }

    private List<String> getListWithPlayerNames(List<GameState> gameStates) {
        List<String> playerNames = new ArrayList<>();
        for (GameState gameState : gameStates) {
            String playerName = gameState.getPlayer().getPlayerName();
            playerNames.add(playerName);
        }
        return playerNames;
    }

    private GameState getGameStateByPlayerName(List<GameState> gameStates,String playerName) {
        for (GameState gameState : gameStates) {
            if (gameState.getPlayer().getPlayerName().equals(playerName)) {
                return gameState;
            }
        }
        return null;
    }

    private void loadGame(GameState gameState) {
        MapLoader.writeMap(gameState.getCurrentMap());
        player.setInventory(gameState.getInventory().convertToInventory());
        map = MapLoader.loadMap(MapLoader.CURRENT_MAP);
        Cell cell = map.getPlayer().getCell();
        player.setCell(cell);
        map.setPlayer(player);
        mapName = gameState.getMapName();
        canvas = new Canvas(
                map.getWidth() * Tiles.TILE_WIDTH,
                map.getHeight() * Tiles.TILE_WIDTH);
        context = canvas.getGraphicsContext2D();

        scrollPane = new ScrollPane();

        scrollPane.pannableProperty().set(true);
        scrollPane.setContent(canvas);

        scrollPane.setHvalue(gameState.getHscroll());
        scrollPane.setVvalue(gameState.getVscroll());

        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        borderPane.setCenter(scrollPane);

        refresh();
        displayUI();
    }
}
