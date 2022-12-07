package com.company.prodamgarage.models.dialog;

import com.company.prodamgarage.models.GameOver;

public interface Dialog {

    int importance = 0;

    Object create() throws GameOver;

}
