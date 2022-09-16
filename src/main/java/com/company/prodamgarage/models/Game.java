package com.company.prodamgarage.models;

import com.company.prodamgarage.models.eventModels.BadEvent;
import com.company.prodamgarage.models.eventModels.EventsRepository;
import com.company.prodamgarage.models.eventModels.GoodEvent;

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

        User user;

        try {
            user = User.getInstance();

            EventReader reader = new EventReader();
            EventsRepository rep = reader.getEventsRepository(new JavaFXDialogFactory());

            for (GoodEvent goodEvent : rep.getGoodEventList()) {
                System.out.println(goodEvent.name + " " + goodEvent.moneyBonus);
            }

        } catch (IOException e) {
            System.out.println("Error " + e.toString());
        }
    }


    // логика игры(создание событий, изменение состояний персонажа, сохранение изменений)

//    public Dialog getNext() {} // Получение следующего события


}
