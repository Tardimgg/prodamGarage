package com.company.prodamgarage.models.conditions;

import com.company.prodamgarage.models.user.User;
import io.reactivex.Single;

public class DefaultConditions extends Conditions {

    public Single<Boolean> check() {
        return Single.just(true);
    }

}
