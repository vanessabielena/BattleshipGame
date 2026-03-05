import cz.cvut.fel.pjv.battleship.battleshipfx.Ship;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShipTest {

    @Test
    public void testHit() {
        Ship ship = new Ship(4, false);
        assertEquals(4, ship.getHealth());

        ship.hit();
        assertEquals(3, ship.getHealth());

        ship.hit();
        assertEquals(2, ship.getHealth());

        ship.hit();
        assertEquals(1, ship.getHealth());

        ship.hit();
        assertEquals(0, ship.getHealth());
    }

    @Test
    public void testIsAlive() {
        Ship ship = new Ship(3, true);
        assertTrue(ship.isAlive());

        ship.hit();
        assertTrue(ship.isAlive());

        ship.hit();
        assertTrue(ship.isAlive());

        ship.hit();
        assertFalse(ship.isAlive());
    }
}