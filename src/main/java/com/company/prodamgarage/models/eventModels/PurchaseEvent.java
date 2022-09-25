package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.dialog.dialogBuilders.PurchaseDialogBuilder;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.dialog.dialogBuilders.DialogBuilder;

public class PurchaseEvent extends Event {

    public PurchaseEvent(DialogFactory dialogFactory) {
        super(dialogFactory);
    }

    @Override
    public void dialogBuilder() {

    }
}
