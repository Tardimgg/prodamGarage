package com.company.prodamgarage.models;

import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.eventModels.Event;
import com.company.prodamgarage.models.loaders.EventReader;
import com.company.prodamgarage.models.user.User;
import com.company.prodamgarage.models.user.UserChanges;
import io.reactivex.Single;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.schedulers.Schedulers;

import javax.annotation.Nullable;

public class Game {

    private static volatile Game instance;

    @Nullable
    public static Game getInstance() {
        return instance;
    }

    private final DialogFactory dialogFactory;

    public Game(DialogFactory eventFactory) {
        this.dialogFactory = eventFactory;
        instance = this;

        User user;

        try {
            user = User.getInstance().blockingGet();

            EventReader.getEventsRepository(eventFactory)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new BiConsumerSingleObserver<>((eventsRepository, throwable) -> {

                        for (Event goodEvent : eventsRepository.getGoodEventList()) {
//                           System.out.println(goodEvent. + " " + goodEvent.moneyBonus);
                        }

                    }));

        } catch (RuntimeException e) {
            System.out.println("Error. " + e);
        }
    }

    // логика игры(создание событий, изменение состояний персонажа, сохранение изменений)

    public void getNext() {
        Event event = EventReader.getEventsRepository(dialogFactory).blockingGet().getRandomGoodEvent();
        event.dialogBuilder();
        ;
    } // Получение следующего события
}
