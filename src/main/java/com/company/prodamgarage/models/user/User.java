package com.company.prodamgarage.models.user;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.conditions.ConditionsTypes;
import com.company.prodamgarage.observable.SimpleObservable;
import com.company.prodamgarage.models.eventModels.Event;
import com.company.prodamgarage.observable.SubscribeBuilder;
import io.reactivex.*;
import io.reactivex.subjects.BehaviorSubject;
import javafx.scene.control.Label;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private static String rootPath = null;
    private static final String propertiesFolder = "/properties";

    static {
        try {
            new File(
                    new File(User.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + propertiesFolder
            ).mkdirs();
            rootPath = new File(User.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    //    private static final String serializationPath = "src/main/resources/data/testSave.ser";
    private static final String serializationPath = rootPath + propertiesFolder + "/testSave.ser";

    private final SimpleObservable<Integer> age = new SimpleObservable<>(20);
    private final SimpleObservable<String> name = new SimpleObservable<>("Пришелец");
    private final SimpleObservable<Integer> cash = new SimpleObservable<>(50000);
    private final SimpleObservable<Integer> credit = new SimpleObservable<>(0);
    private final SimpleObservable<Integer> moneyFlow = new SimpleObservable<>(23000);
    private final SimpleObservable<Integer> expenses = new SimpleObservable<>(18000);
    private final SimpleObservable<Integer> assets = new SimpleObservable<>(0);
    private final SimpleObservable<Integer> passive = new SimpleObservable<>(2000000);
    private final SimpleObservable<Integer> freeTime = new SimpleObservable<>(500);
    private final SimpleObservable<Integer> mapPosition = new SimpleObservable<>(0);
    private final SimpleObservable<Integer> currentTime = new SimpleObservable<>(0);
    private final SimpleObservable<Integer> currentPlotTime = new SimpleObservable<>(0);
//    private final HashMap<PropertyType, List<Pair<String, UserChanges>>> properties = new HashMap<>();
    private final SimpleObservable<HashMap<PropertyType, List<Pair<String, UserChanges>>>> properties = new SimpleObservable<>(new HashMap<>());
    private static String imagePath = "src/main/resources/images/image1.png";

//    private static

    private void destroy() {
        age.destroy();
        name.destroy();
        cash.destroy();
        credit.destroy();
        moneyFlow.destroy();
        expenses.destroy();
        assets.destroy();
        passive.destroy();
        freeTime.destroy();
        mapPosition.destroy();
        currentTime.destroy();
        currentPlotTime.destroy();
    }

    public boolean destroyed = false;

    private String customImagePath = null;

    private static volatile User instance;

    private volatile List<Event> deferredEvents = new ArrayList<>();

    public static Single<User> newInstance() {
        if (instance != null) {
            instance.destroy();
            instance.destroyed = true;
        }
        instance = new User();
        try {
            instance.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getInstance();
    }

    private static transient volatile BehaviorSubject<User> subject = null;

    public static Single<SubscribeBuilder<User>> getInstances() {
        return Single.create(singleEmitter -> {
            if (subject == null) {
                subject = BehaviorSubject.create();

                if (instance == null) {
                    getInstance().blockingGet();
                }
                subject.onNext(instance);
            }

            singleEmitter.onSuccess(new SubscribeBuilder<>(subject));
        });
    }

    @Nonnull
    public static Single<User> getInstance() {
        return Single.create(singleSubscriber -> {
            synchronized (User.class) {
                if (instance == null) {
                    Throwable err = reload(serializationPath, imagePath).blockingGet();
                    if (err != null) {
                        User usr = User.newInstance().blockingGet();
                        instance = usr;

                        if (usr == null) {

                            if (!(err instanceof IOException)) {
                                err.printStackTrace();
                            }
                            singleSubscriber.onError(new RuntimeException("user load error. " + err));
                        }
                    }
                }
                singleSubscriber.onSuccess(instance);
            }
        });
    }

    private User() {
        subject.onNext(this);
    }

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
                e.printStackTrace();
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


    public int getExpenses() {
        return expenses.get();
    }

    public int getAssets() {
        return assets.get();
    }

    public int getPassive() {
        return passive.get();
    }

    public int getFreeTime() {
        return freeTime.get();
    }

    public void setExpenses(int expenses) {
        this.expenses.set(expenses);
    }

    public void setAssets(int assets) {
        this.assets.set(assets);
    }

    public void setPassive(int passive) {
        this.passive.set(passive);
    }

    public void setFreeTime(int freeTime) {
        this.freeTime.set(freeTime);
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

    public void removeDeferredEvent(Event event) {
        deferredEvents.remove(event);
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
            case ("expenses"):
                return this.expenses.get();
            case ("assets"):
                return this.assets.get();
            case ("passive"):
                return this.passive.get();
            case ("freeTime"):
                return this.freeTime.get();
        }
        return null;
    }

    public void addProperties(PropertyType propertyType, String value, UserChanges userChanges) {
        properties.get().merge(propertyType, new ArrayList<>(List.of(Pair.create(value, userChanges))), (f, s) -> {
            f.addAll(s);
            return f;
        });
        properties.changed();
    }

    public boolean checkProperties(PropertyType propertyType, String value) {
        if (properties.get().containsKey(propertyType)) {
            return properties.get().get(propertyType).parallelStream().anyMatch((v) -> v.getKey().equals(value));
        }
        return false;
    }

    public SubscribeBuilder<HashMap<PropertyType, List<Pair<String, UserChanges>>>> subscribeProperties() {
        return properties.subscribe();
    }

    public SubscribeBuilder<String> subscribeName() {
        return name.subscribe();
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

    public SubscribeBuilder<Integer> subscribeFreeTime() {
        return freeTime.subscribe();
    }

    public SubscribeBuilder<Integer> subscribeExpenses() {
        return expenses.subscribe();
    }

    public SubscribeBuilder<Integer> subscribeAssets() {
        return assets.subscribe();
    }

    public SubscribeBuilder<Integer> subscribePassive() {
        return passive.subscribe();
    }
 }
