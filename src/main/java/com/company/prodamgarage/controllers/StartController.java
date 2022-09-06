package com.company.prodamgarage.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StartController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("nikita gay");
    }
}