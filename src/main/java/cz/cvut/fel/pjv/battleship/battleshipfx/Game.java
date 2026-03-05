package cz.cvut.fel.pjv.battleship.battleshipfx;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.util.Duration;


public class Game extends Application {

    private ImageView shipImage = new ImageView();
    private ImageView shipImageMain = new ImageView();

    private BorderPane root;

    private HashMap<Integer, List<List<Integer>>> customShips = null;
    private Stage primaryStage;

    private Board enemyBoard, playerBoard;

    private int shipsToPlace = 5;

    private int computerShipsToPlace = 5;

    private boolean enemyTurn = false;

    private ComputerPlayer computer;

    private boolean running = false;

    private boolean isLoaded = false;

    private Move humanMoves = new Move();
    private Move computerMoves = new Move();
    private Move humanPlacedShips = new Move();
    private Move computerPlacedShips = new Move();

    private SavedGames jsonUtil = new SavedGames();

    private Random random = new Random();

    private final static Logger LOGGER = Logger.getLogger(Game.class.getName());

    private void startGame() { // zacnem hru vygenerovanim nahodnych poloh lodi pre pocitac
        int type = computerShipsToPlace;
        computer = new ComputerPlayer(playerBoard);
        while (computerShipsToPlace > 0) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            boolean isVerticalRandom = Math.random() < 0.5;
            Ship enemyShip;
            if (customShips != null) {
                enemyShip = new Ship(isVerticalRandom, customShips.get(computerShipsToPlace - 1));
                if (enemyBoard.placeShip(enemyShip, x, y, customShips.get(computerShipsToPlace - 1))) {
                    computerShipsToPlace--;
//                    computerPlacedShips.addShipMoves(enemyShip.squareArray);
                    ArrayList<Square> squareListForSaving = new ArrayList<>();
                    squareListForSaving.add(new Square(x, y, enemyBoard));
                    squareListForSaving.add(enemyShip.isVertical ? enemyBoard.getSquare(0, 1) : enemyBoard.getSquare(1, 0));
                    computerPlacedShips.addShipMoves(squareListForSaving);
                }
            } else {
                enemyShip = new Ship(computerShipsToPlace, isVerticalRandom);
                if (enemyBoard.placeShip(enemyShip, x, y)) {
                    computerShipsToPlace--;
                    computerPlacedShips.addShipMoves(enemyShip.squareArray);
                }
            }
        }
        LOGGER.info("Computer placed all of its ships.");
        running = true;
        LOGGER.info("Game has started.");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Battleship Game");
        primaryStage.setResizable(false);

        FileHandler fh = new FileHandler("log.txt");
        fh.setLevel(Level.ALL);
        fh.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(fh);
        LOGGER.setLevel(Level.ALL);
        LOGGER.info("App was opened");

