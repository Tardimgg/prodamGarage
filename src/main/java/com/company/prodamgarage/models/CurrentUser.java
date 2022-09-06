package com.company.prodamgarage.models;

import javax.annotation.Nullable;

public class CurrentUser {

    private static volatile CurrentUser instance;

    @Nullable
    public static CurrentUser getInstance() {
        return instance;
    }

    private final User user;

    public CurrentUser(User user) {
        this.user = user;
        instance = this;
    }
}
