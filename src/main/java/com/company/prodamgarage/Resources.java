package com.company.prodamgarage;

import io.reactivex.Single;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Resources {

    private static final HashMap<SceneType, Pair<Parent, ?>> parents = new HashMap<>();
    private static final AtomicBoolean initialized = new AtomicBoolean(false);

    private static final Map<SceneType, String> paths = new HashMap<>();
    static {
       paths.put(SceneType.ROOT, "root-view.fxml");
       paths.put(SceneType.SPLASH, "splash.fxml");
       paths.put(SceneType.MENU, "menu.fxml");
       paths.put(SceneType.GAME, "game.fxml");
       paths.put(SceneType.MESSAGE, "message.fxml");
       paths.put(SceneType.NOTIFICATION, "notification.fxml");
       paths.put(SceneType.SELECTION, "selection.fxml");
//       paths.put(SceneType.PLAYER_INFO, "player_info.fxml");
       paths.put(SceneType.POSSIBILITIES, "possibilities.fxml");
       paths.put(SceneType.POSSIBILITIES_MENU, "possibilities_menu.fxml");
       paths.put(SceneType.BUSINESS_POSSIBILITIES, "business_possibilities.fxml");
       paths.put(SceneType.BUSINESS_POSSIBILITIES_UNIT, "business_possibilities_unit.fxml");
       paths.put(SceneType.EDUCATION_POSSIBILITIES, "education_possibilities.fxml");
    }

    public static Pair<Parent, ?> create(SceneType sceneType) throws IOException {
        if (!paths.containsKey(sceneType)) {
            return null;
        }
        FXMLLoader loader = new FXMLLoader(Resources.class.getResource(paths.get(sceneType)));
        Parent parent = loader.load();

        switch (sceneType) {
            case MENU -> {
                parent.getStylesheets().add(Resources.class.getResource("menu.css").toExternalForm());
            }
            case GAME -> {
                parent.getStylesheets().add(Resources.class.getResource("game.css").toExternalForm());
            }
//            case PLAYER_INFO -> {
//                parent.getStylesheets().add(Resources.class.getResource("player_info.css").toExternalForm());
//            }
            case BUSINESS_POSSIBILITIES_UNIT ->{
                parent.getStylesheets().add(Resources.class.getResource("business_possibilities_unit.css").toExternalForm());
            }
            case SELECTION ->{
                parent.getStylesheets().add(Resources.class.getResource("selection.css").toExternalForm());
            }
            case NOTIFICATION ->{
                parent.getStylesheets().add(Resources.class.getResource("notification.css").toExternalForm());
            }
            case EDUCATION_POSSIBILITIES ->{
                parent.getStylesheets().add(Resources.class.getResource("education_possibilities.css").toExternalForm());
            }
        }

        Object controller = loader.getController();
        return Pair.create(parent, controller);
    }


    private static void init() throws IOException {
        for (var val : paths.keySet()) {
//        for (var val : views) {
//            FXMLLoader loader = new FXMLLoader(Resources.class.getResource(val.value));
//            Parent parent = loader.load();
//            Object controller = loader.getController();
//            parents.put(val.key, Pair.create(parent, controller));
            parents.put(val, create(val));
//            parents.put(val.key, create(val.key));
        }
    }

    public static Single<Pair<Parent, ?>> getParent(SceneType sceneType) {
        return Single.create(singleEmitter -> {
            synchronized (initialized) {
                if (!initialized.get()) {
                    init();
                    initialized.set(true);
                }
            }
            if (parents.containsKey(sceneType)) {
                singleEmitter.onSuccess(parents.get(sceneType));
            } else {
                singleEmitter.onError(new ClassNotFoundException(sceneType.name()));
            }
        });
    }

    public static Single<Pair<Parent, ?>> getNewParent(SceneType sceneType) {
        return Single.create(singleEmitter -> {
            var res = create(sceneType);
            if (res != null) {
                singleEmitter.onSuccess(res);
            } else {
                singleEmitter.onError(new ClassNotFoundException(sceneType.name()));
            }
        });
    }
}