        mainMenu(primaryStage);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Parent generateContent(int gridSize, int numberOfShips, HashMap<Integer, List<List<Integer>>> customShipGrid, Stage primaryStage) {
        this.primaryStage = primaryStage;
        root = new BorderPane();
        VBox textBox = new VBox();
        root.setBottom(textBox);
        textBox.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
        textBox.setAlignment(Pos.CENTER);
        textBox.setPadding(new Insets(10));
        Label firsttextLabel = new Label("Place all your ships on the grid.");
        firsttextLabel.setStyle("-fx-font-size: 20px;");
        textBox.getChildren().add(firsttextLabel);
        root.setPrefSize(1000, 650);
        Game.Title title = new Game.Title("B A T T L E S H I P S");
        title.setTranslateX(20);
        title.setTranslateY(30);
        Button saveButton = new Button("Save Game");
        saveButton.setStyle(getButtonCss(15, 10, 20));
        saveButton.setOnAction(e -> {
            System.out.println(customShips);
            GameData gd = new GameData(playerBoard.getGridSize(), customShips.size(), humanMoves.getMoves(), computerMoves.getMoves(), humanPlacedShips.getShipMoves(), computerPlacedShips.getShipMoves(), customShips);
            jsonUtil.saveJson(gd, "gameprogress.json");
        });
        textBox.getChildren().add(saveButton);
        shipsToPlace = numberOfShips;
        computerShipsToPlace = numberOfShips;
        VBox customShipView;
        if (customShipGrid != null) {
            customShipView = new CustomShipView(customShipGrid);
            root.setRight(customShipView);
        } else {
            customShipView = null;
        }
//        root.setRight(new Text("TO DO: obrazky lodiek plus časovač"));

        enemyBoard = new Board(true, false, gridSize, numberOfShips ,event -> { //event handler ktory za priebehu hry spracovava kliknutia na enemy board
            if (!running)
                return;
            Square square = (Square) event.getSource();
            humanMoves.addMove(square);
            LOGGER.info("Players move: " + square);
            if (square.wasShot)
                return;
            enemyTurn = !square.shoot();
            if (enemyBoard.ships == 0) {
                showEndGameScreenWithDelay(true);
                LOGGER.info("Human player won.");
//                System.out.println("Human player won");
            }
            if (enemyTurn)
                computerTurn();
        });

        playerBoard = new Board(false, false, gridSize, numberOfShips, event -> { //event handler ktory pred zaciatkom hry spracovava kliknutia na player board
            if (running)
                return;
            Square square = (Square) event.getSource();
            if (customShipGrid != null) {
                customShips = customShipGrid;
//                System.out.println(customShipGrid.get(shipsToPlace - 1));
                Ship newCustomShip = new Ship(event.getButton() == MouseButton.PRIMARY, customShipGrid.get(shipsToPlace - 1));
                if (playerBoard.placeShip(newCustomShip, square.x, square.y, customShipGrid.get(shipsToPlace - 1))) {
                    ArrayList<Square> squareListForSaving = new ArrayList<>();
                    squareListForSaving.add(square);
                    squareListForSaving.add(newCustomShip.isVertical ? playerBoard.getSquare(0, 1) : playerBoard.getSquare(1, 0));
                    humanPlacedShips.addShipMoves(squareListForSaving);
                    ((CustomShipView) customShipView).nextShip();
//                    System.out.println(humanPlacedShips);
                    if (--shipsToPlace == 0) {
                        LOGGER.info("All players ships were placed.");
                        Label textLabel = new Label("Game has started. Objective: take down all opponents ships.");
                        textLabel.setStyle("-fx-font-size: 20px;");
                        textBox.getChildren().clear();
                        textBox.getChildren().addAll(textLabel, saveButton);
                        startGame();
                    }
                }
            } else {
                Ship newShip = new Ship(shipsToPlace, event.getButton() == MouseButton.PRIMARY);
                if (playerBoard.placeShip(newShip, square.x, square.y)) {
                    humanPlacedShips.addShipMoves(newShip.getSquareArray());
                    if (--shipsToPlace == 0) {
                        LOGGER.info("All players ships were placed.");
                        Label textLabel = new Label("Game has started. Objective: take down all opponents ships.");
                        textLabel.setStyle("-fx-font-size: 20px;");
                        textBox.getChildren().clear();
                        textBox.getChildren().addAll(textLabel, saveButton);
                        startGame();
                    }
                }
            }
        });
        LOGGER.info("Players and computers boards were created.");
        try {
            Image image = new Image("sea2.png");
            shipImage.setImage(image);
            shipImage.setFitWidth(600);
            shipImage.setPreserveRatio(true);
        } catch (IllegalArgumentException ex) {
            LOGGER.severe("Can't load resource: " + ex.getMessage());
        }
        BackgroundImage backgroundImage = new BackgroundImage(shipImage.getImage(),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        root.setBackground(background);
        HBox hbox = new HBox(50, enemyBoard, playerBoard);
        hbox.setAlignment(Pos.CENTER);
        root.setCenter(hbox);
        root.setTop(title);
        return root;
    }

    private void computerTurn() {
        while (enemyTurn) {
            Square square = computer.computerTurn();
            computerMoves.addMove(square);
            LOGGER.info("Computers move: " + square);
            enemyTurn = square.shoot();
            if (playerBoard.ships == 0) {
                showEndGameScreenWithDelay(false);
                LOGGER.info("Computer won.");
            }
        }

    }

    private void showEndGameScreenWithDelay(boolean isWin) {
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> showEndGameScreen(isWin));
        delay.play();
    }

