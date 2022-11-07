package com.company.prodamgarage;

import com.company.prodamgarage.SceneType;
import io.reactivex.Observer;

public interface RequiringTransition {

    void subscribe(Observer<Pair<SceneType, Object>> obs);

}
