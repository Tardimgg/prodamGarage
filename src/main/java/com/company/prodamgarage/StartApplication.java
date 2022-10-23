package com.company.prodamgarage;

import com.company.prodamgarage.controllers.RootController;
import com.company.prodamgarage.models.*;
import com.company.prodamgarage.models.dialog.factory.ConsoleDialogFactory;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.dialog.factory.JavaFXDialogFactory;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {


    @Override
    public void start(Stage stage) {

        stage.setTitle("Hello!");
        Resources.getParent(SceneType.ROOT)
                .subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new BiConsumerSingleObserver<>((parentPair, throwable) -> {

                    Scene scene = new Scene(parentPair.key);
//                    scene.getStylesheets().add("/styles/style.css");
                    ((RootController) parentPair.getValue()).setView(SceneType.MENU);

                    stage.setScene(scene);
                    stage.show();
                }));
    }

    enum Mode {
        CONSOLE,
        GUI
    }

    private final static Mode mode = Mode.GUI;

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
                    Game.getInstance().getNext().blockingIterable().forEach((v) -> {
                        try {
                            v.show();
                        } catch (GameOver e) {
                            e.printStackTrace();
                            System.exit(0);
                        }
                    });
                }
            }
        }
    }
}