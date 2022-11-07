package com.company.prodamgarage;

import com.company.prodamgarage.controllers.RootController;
import com.company.prodamgarage.models.*;
import com.company.prodamgarage.models.dialog.factory.ConsoleDialogFactory;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.dialog.factory.JavaFXDialogFactory;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartApplication extends Application {


    @Override
    public void start(Stage stage) {

        stage.setTitle("Hello!");
        Resources.getParent(SceneType.ROOT)
                .subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new BiConsumerSingleObserver<>((parentPair, throwable) -> {

                    Scene scene = new Scene(parentPair.key);
                    scene.getStylesheets().addAll(this.getClass().getResource("menu.css").toExternalForm());
                    ((RootController) parentPair.getValue()).setView(Pair.create(SceneType.MENU, null));
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
                            v.create();
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
