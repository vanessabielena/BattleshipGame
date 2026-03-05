package cz.cvut.fel.pjv.battleship.battleshipfx;

import java.util.ArrayList;
import java.util.List;

public class Move {
    private List<List<Integer>> moves = new ArrayList<>();
    private List<List<List<Integer>>> shipMoves = new ArrayList<>();

    public Move() {
    }

    public void addMove(Square square) {
        List<Integer> position = new ArrayList<>();
        position.add(square.x);
        position.add(square.y);
//        System.out.println(position);
        moves.add(position);
    }

    public void addMove(List<Square> squares) {
        for (Square square : squares) {
            List<Integer> position = new ArrayList<>();
            position.add(square.x);
            position.add(square.y);
//            System.out.println(position);
            moves.add(position);
        }
    }

    public void addShipMoves(ArrayList<Square> squaresList) {
        List<Square> squares = (List<Square>) squaresList;
//        for (List<Square> squares : squaresList) {
        List<List<Integer>> parts = new ArrayList<>();
        for (Square square : squares) {
            List<Integer> position = new ArrayList<>();
            position.add(square.x);
            position.add(square.y);
            parts.add(position);
        }
        shipMoves.add(parts);
//        System.out.println(shipMoves);
    }

    public List<List<Integer>> getMoves() {
        return moves;
    }

    public List<List<List<Integer>>> getShipMoves() {
        return shipMoves;
    }
}
