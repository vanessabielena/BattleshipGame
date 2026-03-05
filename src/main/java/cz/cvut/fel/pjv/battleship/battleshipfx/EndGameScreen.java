package cz.cvut.fel.pjv.battleship.battleshipfx;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EndGameScreen extends Scene {
    private ImageView imageView;
    private ImageView imageViewSea;

    public EndGameScreen(boolean isWin, Stage primaryStage) { //Stage primaryStage
        super(new StackPane(), 1000, 650);
        Image image;
        if (isWin) {
            image = new Image("youwonimage3.png");
        } else {
            image = new Image("youlost.png");
        }

        imageView = new ImageView(image);
        Image imageSea = new Image("sea2.png");
        imageViewSea = new ImageView(imageSea);
//        imageView = new ImageView(image);
        StackPane root = (StackPane) getRoot();
//        root.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
        BackgroundImage backgroundImage = new BackgroundImage(imageViewSea.getImage(),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        root.setBackground(background);

        imageView.fitWidthProperty().bind(widthProperty());
        imageView.fitHeightProperty().bind(heightProperty());

        Button returnButton = new Button("Return to Main Menu");
        returnButton.setStyle(Game.getButtonCss(20, 10, 20));
        returnButton.setOnAction(event -> {
//            Scene gameScene = null;
            try {
//                gameScene = new Scene(new Game().mainMenu(primaryStage));
                Game game = new Game();
                game.mainMenu(primaryStage);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
//            primaryStage.setScene(gameScene);
        });

        StackPane.setAlignment(returnButton, Pos.BOTTOM_CENTER);

        root.getChildren().addAll(imageView, returnButton);
    }
}

