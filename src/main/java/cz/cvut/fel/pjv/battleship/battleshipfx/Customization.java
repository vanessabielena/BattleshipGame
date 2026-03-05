
package cz.cvut.fel.pjv.battleship.battleshipfx;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.effect.BoxBlur;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Customization extends Scene {

    boolean isCustomized = false;
//    List<List<Integer>> squareList = new ArrayList<>();
    private Pane pane;
    private HBox boardsContainer = new HBox();
    private Board customBoard;
    private HashMap<String, Integer> gameData = new HashMap<String, Integer>();
    private HashMap<Integer, List<List<Integer>>> shipsData = new HashMap<Integer, List<List<Integer>>>();
    private int gridSize = 10;

    public Customization(Stage primaryStage) {
        super(new StackPane());
        pane = (StackPane) getRoot();

        pane.setPrefWidth(900); // Set preferred width
        pane.setPrefHeight(600); // Set preferred height

        Pane backgroundPane = new Pane();
        Image backgroundImage = new Image("shipimagebg.png");
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        backgroundPane.setBackground(new Background(background));
        BoxBlur blur = new BoxBlur(10, 10, 3); // Adjust the blur radius as needed
        backgroundPane.setEffect(blur);


        Label numberShipsLabel = new Label("Select the number of ships:");
        numberShipsLabel.setStyle("-fx-font-size: 20px;");
        ComboBox<Integer> numberShipsCombo = new ComboBox<>();
        numberShipsCombo.getItems().addAll(3, 4, 5, 6);
        Label gridSizeLabel = new Label("Grid Size:");
        gridSizeLabel.setStyle("-fx-font-size: 20px;");
        ComboBox<String> gridSizeCombo = new ComboBox<>();
        gridSizeCombo.getItems().addAll("Small (6*6)", "Medium (10*10)", "Large (14*14)");


        Button playButton = new Button("Play");
        playButton.setStyle(Game.getButtonCss(35, 20, 60));

        playButton.setOnAction(e -> {
            try {
                if (isCustomized) {
                    System.out.println(shipsData);
                    Scene gameScene = new Scene(new Game().generateContent(gridSize, shipsData.size(), shipsData, primaryStage));
                    primaryStage.setScene(gameScene);
                } else {
                    Scene gameScene = new Scene(new Game().generateContent(gridSize, 5, null, primaryStage));
                    primaryStage.setScene(gameScene);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(numberShipsLabel, 0, 0);
        gridPane.add(numberShipsCombo, 1, 0);
        gridPane.add(gridSizeLabel, 0, 1);
        gridPane.add(gridSizeCombo, 1, 1);
        gridPane.add(playButton, 0, 4);

        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setPercentWidth(35);
        gridPane.getColumnConstraints().add(colConstraints);
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(10);
        gridPane.getRowConstraints().add(rowConstraints);
        gridPane.setVgap(10);

        Image backgroundWhiteBlur = new Image("whiteblur.png");
        BackgroundImage gridBackground = new BackgroundImage(backgroundWhiteBlur,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        gridPane.setBackground(new Background(gridBackground));



        String selectedSize = gridSizeCombo.getValue();
        numberShipsCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
//                System.out.println("Selected number: " + newValue);
                // Perform actions based on the selected size
                boardsContainer.getChildren().clear();
                boardsContainer = createCustomBoards(newValue);
                gridPane.add(boardsContainer, 0, 2, 2, 1);

            } else {
                System.out.println("No size selected");
                // Handle case when no size is selected
            }
        });
        gridSizeCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue == "Small (6*6)") {
                    gridSize = 6;
                } else if (newValue == "Medium (10*10)") {
                    gridSize = 10;
                } else if (newValue == "Large (14*14)") {
                    gridSize = 14;
                }
                System.out.println("Selected number: " + gridSize);

            } else {
                System.out.println("No size selected");
                // Handle case when no size is selected
            }
        });

//        pane.getChildren().add(gridPane);
        pane.getChildren().addAll(backgroundPane, gridPane);


    }

    private HBox createCustomBoards(int numberOfShips) {
        HBox boards = new HBox();
        for (int i = 0; i < numberOfShips; i++) {
            List<List<Integer>> squareList = new ArrayList<>();
            for (int y = 0; y < 4; y++) {
                List<Integer> row = new ArrayList<>();
                for (int x = 0; x < 4; x++) {
                    row.add(0);
                }
                squareList.add(row);
            }
            shipsData.put(i, squareList);
            int finalI = i;
            customBoard = new Board(false, true, 4, numberOfShips, event -> {
                Square square = (Square) event.getSource();
                int x = square.x;
                int y = square.y;
                shipsData.get(finalI).get(y).set(x, 1);
                square.setFill(Color.LIGHTGRAY);
                square.setStroke(Color.BLACK);
                isCustomized = true;
//                System.out.println(shipsData);
//            Ship newShip = new Ship(1, true);

            });
            boards.getChildren().add(customBoard);
        }
        return boards;
    }
}
