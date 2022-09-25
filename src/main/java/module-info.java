module com.company.prodamgarage {
    requires javafx.controls;
    requires javafx.fxml;
    requires jsr305;
    requires rxjavafx;
    requires io.reactivex.rxjava2;
    requires com.google.gson;
    requires json.simple;


    opens com.company.prodamgarage to javafx.fxml;
    exports com.company.prodamgarage;
    exports com.company.prodamgarage.controllers;
    opens com.company.prodamgarage.controllers to javafx.fxml;
    exports com.company.prodamgarage.models;
    opens com.company.prodamgarage.models to javafx.fxml;

    exports com.company.prodamgarage.models.eventModels;
    opens com.company.prodamgarage.models.eventModels;
    exports com.company.prodamgarage.models.dialog.dialogBuilders;
    opens com.company.prodamgarage.models.dialog.dialogBuilders to javafx.fxml;
    exports com.company.prodamgarage.models.dialog;
    opens com.company.prodamgarage.models.dialog to javafx.fxml;
    exports com.company.prodamgarage.models.dialog.factory;
    opens com.company.prodamgarage.models.dialog.factory to javafx.fxml;
    exports com.company.prodamgarage.models.loaders;
    opens com.company.prodamgarage.models.loaders to javafx.fxml;
    exports com.company.prodamgarage.models.mapModels;
    opens com.company.prodamgarage.models.mapModels to javafx.fxml;
    exports com.company.prodamgarage.models.user;
    opens com.company.prodamgarage.models.user to javafx.fxml;
    exports com.company.prodamgarage.models.dialog.consoleDialogs;
    opens com.company.prodamgarage.models.dialog.consoleDialogs to javafx.fxml;
    exports com.company.prodamgarage.models.dialog.javaFXDialogs;
    opens com.company.prodamgarage.models.dialog.javaFXDialogs to javafx.fxml;
}