package com.company.prodamgarage.models.dialogBuilders;

import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;

public interface DialogBuilder {

    Dialog build(DialogFactory factory);
}
