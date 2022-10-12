package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.dialog.dialogBuilders.DialogBuilder;
import com.company.prodamgarage.models.dialog.dialogBuilders.NotificationDialogBuilder;
import com.company.prodamgarage.models.dialog.dialogBuilders.PlotDialogBuilder;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.user.UserChanges;

public class PlotEvent extends Event{
    public String title;
    public String mainText;
    @Override
    public DialogBuilder dialogBuilder() {
        return new PlotDialogBuilder(this.dialogFactory);
    }

    public PlotEvent(DialogFactory dialogFactory) {
        super(dialogFactory);
    }
}
