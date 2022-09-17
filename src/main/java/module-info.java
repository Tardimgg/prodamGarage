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
}