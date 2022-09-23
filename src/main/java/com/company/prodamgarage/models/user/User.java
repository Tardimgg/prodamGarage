package com.company.prodamgarage.models.user;

import io.reactivex.Single;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.Objects;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String serializationPath = "src/main/resources/data/testSave.ser";

    private int age;
    private String name;
    private int cash;
    private int credit;
    private int moneyFlow;
    private int mapPosition;
    private static String imagePath = "src/main/resources/images/image1.png";

    private String customImagePath = null;

    private static volatile User instance;

    @Nonnull
    public static Single<User> getInstance() throws IOException {
        return Single.create(singleSubscriber -> {
            synchronized (User.class) {
                if (instance == null) {
                    try {
                        reload(serializationPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        singleSubscriber.onError(new Throwable("user load error. " + e));
                        return;
                    }
                }
                singleSubscriber.onSuccess(instance);
            }
        });
    }

    private User() {}

    // add RxJava
    public static void reload(String filePath) throws IOException {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            instance = (User) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            instance = new User();
            instance.save();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (!filePath.equals(User.imagePath)) {
            instance.customImagePath = filePath;
        } else {
            instance.customImagePath = null;
        }
    }

    public static void reload() throws IOException {
        reload(serializationPath);
    }

    public void save(String filePath) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(filePath);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.close();
    }

    public void save() throws IOException {
        instance.save(Objects.requireNonNullElse(instance.customImagePath, serializationPath));
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
        this.imagePath = imagePath;
    }

    public int getMapPosition() {
        return mapPosition;
    }

    public void setMapPosition(int mapPosition) {
        this.mapPosition = mapPosition;
    }
}
