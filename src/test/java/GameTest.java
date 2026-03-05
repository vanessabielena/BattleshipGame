//import cz.cvut.fel.pjv.battleship.battleshipfx.Game;
//import javafx.scene.Scene;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.MouseButton;
//import javafx.stage.Stage;
//import org.junit.jupiter.api.Test;
//import org.testfx.api.FxRobot;
//import org.testfx.api.FxToolkit;
//import org.testfx.framework.junit5.ApplicationTest;
//import org.testfx.util.WaitForAsyncUtils;
//
//import static jdk.internal.jshell.debug.InternalDebugControl.release;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class GameTest extends ApplicationTest {
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        // Create an instance of your Game class and call its start method
//        Game game = new Game();
//        game.start(primaryStage);
//    }
//
//    @Override
//    public void stop() throws Exception {
//        // Clean up JavaFX resources after the test is finished
//        FxToolkit.hideStage();
//        release(new KeyCode[] {});
//        release(new MouseButton[] {});
//        super.stop();
//    }
//
//    @Test
//    public void testGameFlow() {
//        // Wait for the UI to be initialized
//        WaitForAsyncUtils.waitForFxEvents();
//
//        // Simulate user interaction with the UI
//        clickOn("Play");
//
//        // Assert that the game scene is displayed
//        Scene scene = FxToolkit.setupScene();
//        assertTrue(scene.getRoot() instanceof BorderPane);
//
//        // Simulate user interaction with the game board
//        clickOn("#enemyBoard .square"); // Click on an enemy board square
//
//        // Assert the expected outcome based on the game logic
//        // You can use the Game instance and its internal state for assertions
//
//        // Example assertion:
//        // assertEquals(expectedResult, game.getGameState());
//    }
//}
import cz.cvut.fel.pjv.battleship.battleshipfx.Game;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.service.query.PointQuery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {

    private FxRobot robot;

    @BeforeEach
    public void setup() throws Exception {
        // Launch your JavaFX application
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Game.class);
        robot = new FxRobot();
    }

    @AfterEach
    public void cleanup() throws Exception {
        // Clean up JavaFX resources after the test is finished
        FxToolkit.cleanupStages();
    }

    @Test
    public void testGameFlow() {
        // Wait for the UI to be initialized
        robot.interact(() -> {
            // Simulate user interaction with the UI
            robot.clickOn("Play");
        });

        // Assert that the game scene is displayed
//        Scene scene = robot.window(robot.lookup(".border-pane").query().getScene().getWindow());
        Scene scene = robot.lookup(".border-pane").query().getScene();

        assertTrue(scene.getRoot() instanceof BorderPane);

        // Simulate user interaction with the game board
        robot.interact(() -> {
            // Simulate clicking on an enemy board square

            robot.clickOn((PointQuery) robot.lookup("#enemyBoard .square").query());
        });

        // Assert the expected outcome based on the game logic
        // You can use the Game instance and its internal state for assertions

        // Example assertion:
        // assertEquals(expectedResult, game.getGameState());
    }
}
