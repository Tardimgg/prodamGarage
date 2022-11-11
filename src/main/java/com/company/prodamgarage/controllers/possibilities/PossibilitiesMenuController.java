package com.company.prodamgarage.controllers.possibilities;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.RequiringTransition;
import com.company.prodamgarage.SceneType;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import com.company.prodamgarage.models.possibilityModels.PossibilityType;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class PossibilitiesMenuController implements RequiringTransition {

    private PublishSubject<Pair<SceneType, Object>> reqTransition;

    @Override
    public void subscribe(Observer<Pair<SceneType, Object>> obs) {
        reqTransition.subscribe(obs);
    }

    @FXML
    public Label title;

    @FXML
    public GridPane rootMenu;

    @FXML
    public void initialize() {
        reqTransition = PublishSubject.create();
    }

    public void initView() {
        reqTransition = PublishSubject.create();
        int size = 80;

        for (int i = 0; i < possibilities.size(); ++i) {
            Pair<PossibilityType, List<Possibility>> currentPossibility = possibilities.get(i);

            Button btn = new Button();
            btn.setUserData(Pair.create(getSceneType(currentPossibility.getKey()), currentPossibility.getValue()));

            btn.setOnMouseClicked(mouseEvent -> {
                reqTransition.onNext((Pair<SceneType, Object>) btn.getUserData());
                reqTransition.onComplete();
            });

            ImageView imageView = new ImageView();

            InputStream url = getClass().getResourceAsStream("/com/company/prodamgarage/images/settings.png");

            imageView.setImage(new Image(url));
            imageView.setFitHeight(size);
            imageView.setFitWidth(size);

            btn.setGraphic(imageView);

            Label label = new Label();
            label.setText(currentPossibility.key.name());

            VBox box = new VBox(btn, label);
            box.setAlignment(Pos.CENTER);

            rootMenu.add(box, i, 0);
        }
    }


    private SceneType getSceneType(PossibilityType possibilityType) {
        return switch (possibilityType) {
            case APARTMENT -> SceneType.BUSINESS_POSSIBILITIES;
            case BUSINESS -> SceneType.BUSINESS_POSSIBILITIES;
            case EDUCATION -> SceneType.EDUCATION_POSSIBILITIES;
        };
    }



    private String titleSource;
    List<Pair<PossibilityType, List<Possibility>>> possibilities;

    public void setTitle(String title) {
        this.titleSource = title;
    }

    public String getTitle() {
        return titleSource;
    }

    public void setPossibilities(List<Pair<PossibilityType, List<Possibility>>> possibilities) {
        this.possibilities = possibilities;
    }

    public List<Pair<PossibilityType, List<Possibility>>> getPossibilities() {
        return possibilities;
    }
}
