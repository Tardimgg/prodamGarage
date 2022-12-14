package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.dialog.dialogBuilders.DialogBuilder;
import com.company.prodamgarage.models.dialog.dialogBuilders.NotificationDialogBuilder;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.user.DefaultUserChanges;
import com.company.prodamgarage.models.user.UserChanges;

public class NotificationEvent extends Event{
    public String title;
    public String mainText;
    public UserChanges userChanges = new DefaultUserChanges();
    @Override
    public DialogBuilder dialogBuilder(){
        return new NotificationDialogBuilder(dialogFactory)
                .setTitle(title)
                .setMainText(mainText)
                .setChanges(userChanges);
    }

    public NotificationEvent(DialogFactory dialogFactory) {
        super(dialogFactory);
    }
}
