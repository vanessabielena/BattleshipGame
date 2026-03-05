package cz.cvut.fel.pjv.battleship.battleshipfx;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;




import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
//import javafx.scene.shape.Rectangle;

public class Board extends Parent {
    private VBox rows = new VBox();
    private boolean enemy = false;

    private boolean custom = false;
    public int ships = 5;

    private int gridSize;

    public Board(boolean enemy, boolean custom, int size, int numberOfShips, EventHandler<? super MouseEvent> eventHandler) {
        this.enemy = enemy;
        this.custom = custom;
        this.ships = numberOfShips;
        this.gridSize = size;
        for (int y = 0; y < size; y++) {
            HBox row = new HBox();
            for (int x = 0; x < size; x++) {
                Square c = new Square(x, y, this);
                c.setOnMouseClicked(eventHandler);
                row.getChildren().add(c);
            }

            rows.getChildren().add(row);
        }

        getChildren().add(rows);
        if (enemy) {
            rows.setStyle("-fx-border-style: solid inside;" + "-fx-border-width: 5;" + "-fx-border-color: red;");
        } else {
            rows.setStyle("-fx-border-style: solid inside;" + "-fx-border-width: 5;" + "-fx-border-color: green;");
        }
    }

    public boolean placeShip(Ship ship, int x, int y) {
        ArrayList<Square> cellsList = new ArrayList<Square>();
        if (canPlaceShip(ship, x, y)) {
            int length = ship.length;

            if (ship.isVertical) {
                for (int i = y; i < y + length; i++) {
                    Square square = getSquare(x, i);
                    square.ship = ship;
                    cellsList.add(square);
                    if (!enemy) {
                        square.setFill(Color.LIGHTGRAY);
                        square.setStroke(Color.BLACK);
                    }
                }
            }
            else {
                for (int i = x; i < x + length; i++) {
                    Square square = getSquare(i, y);
                    square.ship = ship;
                    cellsList.add(square);
                    if (!enemy) {
                        square.setFill(Color.LIGHTGRAY);
                        square.setStroke(Color.BLACK);
                    }
                }
            }
            ship.squareArray = cellsList;
            return true;
        }

        return false;
    }

    public boolean placeShip(Ship ship, int x, int y, List<List<Integer>> customShipGrid) {
        ArrayList<Square> cellsList = new ArrayList<Square>();
        if (!ship.isVertical)
//            Collections.reverse(customShipGrid);
            customShipGrid = flipGrid(customShipGrid);
        System.out.println(customShipGrid);
        if (canPlaceCustomShip(x, y, customShipGrid)) {
            for (int i = 0; i < 4; i++) { //rows index
                for (int j = 0; j < 4; j++) { //column index
                    if (customShipGrid.get(i).get(j) == 1) {
                        Square square = getSquare(x + j, y + i);
                        square.ship = ship;
                        cellsList.add(square);
                        if (!enemy) {
                            square.setFill(Color.LIGHTGRAY);
                            square.setStroke(Color.BLACK);
                        }
                    }
                }
            }
            ship.squareArray = cellsList;
            return true;
        }
        return false;
    }

    public static List<List<Integer>> flipGrid(List<List<Integer>> grid) {
        List<List<Integer>> flippedGrid = new ArrayList<>();

        int numRows = grid.size();
        int numCols = grid.get(0).size();

        for (int col = 0; col < numCols; col++) {
            List<Integer> flippedColumn = new ArrayList<>();
            for (int row = 0; row < numRows; row++) {
                flippedColumn.add(grid.get(row).get(col));
            }
            flippedGrid.add(flippedColumn);
        }

        return flippedGrid;
    }


    public Square getSquare(int x, int y) {
        return (Square)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    public Square[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[] {
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1),
                new Point2D(x + 1, y + 1),
                new Point2D(x + 1, y - 1),
                new Point2D(x - 1, y - 1),
                new Point2D(x - 1, y + 1)
        };

        List<Square> neighbors = new ArrayList<Square>();

        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbors.add(getSquare((int)p.getX(), (int)p.getY()));
            }
        }

        return neighbors.toArray(new Square[0]);
    }

    public Square[] getNeighbors(Square square) {
        int x = square.x;
        int y = square.y;
        Square[] neighbors = getNeighbors(x, y);
        return neighbors;
    }

    private boolean canPlaceShip(Ship ship, int x, int y) {
        int length = ship.length;

        if (ship.isVertical) {
            for (int i = y; i < y + length; i++) {
                if (!isValidPoint(x, i))
                    return false;

                Square square = getSquare(x, i);
                if (square.ship != null)
                    return false;

                for (Square neighbor : getNeighbors(x, i)) {
                    if (!isValidPoint(x, i))
                        return false;

                    if (neighbor.ship != null)
                        return false;
                }
            }
        }
        else {
            for (int i = x; i < x + length; i++) {
                if (!isValidPoint(i, y))
                    return false;

                Square square = getSquare(i, y);
                if (square.ship != null)
                    return false;

                for (Square neighbor : getNeighbors(i, y)) {
                    if (!isValidPoint(i, y))
                        return false;

                    if (neighbor.ship != null)
                        return false;

                }
            }
        }

        return true;
    }

    private boolean canPlaceCustomShip(int x, int y, List<List<Integer>> customShipGrid) {
        for (int i = 0; i < 4; i++) { //rows index therefore y
            for (int j = 0; j < 4; j++) { //column index therefore x
                if (customShipGrid.get(i).get(j) == 1) {
                    if (!isValidPoint(x + j, y + i))
                        return false;
                    Square square = getSquare(x + j, y + i);
                    if (square.ship != null)
                        return false;
//                    System.out.println("------------------------------");
                    for (Square neighbor : getNeighbors(x + j, y + i)) {
//                        System.out.println(neighbor);
                        if (!isValidPoint(x + j, y + i))
                            return false;

                        if (neighbor.ship != null)
                            return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }

    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < gridSize && y >= 0 && y < gridSize;
    }

    public void drawCross(Square square) {
        // Display a cross
        Line line1 = new Line(0, 0, 30, 30);
        Line line2 = new Line(0, 30, 30, 0);
        line1.setStroke(Color.BLACK);
        line2.setStroke(Color.BLACK);
        getChildren().addAll(line1, line2);
    }

    public void clearSpaceAround(Ship ship, int x, int y) {
        Square[] squareArray = new Square[ship.squareArray.size()];
        squareArray = ship.squareArray.toArray(squareArray);
        for (Square square : squareArray) {
            for (Square neighbor : getNeighbors(square)) {
                if (neighbor.ship == null)
                    neighbor.shoot();
//                    neighbor.setFill(Color.DARKBLUE);
            }
        }

    }

    public boolean isCustom() {
        return custom;
    }

    public int getGridSize() {
        return gridSize;
    }
}
