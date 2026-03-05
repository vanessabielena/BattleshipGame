import cz.cvut.fel.pjv.battleship.battleshipfx.Ship;
import cz.cvut.fel.pjv.battleship.battleshipfx.Square;
import cz.cvut.fel.pjv.battleship.battleshipfx.Board;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void testPlaceShip() {
        Board board = new Board(false, false, 10, 1, null);
        Ship ship = new Ship(3, true);

        // Place the ship at coordinates (2, 3)
        boolean result = board.placeShip(ship, 2, 3);

        assertTrue(result);
        assertEquals(ship, board.getSquare(2, 3).ship);
        assertEquals(ship, board.getSquare(2, 4).ship);
        assertEquals(ship, board.getSquare(2, 5).ship);
        assertNull(board.getSquare(2, 6).ship);
    }

    @Test
    public void testPlaceCustomShip() {
        // Custom ship grid
        List<List<Integer>> customShipGrid = new ArrayList<>();
        customShipGrid.add(Arrays.asList(1, 1, 0, 1));
        customShipGrid.add(Arrays.asList(0, 1, 1, 1));
        customShipGrid.add(Arrays.asList(1, 0, 1, 0));
        customShipGrid.add(Arrays.asList(1, 1, 1, 0));

        Board board = new Board(false, true, 10, 1, null);
        Ship ship = new Ship(true, customShipGrid);
        // Place the custom ship
        boolean result = board.placeShip(ship, 4, 2, customShipGrid);

        assertTrue(result);
        assertEquals(ship, board.getSquare(4, 2).ship);
        assertEquals(ship, board.getSquare(5, 2).ship);
        assertEquals(ship, board.getSquare(5, 3).ship);
        assertEquals(ship, board.getSquare(6, 3).ship);
        assertEquals(ship, board.getSquare(6, 4).ship);
        assertNull(board.getSquare(3, 2).ship);
    }


    @Test
    public void testCanPlaceShip() {
        Board board = new Board(false, false, 10, 2, null);
        Ship ship = new Ship(1, true);

        // Place the ship
        boolean result = board.placeShip(ship, 2, 3);

        assertTrue(result);

        // Try placing another ship at overlapping coordinates (2, 4)
        Ship overlappingShip = new Ship(3, true);
        result = board.placeShip(overlappingShip, 2, 4);

        assertFalse(result);
        assertNull(board.getSquare(2, 4).ship);
        assertNull(board.getSquare(2, 5).ship);
        assertNull(board.getSquare(2, 6).ship);
    }

    @Test
    public void testGetSquare() {
        Board board = new Board(false, false, 10, 1, null);

        Square square = board.getSquare(3, 5);

        assertNotNull(square);
        assertEquals(3, square.x);
        assertEquals(5, square.y);
    }
}

