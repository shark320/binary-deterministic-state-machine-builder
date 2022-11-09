package com.vpavlov.controller;

import java.io.IOException;

import com.vpavlov.App;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}