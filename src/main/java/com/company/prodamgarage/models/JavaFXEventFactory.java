package com.company.prodamgarage.models;

public class JavaFXEventFactory implements EventFactory {

    @Override
    public Dialog createDialog() {
        return new JavaFXDialog();
    }

}
