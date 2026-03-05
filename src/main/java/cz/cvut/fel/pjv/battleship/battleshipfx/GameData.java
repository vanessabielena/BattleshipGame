package cz.cvut.fel.pjv.battleship.battleshipfx;

import java.util.*;

public class GameData {
    private int gridSize;
    private int numberOfShips;
    //  private Map<Integer, String> shipShape = new LinkedHashMap<>();
    private List<List<Integer>> humanMoves;
    private List<List<Integer>> computerMoves;
    private List<List<List<Integer>>> humanShips;
    private List<List<List<Integer>>> computerShips;
    private HashMap<Integer, List<List<Integer>>> customShips;
//    private boolean isCustom = false;

    public GameData() {
    }

    public GameData(int gridSize, int numberOfShips, List<List<Integer>> humanMoves, List<List<Integer>> computerMoves, List<List<List<Integer>>> humanShips, List<List<List<Integer>>> computerShips, HashMap<Integer, List<List<Integer>>> customShips) {
//        this.jmeno = jmeno;
        this.humanShips = humanShips;
        this.computerShips = computerShips;
        this.gridSize = gridSize;
        this.numberOfShips = numberOfShips;
        this.humanMoves = humanMoves;
        this.computerMoves = computerMoves;
        this.customShips = customShips;
//        if (customShips != null) {
//            this.isCustom = true;
//        }
    }

    public int getGridSize() {
        return gridSize;
    }

    public int getNumberOfShips() {
        return numberOfShips;
    }

    public List<List<Integer>> getHumanMoves() {
        return humanMoves;
    }

    public List<List<Integer>> getComputerMoves() {
        return computerMoves;
    }

    public List<List<List<Integer>>> getHumanShips() {
        return humanShips;
    }

    public List<List<List<Integer>>> getComputerShips() {
        return computerShips;
    }

    public HashMap<Integer, List<List<Integer>>> getCustomShips() {
        return customShips;
    }

//    public boolean isCustom() {
//        return isCustom;
//    }

    @Override
    public String toString() {
        return "GameData{" +
                "gridSize=" + gridSize +
                ", numberOfShips=" + numberOfShips +
                ", humanMoves=" + humanMoves +
                ", computerMoves=" + computerMoves +
                ", humanShips=" + humanShips +
                ", computerShips=" + computerShips +
                ", customShips=" + customShips +
                '}';
    }
}
