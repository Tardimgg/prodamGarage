package com.company.prodamgarage.models;


import javax.annotation.Nullable;

public class Game {

    private static volatile Game instance;

    @Nullable
    public static Game getInstance() {
        return instance;
    }

    private final EventFactory eventFactory;

    public Game(EventFactory eventFactory) {
        this.eventFactory = eventFactory;
        instance = this;
    }

    // логика игры(создание событий, изменение состояний персонажа, сохранение изменений)


}
