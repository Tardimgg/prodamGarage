package com.company.prodamgarage.models;

import com.company.prodamgarage.DefaultObserver;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.dialog.dialogBuilders.NotificationDialogBuilder;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.eventModels.Event;
import com.company.prodamgarage.models.eventModels.PossibilitiesEvent;
import com.company.prodamgarage.models.loaders.EventReader;
import com.company.prodamgarage.models.loaders.MapReader;
import com.company.prodamgarage.models.loaders.PlotLoader;
import com.company.prodamgarage.models.loaders.PossibilitiesLoader;
import com.company.prodamgarage.models.mapModels.MapElement;
import com.company.prodamgarage.models.mapModels.MapRepository;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import com.company.prodamgarage.models.user.DefaultUserChanges;
import com.company.prodamgarage.models.user.PropertyType;
import com.company.prodamgarage.models.user.User;
import com.company.prodamgarage.models.user.UserChanges;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import javafx.application.Platform;

import javax.annotation.Nullable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

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

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });
        executor.scheduleAtFixedRate(() -> {
            try {
                user.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.SECONDS);

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

    private void getNextRealization(FlowableEmitter<Dialog> flowableEmitter) throws IllegalAccessException {

        user.setCash(user.getCash() + user.getMoneyFlow() - user.getExpenses());

        if (user.getCash() >= 1000000) {
            Dialog dialog = new NotificationDialogBuilder(dialogFactory)
                    .setTitle("Конец игры")
                    .setMainText("Вы выиграли!")
                    .setChanges(new DefaultUserChanges())
                    .build();
            flowableEmitter.onNext(dialog);
            flowableEmitter.onError(new GameOver("win"));
            return;
        } else if (user.getCash() < 0 || user.getFreeTime() < 0) {
            Dialog dialog = new NotificationDialogBuilder(dialogFactory)
                    .setTitle("Конец игры")
                    .setMainText("Вы проиграли")
                    .setChanges(new DefaultUserChanges())
                    .build();
            flowableEmitter.onNext(dialog);
            flowableEmitter.onError(new GameOver("loses"));
            return;
        }



        Event event;
        MapElement mapElement = map.getMapList().get(user.getCurrentTime() % map.getMapList().size());


        boolean conditionsChecked;
        boolean firstIteration = true;

        do {
            if (!firstIteration) {
                user.increaseCurrentTime();
                mapElement = map.getMapList().get(user.getCurrentTime() % map.getMapList().size());
            }
            firstIteration = false;

            conditionsChecked = false;

            event = switch (mapElement.eventType) {
                case GOOD -> EventReader.getEventsRepository(dialogFactory).blockingGet().getRandomGoodEvent();
                case BAD -> EventReader.getEventsRepository(dialogFactory).blockingGet().getRandomBadEvent();
                case PLOT -> {
                    var repository = PlotLoader.getPlotRepository(dialogFactory).blockingGet();

                    int pos = user.getCurrentPlotTime();
                    if (pos >= repository.getPlotEvents().size() ||
                            !repository.getPlotEvents().get(pos).conditions.check(user, mapElement.seasonType).blockingGet()) {

                        yield null;
                    }
                    conditionsChecked = true;

                    user.increaseCurrentPlotTime();
                    yield repository.getPlotEvents().get(pos);
                }
//                case BUY_CHOICE -> new PossibilitiesEvent(dialogFactory);
//                case EDUCATION_CHOICE -> new PossibilitiesEvent(dialogFactory); // TEMP CODE !!!!!!!!!!
                case POSSIBILITIES -> {
                    PossibilitiesEvent possibilitiesEvent = new PossibilitiesEvent(dialogFactory);

                    if (!possibilitiesEvent.isFullyLoaded()) {
                        Throwable res = possibilitiesEvent.load().blockingGet();
                        if (res != null) {
                            flowableEmitter.onError(res);
                            throw new RuntimeException(res.getMessage());
                        }
                    }

                    possibilitiesEvent.getBusinessPossibilities().ifPresent(possibilities -> {
                        possibilities.removeIf((v) -> user.checkProperties(PropertyType.BUSINESS, v.propertyName));
                    });

                    possibilitiesEvent.getEducationPossibilities().ifPresent(possibilities -> {
                        possibilities.removeIf((v) -> user.checkProperties(PropertyType.EDUCATION, v.propertyName));
                    });

                    possibilitiesEvent.getApartmentPossibilities().ifPresent(possibilities -> {
                        possibilities.removeIf((v) -> user.checkProperties(PropertyType.HOUSE, v.propertyName));
                    });

                    yield possibilitiesEvent;
                }
            };
        } while (event == null || !(conditionsChecked || event.conditions.check(user, mapElement.seasonType).blockingGet()));


//        if (event.deferredEvents != null) {
//            user.addDeferredEvents(event.deferredEvents);
//        }


        if (!event.isFullyLoaded()) {
            Throwable res = event.load().blockingGet();
            if (res != null) {
                flowableEmitter.onError(res);
            }
        }

        flowableEmitter.onNext(event.dialogBuilder().build());

        MapElement finalMapElement = mapElement;
        for (Event deferredEvent : user.getDeferredEvents((v) -> {
            try {
                return v.conditions.check(user, finalMapElement.seasonType).blockingGet();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        })) {
//            if (deferredEvent.deferredEvents != null) {
//                user.addDeferredEvents(deferredEvent.deferredEvents);
//            }
            user.removeDeferredEvent(deferredEvent);
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
                        super.onComplete();
                    }
                });
            }
        }, BackpressureStrategy.BUFFER);

    } // Получение следующего события
}
