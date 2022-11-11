package com.company.prodamgarage.controllers.game;

import com.company.prodamgarage.DefaultObserver;
import com.company.prodamgarage.Resources;
import com.company.prodamgarage.models.user.User;
import com.company.prodamgarage.observable.SubscribeBuilder;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class PlayerInfoController {

    public StackPane rootPlayerInfo;

    public ScrollPane mainInfo;
    public Button mainInfoBtn;
    public Rectangle mainInfoRec;

    public Rectangle assetInfoRec;
    public Button assetInfoBtn;
    public VBox assetInfo;

    public Label money;
    public Label moneyFlow;
    public Label expenses;
    public Label assets;
    public Label passive;
    public Label freeTime;
    public Label costPerHour;
    public Label mood;
    public Label time;



    private <T> void bind(Label label, SubscribeBuilder<T> subscribeBuilder) {
        bind(subscribeBuilder, new DefaultObserver<>() {
            @Override
            public void onNext(T o) {
                label.setText(o.toString());
            }
        });
    }

    private <T> void bind(SubscribeBuilder<T> subscribeBuilder, DefaultObserver<T> obs) {
        subscribeBuilder.subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(obs);
    }

    @FXML
    public void initialize() {
        gotoAssetInfo(null);
        gotoMainInfo(null);

        rootPlayerInfo.getStylesheets().add(Resources.class.getResource("player_info.css").toExternalForm());

        User.getInstance()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(new BiConsumerSingleObserver<>((user, throwable) -> {
                    if (user != null && throwable == null) {

                        bind(money, user.subscribeAge());
                        bind(moneyFlow, user.subscribeMoneyFlow());
                        bind(expenses, user.subscribeExpenses());
                        bind(freeTime, user.subscribeFreeTime());
                        bind(time, user.subscribeCurrentTime());

                        bind(user.subscribeMoneyFlow(), new DefaultObserver<>() {
                            @Override
                            public void onNext(Integer integer) {
                                costPerHour.setText(Float.toString(integer / 24.0f));
                            }
                        });

                        bind(user.subscribeFreeTime(), new DefaultObserver<>() {
                            @Override
                            public void onNext(Integer integer) {
                                mood.setText(integer > 10 ? "Норм" : "Неоч");
                            }
                        });

                    } else {
                        throwable.printStackTrace();
                    }
                }));
    }

    public void gotoMainInfo(ActionEvent actionEvent) {
        mainInfo.toFront();
        mainInfoRec.toFront();
        mainInfoBtn.toFront();
    }


    public void gotoAssetInfo(ActionEvent actionEvent) {
        assetInfo.toFront();
        assetInfoRec.toFront();
        assetInfoBtn.toFront();
    }
}
