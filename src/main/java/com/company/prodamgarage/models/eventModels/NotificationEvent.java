package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.DialogBuilder;
import com.company.prodamgarage.models.DialogFactory;
import com.company.prodamgarage.models.Event;
import com.company.prodamgarage.models.UserChanges;
import com.company.prodamgarage.models.dialogBuilders.NotificationDialogBuilder;

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
