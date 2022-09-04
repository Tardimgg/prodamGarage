module com.company.prodamgarage {
    requires javafx.controls;
    requires javafx.fxml;
    requires jsr305;


    opens com.company.prodamgarage to javafx.fxml;
    exports com.company.prodamgarage;
    exports com.company.prodamgarage.controllers;
    opens com.company.prodamgarage.controllers to javafx.fxml;
    exports com.company.prodamgarage.models;
    opens com.company.prodamgarage.models to javafx.fxml;
}