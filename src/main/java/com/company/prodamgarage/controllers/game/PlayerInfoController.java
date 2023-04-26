package com.company.prodamgarage.controllers.game;

import com.company.prodamgarage.DefaultObserver;
import com.company.prodamgarage.Pair;
import com.company.prodamgarage.Resources;
import com.company.prodamgarage.models.user.PropertyType;
import com.company.prodamgarage.models.user.User;
import com.company.prodamgarage.models.user.UserChanges;
import com.company.prodamgarage.observable.SubscribeBuilder;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class PlayerInfoController {

    public StackPane rootPlayerInfo;

    public ScrollPane mainInfo;
    public Button mainInfoBtn;
    public Rectangle mainInfoRec;

    public Rectangle assetInfoRec;
    public Button assetInfoBtn;
    public ScrollPane assetInfo;

    public Label money;
    public Label moneyFlow;
    public Label expenses;
    public Label assets;
    public Label passive;
    public Label freeTime;
    public Label costPerHour;
    public Label mood;
    public Label time;
    public Label name;
    public Label name1;
    public VBox propertiesView;


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
                .observeOn(JavaFxScheduler.platform(), true)
                .subscribe(obs);
    }

    private void initWithNewUser(User user) {
        if (user != null) {

            bind(name, user.subscribeName());
            bind(name1, user.subscribeName());
            bind(money, user.subscribeCash());
            bind(moneyFlow, user.subscribeMoneyFlow());
            bind(expenses, user.subscribeExpenses());
            bind(assets, user.subscribeAssets());
            bind(passive, user.subscribePassive());
            bind(freeTime, user.subscribeFreeTime());
            bind(time, user.subscribeCurrentTime());

            bind(user.subscribeMoneyFlow(), new DefaultObserver<>() {
                @Override
                public void onNext(Integer integer) {
                    costPerHour.setText(Float.toString(integer / (30.0f * 24)));
                }
            });

            bind(user.subscribeFreeTime(), new DefaultObserver<>() {
                @Override
                public void onNext(Integer integer) {
                    mood.setText(integer > 200 ? "Норм" : "Неоч");
                }
            });

            bind(user.subscribeProperties(), new DefaultObserver<>() {
                @Override
                public void onNext(HashMap<PropertyType, List<Pair<String, UserChanges>>> propertyTypeListHashMap) {
//                                propertiesView.getChildren().filtered(node -> !Arrays.asList("name1", "panel2SmallDescription").contains(node.getId())).clear();
                    propertiesView.getChildren().removeIf(node -> !Arrays.asList("name1", "panel2SmallDescription").contains(node.getId()));

                    for (var key : propertyTypeListHashMap.keySet()) {
                        var res = propertyTypeListHashMap.get(key);

                        if (res != null) {
                            for (var val : res) {
                                BorderPane pane = new BorderPane();
                                VBox.setVgrow(pane, Priority.NEVER);
                                VBox.setMargin(pane, new Insets(0, 0, 0, 10));

                                Label propertyName = new Label();
                                propertyName.getStyleClass().add("property");
                                propertyName.setText(val.key);
                                BorderPane.setAlignment(propertyName, Pos.CENTER_LEFT);

                                Label propertyValue = new Label();
                                propertyValue.getStyleClass().add("property");
                                propertyValue.setText(Integer.toString(Math.abs(val.value.deltaCash)));
                                BorderPane.setAlignment(propertyValue, Pos.CENTER_RIGHT);
                                BorderPane.setMargin(propertyValue, new Insets(0, 10, 0, 0));

                                pane.setLeft(propertyName);
                                pane.setRight(propertyValue);

                                propertiesView.getChildren().add(pane);
                            }
                        }
                    }

                }
            });
        }
    }

    @FXML
    public void initialize() {
        gotoAssetInfo(null);
        gotoMainInfo(null);

        rootPlayerInfo.getStylesheets().add(Resources.class.getResource("player_info.css").toExternalForm());

        User.getInstances()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(new BiConsumerSingleObserver<>((userSubscribeBuilder, throwable) -> {
                    if (userSubscribeBuilder != null && throwable == null) {
                        userSubscribeBuilder
                                .subscribeOn(Schedulers.computation())
                                .observeOn(Schedulers.computation())
                                .subscribe(new DefaultObserver<>() {
                                    @Override
                                    public void onNext(User user) {
                                        initWithNewUser(user);
                                    }
                                });
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
