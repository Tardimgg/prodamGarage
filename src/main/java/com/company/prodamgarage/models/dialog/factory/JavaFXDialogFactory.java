package com.company.prodamgarage.models.dialog.factory;

import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.dialog.JavaFXDialog;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;

public class JavaFXDialogFactory implements DialogFactory {

    @Override
    public Dialog createDialog() {
        return new JavaFXDialog();
    }

}
