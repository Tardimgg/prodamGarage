package com.company.prodamgarage.models;


import javax.annotation.Nullable;
import java.io.IOException;

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
    User user;

    {
        try {
            user = User.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // логика игры(создание событий, изменение состояний персонажа, сохранение изменений)


}
