package com.company.prodamgarage.models;

import javax.annotation.Nullable;
import java.io.IOException;

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
    User user;

    {
        try {
            user = User.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // логика игры(создание событий, изменение состояний персонажа, сохранение изменений)

//    public Dialog getNext() {} // Получение следующего события


}
