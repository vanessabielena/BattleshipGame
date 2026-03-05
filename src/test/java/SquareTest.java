import cz.cvut.fel.pjv.battleship.battleshipfx.Ship;
import cz.cvut.fel.pjv.battleship.battleshipfx.Square;
import cz.cvut.fel.pjv.battleship.battleshipfx.Board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SquareTest {
    @Test
    public void testShoot() {
        // Create a Board object for testing
        Board board = new Board(false, false, 10, 1, null);
        // Create a Square object
        Square square = board.getSquare(0, 0);

        // Test shooting on an empty square
        assertFalse(square.shoot());


        // Create a Ship object and assign it to the Square
        Ship ship = new Ship(3, true);
        board.placeShip(ship, 0, 0);
        Square square2 = board.getSquare(0, 0);

        // Test shooting on a square with a ship
        assertTrue(square2.shoot());
    }
}
