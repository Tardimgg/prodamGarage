package com.company.prodamgarage.models.user;

import io.reactivex.Completable;

public class DefaultUserChanges extends UserChanges {
    @Override
    public Completable apply() {
        System.out.printf("coci xyi");
        return Completable.complete();
    }
}
