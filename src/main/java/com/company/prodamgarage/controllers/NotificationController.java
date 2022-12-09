package com.company.prodamgarage.controllers;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.RequiringTransition;
import com.company.prodamgarage.SceneType;
import com.company.prodamgarage.customView.TableOfChanges;
import com.company.prodamgarage.models.user.UserChanges;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.lang.reflect.Field;

public class NotificationController implements RequiringTransition {


    public TableOfChanges userChangesView;
    public ScrollPane scrollView;
    //    private PublishSubject<SceneType> reqTransition = PublishSubject.create();
    private PublishSubject<Pair<SceneType, Object>> reqTransition;

    @Override
    public void subscribe(Observer<Pair<SceneType, Object>> obs) {
        reqTransition.subscribe(obs);
    }

    @FXML
    public Label title;

    @FXML
    public TextArea main_text;

    @FXML
    Tooltip tooltip1;

    @FXML
    Tooltip tooltip2;

    @FXML
    Tooltip tooltip3;

    private String titleSource;
    private String mainText;
    private UserChanges changes;

    public String getTitle() {
        return titleSource;
    }

    public void setTitle(String title) {
        this.titleSource = title;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public UserChanges getChanges() {
        return changes;
    }

    public void setChanges(UserChanges changes) {
        this.changes = changes;
    }

    @FXML
    public void initialize() {
        main_text.setText(mainText);
        title.setText(titleSource);
        reqTransition = PublishSubject.create();

//        tooltip1.setShowDelay(Duration.seconds(0.1));
//        tooltip2.setShowDelay(Duration.seconds(0.1));
        tooltip3.setShowDelay(Duration.seconds(0.1));
//        tooltip1.setStyle("-fx-font-size: 20");
//        tooltip2.setStyle("-fx-font-size: 20");
        tooltip3.setStyle("-fx-font-size: 20");

        userChangesView.setData(changes);

        userChangesView.prefWidthProperty().bind(main_text.widthProperty());
        userChangesView.minWidthProperty().bind(main_text.widthProperty());
        userChangesView.maxWidthProperty().bind(main_text.widthProperty());

        main_text.prefWidthProperty().bind(scrollView.widthProperty().multiply(0.96));
        main_text.minWidthProperty().bind(scrollView.widthProperty().multiply(0.96));
        main_text.maxWidthProperty().bind(scrollView.widthProperty().multiply(0.96));
        main_text.prefHeightProperty().bind(scrollView.heightProperty().subtract(100));
    }


    public void back(ActionEvent actionEvent) {
        changes.apply()
                .subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        reqTransition.onNext(Pair.create(SceneType.BACK, null));
                        reqTransition.onComplete();
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
    }
}
