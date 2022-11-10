package com.company.prodamgarage.models.dialog.javaFXDialogs;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.GameOver;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.user.UserChanges;

import java.util.List;

public class SelectionJavaFXDialog implements Dialog {

    String title;
    String mainText;
    List<Pair<String, UserChanges>> changes;


    public SelectionJavaFXDialog(String title, String mainText, List<Pair<String, UserChanges>> changes) {
        this.title = title;
        this.mainText = mainText;
        this.changes = changes;
    }

    @Override
    public Object create() throws GameOver {
        return null;
    }
}
