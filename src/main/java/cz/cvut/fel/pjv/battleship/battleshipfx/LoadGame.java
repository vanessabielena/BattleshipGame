package cz.cvut.fel.pjv.battleship.battleshipfx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LoadGame {
    private SavedGames jsonutil;
    private GameData gd;
    private HashMap<Integer, List<List<Integer>>> customShips;


    public LoadGame(SavedGames jsonutil, Board humanBoard, Board computerBoard) {
        this.jsonutil = jsonutil;
        this.gd = jsonutil.loadJson("gameprogress.json");
//        System.out.println(gd);
        this.customShips = gd.getCustomShips();
        List<List<List<Integer>>> humanPlacedShips = gd.getHumanShips();
        List<List<List<Integer>>> computerPlacedShips = gd.getComputerShips();

        if (customShips == null) {
            for (List<List<Integer>> ship : humanPlacedShips) {
                ArrayList<Square> squareList = new ArrayList<>();
                for (List<Integer> squarePosition : ship) {
                    Square square = humanBoard.getSquare(squarePosition.get(0), squarePosition.get(1));
                    squareList.add(square);
                }
                Ship newShip = new Ship(ship.size(), shipIsVertical(squareList));
                newShip.setSquareArray(squareList);
                humanBoard.placeShip(newShip, squareList.get(0).x, squareList.get(0).y);
            }
            for (List<List<Integer>> ship : computerPlacedShips) {
                ArrayList<Square> squareList = new ArrayList<>();
                for (List<Integer> squarePosition : ship) {
                    Square square = computerBoard.getSquare(squarePosition.get(0), squarePosition.get(1));
                    squareList.add(square);
                }
                Ship newShip = new Ship(ship.size(), shipIsVertical(squareList));
                newShip.setSquareArray(squareList);
                computerBoard.placeShip(newShip, squareList.get(0).x, squareList.get(0).y);
            }
        } else {
            placeCustomShips(humanBoard, humanPlacedShips);
            placeCustomShips(computerBoard, computerPlacedShips);

        }

        List<List<Integer>> humanMoves = gd.getComputerMoves();
        for (List<Integer> squarePosition : humanMoves) {
            Square shotSquare = humanBoard.getSquare(squarePosition.get(0), squarePosition.get(1));
            shotSquare.shoot();
        }
        List<List<Integer>> computerMoves = gd.getHumanMoves();
        for (List<Integer> squarePosition : computerMoves) {
            Square shotSquare = computerBoard.getSquare(squarePosition.get(0), squarePosition.get(1));
            shotSquare.shoot();
        }


    }

    private void placeCustomShips(Board board, List<List<List<Integer>>> placedShips) {
        Collections.reverse(placedShips);
        for (int i = 0; i < customShips.size(); i++) {
            ArrayList<Square> squareList = new ArrayList<>();
            for (List<Integer> squarePosition : placedShips.get(i)) {
                Square square = board.getSquare(squarePosition.get(0), squarePosition.get(1));
                squareList.add(square);
            }
            Ship newShip = new Ship(shipIsVertical(placedShips.get(i).get(1)), customShips.get(i));
            newShip.setSquareArray(squareList);
            board.placeShip(newShip, squareList.get(0).x, squareList.get(0).y, customShips.get(i));
        }
    }
//    This is only temporary function, because i will be changing the way ships are displayed
    public boolean shipIsVertical(ArrayList<Square> squares) {
        int x = squares.get(0).x;
        int y = squares.get(0).y;
        if (squares.size() == 1) {
            return true;
        } else if (squares.get(1).x == x) {
            return true;
        }
        return false;
    }

    public boolean shipIsVertical(List<Integer> pointer) {
        if (pointer.get(0) == 1) {
            return false;
        }
        return true;
    }
}
