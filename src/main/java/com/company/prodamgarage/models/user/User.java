package com.company.prodamgarage.models.user;

import com.company.prodamgarage.observable.SimpleObservable;
import com.company.prodamgarage.models.eventModels.Event;
import com.company.prodamgarage.observable.SubscribeBuilder;
import io.reactivex.*;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String serializationPath = "src/main/resources/data/testSave.ser";

    private final SimpleObservable<Integer> age = new SimpleObservable<>(0);
    private final SimpleObservable<String> name = new SimpleObservable<>("");
    private final SimpleObservable<Integer> cash = new SimpleObservable<>(0);
    private final SimpleObservable<Integer> credit = new SimpleObservable<>(0);
    private final SimpleObservable<Integer> moneyFlow = new SimpleObservable<>(0);
    private final SimpleObservable<Integer> mapPosition = new SimpleObservable<>(0);
    private final SimpleObservable<Integer> currentTime = new SimpleObservable<>(0);
    private final SimpleObservable<Integer> currentPlotTime = new SimpleObservable<>(0);
    private final HashMap<PropertyType, List<String>> properties = new HashMap<>();
    private static String imagePath = "src/main/resources/images/image1.png";

    private String customImagePath = null;

    private static volatile User instance;

    private volatile List<Event> deferredEvents = new ArrayList<>();


    @Nonnull
    public static Single<User> getInstance() {
        return Single.create(singleSubscriber -> {
            synchronized (User.class) {
                if (instance == null) {
                    Throwable err = reload(serializationPath, imagePath).blockingGet();
                    if (err != null) {
                        if (!(err instanceof IOException)) {
                            err.printStackTrace();
                        }
                        singleSubscriber.onError(new RuntimeException("user load error. " + err));
                        return;
                    }
                }
                singleSubscriber.onSuccess(instance);
            }
        });
    }

    private User() {}

    public static Completable reload(String filePath, String imagePath) {
        return Completable.create(completableEmitter -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(filePath);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                instance = (User) objectInputStream.readObject();
            } catch (FileNotFoundException e) {
                instance = new User();
                instance.save();
            } catch (IOException | ClassNotFoundException e) {
                completableEmitter.onError(e);
            }
            if (!imagePath.equals(User.imagePath)) {
                instance.customImagePath = filePath;
            } else {
                instance.customImagePath = null;
            }

            completableEmitter.onComplete();
        });
    }

    public static Completable reload() throws IOException {
        return reload(serializationPath, imagePath);
    }

    public void save(String filePath) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(filePath);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.close();
    }

    public void save() throws IOException {
        instance.save(serializationPath);
    }

    public int getCash() {
        return cash.get();
    }

    public void setCash(int cash) {
        this.cash.set(cash);
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public int getCredit() {
        return credit.get();
    }

    public void setCredit(int credit) {
        this.credit.set(credit);
    }

    public int getMoneyFlow() {
        return moneyFlow.get();
    }

    public void setMoneyFlow(int moneyFlow) {
        this.moneyFlow.set(moneyFlow);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        User.imagePath = imagePath;
    }

    public int getMapPosition() {
        return mapPosition.get();
    }

    public void setMapPosition(int mapPosition) {
        this.mapPosition.set(mapPosition);
    }
    
    public void setCurrentTime(int time){
        currentTime.set(time);
    }

    public int getCurrentTime(){
        return currentTime.get();
    }

    public void increaseCurrentTime(){
        currentTime.set(currentTime.get() + 1);
    }

    public int getCurrentPlotTime(){
        return currentPlotTime.get();
    }

    public void increaseCurrentPlotTime(){
        currentPlotTime.set(currentPlotTime.get() + 1);
    }

    public void addDeferredEvents(List<Event> events) {
        deferredEvents.addAll(events);
    }

    public void addDeferredEvent(Event event) {
        deferredEvents.add(event);
    }

    public List<Event> getDeferredEvents(Predicate<Event> predicate) {
        return deferredEvents.stream().filter(predicate).collect(Collectors.toList());
    }

    public Object getRequiredParameter(String param) {
        switch (param) {
            case ("age"):
                return this.age.get();
            case ("name"):
                return this.name.get();
            case ("cash"):
                return this.cash.get();
            case ("credit"):
                return this.credit.get();
            case ("moneyFlow"):
                return this.moneyFlow.get();
            case ("mapPosition"):
                return this.mapPosition.get();
            case ("currentTime"):
                return this.currentTime.get();
        }
        return null;
    }
    public void addProperties(PropertyType propertyType, String value) {
        properties.merge(propertyType, new ArrayList<>(List.of(value)), (f, s) -> {
            f.addAll(s);
            return f;
        });
    }



    public SubscribeBuilder<Integer> subscribeAge() {
        return age.subscribe();
    }

    public SubscribeBuilder<Integer> subscribeCash() {
        return cash.subscribe();
    }

    public SubscribeBuilder<Integer> subscribeMoneyFlow() {
        return moneyFlow.subscribe();
    }

    public SubscribeBuilder<Integer> subscribeCurrentTime() {
        return currentTime.subscribe();

    }
 }
