package io.github.mateuszlubian00.itemcompare.controller;

import io.github.mateuszlubian00.itemcompare.ComparatorApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HelpController {

    @FXML
    public void closeHelp(ActionEvent event) {
        ComparatorApplication.closeHelp();
    }
}
