package com.company.prodamgarage.models.dialog.dialogBuilders;

import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;

public class NotificationDialogBuilder implements DialogBuilder {

    private DialogFactory factory;

    public NotificationDialogBuilder(DialogFactory factory) {
        this.factory = factory;
    }

    @Override
    public Dialog build() {
        return null;
    }
}
