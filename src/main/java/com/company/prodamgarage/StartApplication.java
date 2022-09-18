package com.company.prodamgarage;

import com.company.prodamgarage.models.*;
import com.company.prodamgarage.models.eventModels.EventsRepository;
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

    public static void main(String[] args) {

        DialogFactory eventFactory = new JavaFXDialogFactory();
        new Game(eventFactory);


        launch();
    }
}