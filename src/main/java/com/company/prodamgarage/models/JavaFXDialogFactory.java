package com.company.prodamgarage.models;

public class JavaFXDialogFactory implements DialogFactory {

    @Override
    public Dialog createDialog() {
        return new JavaFXDialog();
    }

}
