package com.company.prodamgarage.models;

import com.company.prodamgarage.models.eventModels.BadEvent;
import com.company.prodamgarage.models.eventModels.EventsRepository;
import com.company.prodamgarage.models.eventModels.GoodEvent;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
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

                        for (GoodEvent goodEvent : eventsRepository.getGoodEventList()) {
                           System.out.println(goodEvent.name + " " + goodEvent.moneyBonus);
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
