module cz.cvut.fel.pjv.battleship.battleshipfx {
  opens cz.cvut.fel.pjv.battleship.battleshipfx to com.fasterxml.jackson.databind;
  requires javafx.controls;
  requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.logging;


//    opens cz.cvut.fel.pjv.battleship.battleshipfx to javafx.fxml;
  exports cz.cvut.fel.pjv.battleship.battleshipfx;
}
