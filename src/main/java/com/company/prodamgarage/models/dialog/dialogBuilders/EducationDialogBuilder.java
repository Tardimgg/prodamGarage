package com.company.prodamgarage.models.dialog.dialogBuilders;

import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.possibilityModels.Possibility;

import java.util.List;

public class EducationDialogBuilder implements DialogBuilder {

    private DialogFactory factory;

    public EducationDialogBuilder(DialogFactory factory) {
        this.factory = factory;
    }

    @Override
    public DialogBuilder setTitle(String title) {
        return this;
    }

    @Override
    public Dialog build() {
        return null;
//        return factory.createEducationDialog();
    }

}
