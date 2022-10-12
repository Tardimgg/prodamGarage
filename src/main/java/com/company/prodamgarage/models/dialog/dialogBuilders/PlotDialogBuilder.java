package com.company.prodamgarage.models.dialog.dialogBuilders;

import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;

public class PlotDialogBuilder implements DialogBuilder{
    private DialogFactory factory;

    public PlotDialogBuilder(DialogFactory factory) {
        this.factory = factory;
    }

    @Override
    public Dialog build() {
        return null;
    }
}
