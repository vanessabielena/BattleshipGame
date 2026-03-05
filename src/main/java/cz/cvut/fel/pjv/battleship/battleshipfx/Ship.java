package cz.cvut.fel.pjv.battleship.battleshipfx;


import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Ship extends Parent {
    public int length;
    public boolean isVertical = true;

    private int health;
    public ArrayList<Square> squareArray;

    public Ship(int length, boolean isVertical) {
        this.length = length;
        this.isVertical = isVertical;
        health = length;

        VBox vbox = new VBox();
        for (int i = 0; i < length; i++) {
            Rectangle square = new Rectangle(30, 30);
            square.setFill(null);
            square.setStroke(Color.BLACK);
            vbox.getChildren().add(square);
        }
        getChildren().add(vbox);
    }

    public Ship(boolean isVertical, List<List<Integer>> customShipGrid) {
        VBox vbox = new VBox();
        this.isVertical = isVertical;
        this.length = 0;
        for (List<Integer> row : customShipGrid) {
            for (int item : row) {
                if (item == 1) {
                    this.length++;
                    Rectangle square = new Rectangle(30, 30);
                    square.setFill(null);
                    square.setStroke(Color.BLACK);
                    vbox.getChildren().add(square);
                }
            }
        }
        getChildren().add(vbox);
        this.health = this.length;
    }

    public void hit() {
        health--;
    }

    public ArrayList<Square> getSquareArray() {
        return squareArray;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setVertical(boolean vertical) {
        isVertical = vertical;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setSquareArray(ArrayList<Square> squareArray) {
        this.squareArray = squareArray;
    }

    public int getHealth() {
        return health;
    }
}