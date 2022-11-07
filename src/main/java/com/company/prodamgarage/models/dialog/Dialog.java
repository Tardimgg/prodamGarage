package com.company.prodamgarage.models.dialog;

import com.company.prodamgarage.models.GameOver;

public interface Dialog {

    Object create() throws GameOver;

}
