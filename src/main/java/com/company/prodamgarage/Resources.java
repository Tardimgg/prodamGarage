package com.company.prodamgarage;

import io.reactivex.Single;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Resources {

    private static final HashMap<SceneType, Pair<Parent, ?>> parents = new HashMap<>();
    private volatile static boolean initialized = false;


    public static void init() throws IOException {
        List<Pair<SceneType, String>> views = Arrays.asList(
                Pair.create(SceneType.ROOT, "root-view.fxml"),
                Pair.create(SceneType.SPLASH, "splash.fxml"),
                Pair.create(SceneType.MENU, "menu.fxml"),
                Pair.create(SceneType.GAME, "game.fxml"),
                Pair.create(SceneType.MESSAGE, "message.fxml"),
                Pair.create(SceneType.NOTIFICATION, "notification.fxml")
        );

        for (var val: views) {
            FXMLLoader loader = new FXMLLoader(Resources.class.getResource(val.value));
            Parent parent = loader.load();
            Object controller = loader.getController();

            parents.put(val.key, Pair.create(parent, controller));

        }
        initialized = true;
    }

    public static Single<Pair<Parent, ?>> getParent(SceneType sceneType) {
        return Single.create(singleEmitter -> {
            if (!initialized) {
                init();
            }
            if (parents.containsKey(sceneType)) {
                singleEmitter.onSuccess(parents.get(sceneType));
            } else {
                singleEmitter.onError(new ClassNotFoundException());
            }
        });
    }
}
