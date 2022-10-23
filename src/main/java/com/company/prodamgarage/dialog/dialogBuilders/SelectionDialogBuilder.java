package com.company.prodamgarage.models.dialog.dialogBuilders;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.user.UserChanges;

import java.util.List;

public class SelectionDialogBuilder implements DialogBuilder {

    private DialogFactory factory;

    public SelectionDialogBuilder(DialogFactory factory) {
        this.factory = factory;
    }

    private String title;
    private String mainText;
    private List<Pair<String, UserChanges>> changes;

    @Override
    public SelectionDialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public SelectionDialogBuilder setMainText(String mainText) {
        this.mainText = mainText;
        return this;
    }

    public SelectionDialogBuilder setChanges(List<Pair<String, UserChanges>> changes) {
        this.changes = changes;
        return this;
    }

    @Override
    public Dialog build() {
        return factory.createSelectionDialog(title, mainText, changes);
    }
}
