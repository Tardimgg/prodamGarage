package com.company.prodamgarage.models;

import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.eventModels.Event;
import com.company.prodamgarage.models.loaders.EventReader;
import com.company.prodamgarage.models.user.User;
import io.reactivex.Single;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.schedulers.Schedulers;

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
            user = User.getInstance().blockingGet();

            EventReader.getEventsRepository(eventFactory)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new BiConsumerSingleObserver<>((eventsRepository, throwable) -> {

                        for (Event goodEvent : eventsRepository.getGoodEventList()) {
//                           System.out.println(goodEvent. + " " + goodEvent.moneyBonus);
                        }

                    }));

        } catch (IOException e) {
            System.out.println("Error. " + e);
        }
    }

    // логика игры(создание событий, изменение состояний персонажа, сохранение изменений)

    public Single<Dialog> getNext() {
        return Single.create(singleSubscriber -> {
            singleSubscriber.onSuccess(eventFactory.createDialog());
        });
    } // Получение следующего события
}
