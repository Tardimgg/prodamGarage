package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.dialog.dialogBuilders.DialogBuilder;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.user.UserChanges;
import com.company.prodamgarage.models.dialog.dialogBuilders.NotificationDialogBuilder;

public class NotificationEvent extends Event {
    

    public String title;
    public String text;
    public UserChanges userChanges;


    public NotificationEvent(DialogFactory dialogFactory) {
        super(dialogFactory);
    }

    @Override
    public DialogBuilder dialogBuilder() {
        return new NotificationDialogBuilder();
    }
}
