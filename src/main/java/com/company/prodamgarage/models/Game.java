package com.company.prodamgarage.models;

import com.company.prodamgarage.DefaultObserver;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.eventModels.Event;
import com.company.prodamgarage.models.eventModels.PossibilitiesEvent;
import com.company.prodamgarage.models.loaders.EventReader;
import com.company.prodamgarage.models.loaders.MapReader;
import com.company.prodamgarage.models.loaders.PlotLoader;
import com.company.prodamgarage.models.loaders.PossibilitiesLoader;
import com.company.prodamgarage.models.mapModels.MapElement;
import com.company.prodamgarage.models.mapModels.MapRepository;
import com.company.prodamgarage.models.user.User;
import com.company.prodamgarage.models.user.UserChanges;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

import javax.annotation.Nullable;
import java.util.List;

public class Game {

    private static volatile Game instance;

    @Nullable
    public static Game getInstance() {
        return instance;
    }

    private final DialogFactory dialogFactory;

    private volatile MapRepository map = null;
    private final PublishSubject<Object> mapReadiness = PublishSubject.create();


    private User user;

    public Game(DialogFactory eventFactory) {
        this.dialogFactory = eventFactory;
        instance = this;

        MapReader.getMapRepository()
                .subscribeOn(Schedulers.computation())
                .subscribe(new BiConsumerSingleObserver<>((mapRepository, throwable) -> {
                    try {
                        user = User.getInstance().blockingGet();
                    } catch (RuntimeException e) {
                        mapReadiness.onError(e);
                        return;
                    }
                    map = mapRepository;
                    mapReadiness.onComplete();
                }));
    }

    // логика игры(создание событий, изменение состояний персонажа, сохранение изменений)

    private void getNextRealization(FlowableEmitter<Dialog> flowableEmitter) {
        Event event;
        MapElement mapElement = map.getMapList().get(user.getCurrentTime() % map.getMapList().size());

        int count = 0;
        int MAX_COUNT = 10;
        do {
            if (count >= MAX_COUNT) {
                user.increaseCurrentTime();
                flowableEmitter.onComplete();
                return;
            }
            count += 1;

            event = switch (mapElement.eventType) {
                case GOOD -> EventReader.getEventsRepository(dialogFactory).blockingGet().getRandomGoodEvent();
                case BAD -> EventReader.getEventsRepository(dialogFactory).blockingGet().getRandomBadEvent();
                case PLOT -> {
                    int pos = user.getCurrentPlotTime();
                    user.increaseCurrentPlotTime();
                    var repository = PlotLoader.getPlotRepository(dialogFactory).blockingGet();
                    if (pos >= repository.getPlotEvents().size()) {
                        yield null;
                    }
                    yield repository.getPlotEvents().get(pos);
                }
//                case BUY_CHOICE -> new PossibilitiesEvent(dialogFactory);
//                case EDUCATION_CHOICE -> new PossibilitiesEvent(dialogFactory); // TEMP CODE !!!!!!!!!!
                case POSSIBILITIES -> new PossibilitiesEvent(dialogFactory);
            };
        } while (event == null || !event.conditions.check(user, mapElement.seasonType).blockingGet());

        if (event.deferredEvents != null) {
            user.addDeferredEvents(event.deferredEvents);
        }

        if (!event.isFullyLoaded()) {
            Throwable res = event.load().blockingGet();
            if (res != null) {
                flowableEmitter.onError(res);
            }
        }

        flowableEmitter.onNext(event.dialogBuilder().build());

        for (Event deferredEvent : user.getDeferredEvents((v) -> v.conditions.check(user, mapElement.seasonType).blockingGet())) {
            if (deferredEvent.deferredEvents != null) {
                user.addDeferredEvents(deferredEvent.deferredEvents);
            }
            flowableEmitter.onNext(deferredEvent.dialogBuilder().build());
        }

//        flowableEmitter.onNext(event.dialogBuilder().setTitle("ЫЫЫЫЫЫЫЫЫЫЫЫЫЫЫЫЫЫ").build());
        user.increaseCurrentTime();
        flowableEmitter.onComplete();
    }

    public Flowable<Dialog> getNext() {

        return Flowable.create(flowableEmitter -> {
            synchronized (Game.this) {
                mapReadiness.subscribe(new DefaultObserver<>() {
                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        flowableEmitter.onComplete();
                    }

                    @Override
                    public void onComplete() {
                        try {
                            getNextRealization(flowableEmitter);
                        } catch (Exception e) {
                            e.printStackTrace();
                            flowableEmitter.onComplete();
                        }
                    }
                });
            }
        }, BackpressureStrategy.BUFFER);

    } // Получение следующего события
}
