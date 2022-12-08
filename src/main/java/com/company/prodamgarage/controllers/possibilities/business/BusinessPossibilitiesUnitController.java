package com.company.prodamgarage.controllers.possibilities.business;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.RequiringTransition;
import com.company.prodamgarage.SceneType;
import com.company.prodamgarage.customView.TableOfChanges;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import io.reactivex.Observer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
public class BusinessPossibilitiesUnitController implements RequiringTransition {

    @FXML
    public TableOfChanges userChangesView;
    public ScrollPane scrollView;

    private PublishSubject<Pair<SceneType, Object>> reqTransition;

    @Override
    public void subscribe(Observer<Pair<SceneType, Object>> obs) {
        reqTransition.subscribe(obs);
    }

    @FXML
    TextArea main_text;

    @FXML
    Tooltip tooltip1;

    @FXML
    Tooltip tooltip2;

    @FXML
    Tooltip tooltip3;

//    Tooltip tooltip = new Tooltip("A tooltip");
//    tooltip.setShowDelay(Duration.seconds(3));
    Possibility possibility;

    public void setDate(Possibility possibility) {
        this.possibility = possibility;
        main_text.setText(possibility.description);

        userChangesView.setData(possibility.userChanges);
    }

    @FXML
    public void initialize() {
        reqTransition = PublishSubject.create();
        tooltip1.setShowDelay(Duration.seconds(0.1));
        tooltip2.setShowDelay(Duration.seconds(0.1));
        tooltip3.setShowDelay(Duration.seconds(0.1));
        tooltip1.setStyle("-fx-font-size: 20");
        tooltip2.setStyle("-fx-font-size: 20");
        tooltip3.setStyle("-fx-font-size: 20");


        userChangesView.prefWidthProperty().bind(main_text.widthProperty());
        userChangesView.minWidthProperty().bind(main_text.widthProperty());
        userChangesView.maxWidthProperty().bind(main_text.widthProperty());

        main_text.prefWidthProperty().bind(scrollView.widthProperty().multiply(0.96));
        main_text.minWidthProperty().bind(scrollView.widthProperty().multiply(0.96));
        main_text.maxWidthProperty().bind(scrollView.widthProperty().multiply(0.96));
        main_text.prefHeightProperty().bind(scrollView.heightProperty().subtract(100));
    }


    public void apply(ActionEvent actionEvent) {
        possibility.userChanges.apply().subscribeOn(Schedulers.computation()).subscribe();
        back(actionEvent);
    }

    public void back(ActionEvent actionEvent) {
        reqTransition.onNext(Pair.create(SceneType.BACK, null));
        reqTransition.onComplete();
    }
}
