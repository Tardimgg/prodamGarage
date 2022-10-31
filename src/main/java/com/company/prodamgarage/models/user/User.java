package com.company.prodamgarage.models.user;

import com.company.prodamgarage.models.eventModels.Event;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String serializationPath = "src/main/resources/data/testSave.ser";

    private int age;
    private String name;
    private int cash;
    private int credit;
    private int moneyFlow;
    private int mapPosition;
    private int currentTime;
    private int currentPlotTime = 0;
    private HashMap<PropertyType, List<String>> properties = new HashMap<>();
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
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getMoneyFlow() {
        return moneyFlow;
    }

    public void setMoneyFlow(int moneyFlow) {
        this.moneyFlow = moneyFlow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        User.imagePath = imagePath;
    }

    public int getMapPosition() {
        return mapPosition;
    }

    public void setMapPosition(int mapPosition) {
        this.mapPosition = mapPosition;
    }
    
    public void setCurrentTime(int time){currentTime = time;}
    public int getCurrentTime(){ return currentTime;}
    public void increaseCurrentTime(){currentTime += 1;}

    public int getCurrentPlotTime(){ return currentPlotTime;}
    public void increaseCurrentPlotTime(){currentPlotTime += 1;}

    public void addDeferredEvents(List<Event> events) {
        deferredEvents.addAll(events);
    }

    public void addDeferredEvent(Event event) {
        deferredEvents.add(event);
    }

    public List<Event> getDeferredEvents(Predicate<Event> predicate) {
        return deferredEvents.stream().filter(predicate).collect(Collectors.toList());
    }
 }
