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

    private static Map<SceneType, String> paths = new HashMap<>();
    static {
       paths.put(SceneType.ROOT, "root-view.fxml");
       paths.put(SceneType.SPLASH, "splash.fxml");
       paths.put(SceneType.MENU, "menu.fxml");
       paths.put(SceneType.GAME, "game.fxml");
       paths.put(SceneType.MESSAGE, "message.fxml");
       paths.put(SceneType.NOTIFICATION, "notification.fxml");
       paths.put(SceneType.POSSIBILITIES, "possibilities.fxml");
       paths.put(SceneType.POSSIBILITIES_MENU, "possibilities_menu.fxml");
       paths.put(SceneType.BUSINESS_POSSIBILITIES, "business_possibilities.fxml");
       paths.put(SceneType.BUSINESS_POSSIBILITIES_UNIT, "business_possibilities_unit.fxml");
    }

    public static Pair<Parent, ?> create(SceneType sceneType) throws IOException {
        if (!paths.containsKey(sceneType)) {
            return null;
        }
        FXMLLoader loader = new FXMLLoader(Resources.class.getResource(paths.get(sceneType)));
        Parent parent = loader.load();
        Object controller = loader.getController();
        return Pair.create(parent, controller);
    }


    private static void init() throws IOException {
        List<Pair<SceneType, String>> views = Arrays.asList(
                Pair.create(SceneType.ROOT, "root-view.fxml"),
                Pair.create(SceneType.SPLASH, "splash.fxml"),
                Pair.create(SceneType.MENU, "menu.fxml"),
                Pair.create(SceneType.GAME, "game.fxml"),
                Pair.create(SceneType.MESSAGE, "message.fxml"),
                Pair.create(SceneType.NOTIFICATION, "notification.fxml"),
                Pair.create(SceneType.POSSIBILITIES, "possibilities.fxml"),
                Pair.create(SceneType.POSSIBILITIES_MENU, "possibilities_menu.fxml"),
                Pair.create(SceneType.BUSINESS_POSSIBILITIES, "business_possibilities.fxml"),
                Pair.create(SceneType.BUSINESS_POSSIBILITIES_UNIT, "business_possibilities_unit.fxml")
        );

        for (var val : views) {
//            FXMLLoader loader = new FXMLLoader(Resources.class.getResource(val.value));
//            Parent parent = loader.load();
//            Object controller = loader.getController();
//            parents.put(val.key, Pair.create(parent, controller));
            parents.put(val.key, create(val.key));
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
