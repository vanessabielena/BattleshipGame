package cz.cvut.fel.pjv.battleship.battleshipfx;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomShipView extends VBox {

    private List<VBox> boards = new ArrayList<>();
    private int pointer = 0;
    private int shipNumber;
    public CustomShipView(HashMap<Integer, List<List<Integer>>> data) {
        super();
        setAlignment(Pos.CENTER);
        this.shipNumber = data.size();
        Label text1 = new Label("Ships in");
        Label text2 = new Label("the game:");
        text1.setStyle("-fx-font-size: 20px;");
        text2.setStyle("-fx-font-size: 20px;");
        getChildren().add(text1);
        getChildren().add(text2);

        for (int i = data.size() - 1; i >= 0; i--) {
            List<List<Integer>> boardData = data.get(i);
            if (boardData != null) {
                VBox board = createBoard(boardData);
                getChildren().add(board);
                boards.add(board);
            }
        }
        boards.get(0).setStyle("-fx-border-style: solid inside;" + "-fx-border-width: 7;" + "-fx-border-color: yellow;");

    }

    private VBox createBoard(List<List<Integer>> data) {
        VBox box = new VBox();
        Board board = new Board(false, true, 4, data.size(), event -> {
            // Handle board click event
//            Square square = (Square) event.getSource();
//            int x = square.x;
//            int y = square.y;
//            System.out.println("Clicked: " + x + ", " + y);
        });

        // Customize the board based on the data
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                int value = data.get(y).get(x);
                Square square = board.getSquare(x, y);
                if (value == 1) {
                    square.setFill(Color.LIGHTGRAY);
                    square.setStroke(Color.BLACK);
                }
                // Customize other values as needed
            }
        }
        box.getChildren().add(board);
        return box;
    }

    public void nextShip() {
        boards.get(pointer).setStyle(null);
        pointer++;
        if (pointer != shipNumber) {
            boards.get(pointer).setStyle("-fx-border-style: solid inside;" + "-fx-border-width: 7;" + "-fx-border-color: yellow;");
        }
    }
}
