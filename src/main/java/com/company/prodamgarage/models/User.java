package com.company.prodamgarage.models;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.Arrays;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private static String serializationPath = "src/main/resources/saves/testSave.ser";

    private int age;
    private String name;
    private int cash;
    private int credit;
    private int moneyFlow;
    private int mapPosition;
    private String imagePath = "src/main/resources/images/image1.png";

    private static volatile User instance;

    @Nonnull
    public static User getInstance() throws IOException {
        if (instance == null) {
            reload(serializationPath);
        }
        return instance;
    }

    private User() {}

    private static void reload(String filePath) throws IOException {
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
        this.imagePath = imagePath;
    }

    public int getMapPosition() {
        return mapPosition;
    }

    public void setMapPosition(int mapPosition) {
        this.mapPosition = mapPosition;
    }
}
