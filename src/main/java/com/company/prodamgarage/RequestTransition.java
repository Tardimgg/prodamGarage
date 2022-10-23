package com.company.prodamgarage;

import com.company.prodamgarage.SceneType;
import io.reactivex.Observer;

public interface RequestTransition {

    void subscribe(Observer<SceneType> obs);

}
