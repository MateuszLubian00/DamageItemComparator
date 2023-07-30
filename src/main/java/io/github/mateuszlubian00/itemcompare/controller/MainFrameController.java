package io.github.mateuszlubian00.itemcompare.controller;

import io.github.mateuszlubian00.itemcompare.ComparatorApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainFrameController {

    /* Dirty hack, initial button pressed (Player) has this tag.
    * Later on this value will change, but the button will retain its tag.
    */
    @FXML
    protected Button pressed;

    @FXML
    protected void switchToPlayerStats(ActionEvent event) throws IOException {
        ComparatorApplication.changeSubScene("player-stats.fxml", ((Node) event.getSource()).getScene());
        updatePressed((Button) event.getSource());
    }

    @FXML
    protected void switchToItems(ActionEvent event) throws IOException {
        ComparatorApplication.changeSubScene("items.fxml", ((Node) event.getSource()).getScene());
        updatePressed((Button) event.getSource());
    }

    @FXML
    protected void switchToEnemies(ActionEvent event) throws IOException {
        ComparatorApplication.changeSubScene("enemies.fxml", ((Node) event.getSource()).getScene());
        updatePressed((Button) event.getSource());
    }

    @FXML
    protected void switchToGraphs(ActionEvent event) throws IOException {
        ComparatorApplication.changeSubScene("graphs.fxml", ((Node) event.getSource()).getScene());
        updatePressed((Button) event.getSource());
    }

    /** Helper method to add and remove class "pressed" on menu buttons. */
    protected void updatePressed(Button button) {
        pressed.getStyleClass().remove("pressed");
        pressed = button;
        pressed.getStyleClass().add("pressed");
    }
}
