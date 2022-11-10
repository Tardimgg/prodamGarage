package com.company.prodamgarage.controllers.possibilities.business;

import com.company.prodamgarage.*;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Pagination;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BusinessPossibilitiesController implements RequiringTransition, RequiringData {

    private PublishSubject<Pair<SceneType, Object>> reqTransition;

    @Override
    public void subscribe(Observer<Pair<SceneType, Object>> obs) {
        reqTransition.subscribe(obs);
    }

    @FXML
    private Pagination rootBusiness;

    List<Possibility> data;

    @Override
    public void setData(Object obj) {
        data = (List<Possibility>) obj;
        createdView = new ArrayList<>(Collections.nCopies(data.size(), null));
//        createdView = Collections.nCopies(data.size(), null);
//        rootBusiness.setCurrentPageIndex(0);
        rootBusiness.setPageCount(data.size());
        reqTransition = PublishSubject.create();


        rootBusiness.setPageFactory(index -> {
            if (createdView.get(index) != null) {
                return createdView.get(index).getKey();
            }
            Pair<Parent, ?> res = Resources.getNewParent(SceneType.BUSINESS_POSSIBILITIES_UNIT).blockingGet();

            if (res.getValue() instanceof RequiringTransition controller) {
                controller.subscribe(new DefaultObserver<>() {
                    @Override
                    public void onNext(Pair<SceneType, Object> sceneInfo) {
                        if (sceneInfo.key == SceneType.BACK) {
                            back(null);
                        }
                    }
                });
            } else {
                System.out.println("warning: " + res.getValue().getClass() +
                        " not implement RequestTransition");
            }

            ((BusinessPossibilitiesUnitController) res.value).setDate(data.get(index));
            createdView.set(index, res);
            return res.getKey();
        });

    }

    List<Pair<Parent, ?>> createdView;

    @FXML
    public void initialize() {
        reqTransition = PublishSubject.create();

    }

    public void back(ActionEvent actionEvent) {
        reqTransition.onNext(Pair.create(SceneType.BACK, null));
        reqTransition.onComplete();
    }
}