    private void showEndGameScreen(boolean isWin) {
        Scene endGameScene = new EndGameScreen(isWin, primaryStage);
        primaryStage.setScene(endGameScene);
    }

    public Parent mainMenu(Stage primaryStage) throws Exception {
        try {
            Image image = new Image("shipimagebg.png");
            shipImageMain.setImage(image);
            shipImageMain.setFitWidth(900);
            shipImageMain.setPreserveRatio(true);
        } catch (IllegalArgumentException ex) {
            LOGGER.severe("Can't load resource: " + ex.getMessage());
        }

        // Create a Pane to hold UI components
        Pane pane = new Pane();
        pane.setPadding(new Insets(10));

        // Create UI components
        Game.Title title = new Game.Title("B A T T L E S H I P S");
        title.setTranslateX(180);
        title.setTranslateY(180);
        Button customButton = new Button("Customize Game");
        customButton.setStyle(getButtonCss(25, 10,20));
        Button loadButton = new Button("Load game");
        loadButton.setStyle(getButtonCss(25, 10, 27));
        Button playButton = new Button("Play");
        playButton.setStyle(getButtonCss(35,20,60));
        playButton.setOnAction(e -> {
            try {
                LOGGER.info("Playing new game was chosen.");
                Scene gameScene = new Scene(generateContent(10, 5, null, primaryStage));
                primaryStage.setScene(gameScene);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        loadButton.setOnAction(e -> {
            try {
                LOGGER.info("Loading of a saved game was chosen.");
                isLoaded = true;
                GameData temporaryGD =  jsonUtil.loadJson("gameprogress.json");
                Scene gameScene = new Scene(generateContent(temporaryGD.getGridSize(), temporaryGD.getNumberOfShips(), temporaryGD.getCustomShips(), primaryStage));
                primaryStage.setScene(gameScene);
                shipsToPlace = 0;
                running = true;
                LoadGame lg = new LoadGame(jsonUtil, playerBoard, enemyBoard);
                computer = new ComputerPlayer(playerBoard);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            // Handle play button click
        });

        customButton.setOnAction(e -> {
            try {
                LOGGER.info("Customization was chosen.");
                Customization customizeScene = new Customization(primaryStage);
                primaryStage.setScene(customizeScene);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            // Handle play button click
        });

        // Position UI components on the Pane
        playButton.setLayoutX(350);
        playButton.setLayoutY(300);
        loadButton.setLayoutX(350);
        loadButton.setLayoutY(400);
        customButton.setLayoutX(320);
        customButton.setLayoutY(30);
        BackgroundImage backgroundImage = new BackgroundImage(shipImageMain.getImage(),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        pane.setBackground(background);

        // Add UI components to the Pane
        pane.getChildren().addAll(title, playButton, loadButton, customButton);

        // Create a Scene and add the Pane to it
        Scene mainScene = new Scene(pane, 900, 600);

        // Set the Scene on the Stage and show the Stage
        primaryStage.setScene(mainScene);
        LOGGER.info("Main menu was created.");
        primaryStage.show();
        return pane;

    }

    public static String getButtonCss(int textSize, int padx, int pady) {
        return "-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%), linear-gradient(#20262b, #191d22), " +
                "radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0)); -fx-background-radius: 5,4,3,5; -fx-background-insets: 0,1,2,0;" +
                "    -fx-text-fill: white;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Arial\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: "+ textSize +"px;\n" +
                "    -fx-padding:"+ padx + " " +pady + " " + padx + " " +pady + ";";
    }

    public static class Title extends StackPane{
        public Title(String name) {
            Rectangle bg = new Rectangle(530, 60);
            bg.setStroke(Color.WHITE);
            bg.setStrokeWidth(2);
            bg.setFill(null);

            Text text = new Text(name);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Times New Roman", FontWeight.SEMI_BOLD, 50));

            setAlignment(Pos.CENTER);
            getChildren().addAll(bg,text);
        }
    }


}
