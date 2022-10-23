package com.company.prodamgarage.models.dialog.dialogBuilders;

import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.user.UserChanges;

public class NotificationDialogBuilder implements DialogBuilder {

    private DialogFactory factory;

    public NotificationDialogBuilder(DialogFactory factory) {
        this.factory = factory;
    }

    private String title;
    private String mainText;
    private UserChanges changes;

    @Override
    public NotificationDialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public NotificationDialogBuilder setMainText(String mainText) {
        this.mainText = mainText;
        return this;
    }

    public NotificationDialogBuilder setChanges(UserChanges changes) {
        this.changes = changes;
        return this;
    }

    @Override
    public Dialog build() {
        return factory.createNotificationDialog(title, mainText, changes);
    }
}
