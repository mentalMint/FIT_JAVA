package ru.nsu.fit.oop.tetris.View;

import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class ButtonCreator extends Button {
    public static int width = 480;
    public static int buttonWidth = 200;
    public static int buttonHeight = 70;

    public ButtonCreator(int width) {
        this.width = width;
    }

    public static Button createButton(String label, int y) {
        Button button = new Button(label);
        button.setTranslateX((float) width / 2 - (float) buttonWidth / 2);
        button.setTranslateY(y);
        button.setPrefSize(buttonWidth, buttonHeight);
        button.setStyle("-fx-background-color: #0aafa0");
        button.setFont(Font.font("helvetica", FontWeight.BLACK, FontPosture.REGULAR, 20));
        return button;
    }
}
