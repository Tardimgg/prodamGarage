package com.company.prodamgarage.models;

import javax.annotation.Nullable;

public class Game {

    private static volatile Game instance;

    @Nullable
    public static Game getInstance() {
        return instance;
    }

    private final DialogFactory eventFactory;

    public Game(DialogFactory eventFactory) {
        this.eventFactory = eventFactory;
        instance = this;
    }

    // логика игры(создание событий, изменение состояний персонажа, сохранение изменений)

//    public Dialog getNext() {} // Получение следующего события


}
