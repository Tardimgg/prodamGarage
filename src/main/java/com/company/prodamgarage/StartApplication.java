package com.company.prodamgarage;

import com.company.prodamgarage.models.*;
import com.company.prodamgarage.models.dialog.factory.ConsoleDialogFactory;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.dialog.factory.JavaFXDialogFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    enum Mode {
        CONSOLE,
        GUI
    }

    private final static Mode mode = Mode.CONSOLE;

    public static void main(String[] args) {
        DialogFactory dialogFactory;

        dialogFactory = switch (mode) {
            case CONSOLE -> new ConsoleDialogFactory();
            case GUI -> new JavaFXDialogFactory();
        };
        new Game(dialogFactory);

        switch (mode) {
            case GUI -> launch();
            case CONSOLE -> {
                while (true) {
                    try {
                        Game.getInstance().getNext().blockingGet().show();
                    } catch (GameOver e) {
                        break;
                    }
                }
            }
        }
    }
}