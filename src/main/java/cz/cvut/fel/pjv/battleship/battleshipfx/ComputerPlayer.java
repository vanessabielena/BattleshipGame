package cz.cvut.fel.pjv.battleship.battleshipfx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ComputerPlayer{

    private Board playerBoard;
    private Random random = new Random();
    private Square lastHit = null;
    private List<Square> lastHitSquares = new ArrayList<>();
    public ComputerPlayer(Board playerBoard) {
        this.playerBoard = playerBoard;
    }

    public Square computerTurn() {
        boolean shotEmptySquare = false;
        Square square = null;
        while (!shotEmptySquare) {
            if (lastHit != null) {
                int count = 0;
                Square[] logicalSteps = playerBoard.getNeighbors(lastHit);
                for (Square s : logicalSteps) {
                    if (s.wasShot)
                        count++;
                }
                if (count == logicalSteps.length) {
                    lastHit = lastHitSquares.get(0);
                    continue;
                }
                int index = random.nextInt(logicalSteps.length);
                square = logicalSteps[index];
            } else {
                int x = random.nextInt(playerBoard.getGridSize());
                int y = random.nextInt(playerBoard.getGridSize());
                square = playerBoard.getSquare(x, y);
            }
            if (square.wasShot)
                continue;
            shotEmptySquare = true;
        }
        if (square.ship != null) {
            if (square.ship.getHealth() == 1){
                lastHit = null;
                lastHitSquares.clear();
            } else {
                lastHit = square;
                lastHitSquares.add(square);
            }
        }
        return square;
    }
}
