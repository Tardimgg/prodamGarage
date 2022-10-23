package com.company.prodamgarage.models.dialog.dialogBuilders;

import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;

public interface DialogBuilder {

    DialogBuilder setTitle(String title);

    Dialog build();
}
