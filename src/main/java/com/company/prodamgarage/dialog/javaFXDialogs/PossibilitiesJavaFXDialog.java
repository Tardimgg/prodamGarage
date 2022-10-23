package com.company.prodamgarage.models.dialog.javaFXDialogs;

import com.company.prodamgarage.models.GameOver;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.possibilityModels.Possibility;

import java.util.List;

public class PossibilitiesJavaFXDialog implements Dialog {

    List<Possibility> apartmentPossibilities;
    List<Possibility> businessPossibilities;
    String title;

    public PossibilitiesJavaFXDialog(String title, List<Possibility> apartmentPossibilities, List<Possibility> businessPossibilities) {
        this.title = title;
        this.apartmentPossibilities = apartmentPossibilities;
        this.businessPossibilities = businessPossibilities;
    }

    @Override
    public Object show() throws GameOver {
        return null;
    }
}
