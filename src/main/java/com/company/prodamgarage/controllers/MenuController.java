package com.company.prodamgarage.controllers;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.RequiringTransition;
import com.company.prodamgarage.SceneType;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MenuController implements RequiringTransition {

    private final PublishSubject<Pair<SceneType, Object>> reqTransition = PublishSubject.create();

    @Override
    public void subscribe(Observer<Pair<SceneType, Object>> obs) {
        reqTransition.subscribe(obs);

    }

//    @FXML
//    private Label game_name;

    @FXML
    private Button settings_btn;

    @FXML
    protected void showGame(ActionEvent e) {
        reqTransition.onNext(Pair.create(SceneType.GAME, null));
        reqTransition.onComplete();
        /*
        Resources.getParent(SceneType.GAME).subscribe(new BiConsumerSingleObserver<>((parentPair, throwable) -> {
            //            home_page_parent = FXMLLoader.load(getClass().getResource("game.fxml"));
            Stage app_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
//                app_stage.
//                app_stage.hide(); //optional

            Scene scene = new Scene(parentPair.key);
            app_stage.setScene(scene);
            app_stage.show();
        }));

         */


    }

    @FXML
    public void initialize() {
        VBox hbox = new VBox();
        Button button1 = new Button("Add");
        Button button2 = new Button("Remove");
        VBox.setVgrow(button1, Priority.ALWAYS);
        VBox.setVgrow(button2, Priority.ALWAYS);
        button1.setMaxWidth(Double.MAX_VALUE);
        button2.setMaxWidth(Double.MAX_VALUE);
        hbox.getChildren().addAll(button1, button2);
//        game_name.center
//        URL url = getClass().getResource("resources/images/settings.png");
//
//        if (url != null) {
//            ImageView imageView = new ImageView(url.toExternalForm());
//            settings_btn.setGraphic(imageView);
//            System.out.println("second");
//        } else {
//            System.out.println("image == null");
//        }
    }
}