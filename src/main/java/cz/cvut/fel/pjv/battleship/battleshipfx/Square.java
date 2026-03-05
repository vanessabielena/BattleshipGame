package cz.cvut.fel.pjv.battleship.battleshipfx;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square extends Rectangle {
    public int x, y;
    public Ship ship = null;
    public boolean wasShot = false;

    private Board board;

    public Square(int x, int y, Board board) {
//        super(15, 15);
        super(board.isCustom() ? 20 : 30, board.isCustom() ? 20 : 30);
        setOpacity(0.7);
        this.x = x;
        this.y = y;
        this.board = board;
        setFill(Color.LIGHTBLUE);
        setStroke(Color.WHITE);
    }

    public boolean shoot() {
        wasShot = true;
        setFill(Color.DARKBLUE);

        if (ship != null) {
            ship.hit();
            setFill(Color.RED);
            if (!ship.isAlive()) {
                board.ships--;
                board.clearSpaceAround(ship, x, y);
            }
            return true;
        }

        return false;
    }

    public void fillBlack() {
        this.setFill(Color.BLACK);
    }

    @Override
    public String toString() {
        return "Square{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }


}