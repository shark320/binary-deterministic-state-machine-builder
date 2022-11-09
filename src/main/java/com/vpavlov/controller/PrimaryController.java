package com.vpavlov.controller;

import java.io.IOException;

import com.vpavlov.App;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
