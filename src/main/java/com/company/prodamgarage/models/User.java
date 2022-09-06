package com.company.prodamgarage.models;

import java.io.*;
import java.util.Arrays;

public class User implements Serializable{
    private static final long serialVersionUID = 1L;

    public String territoriesInfo;
    private String resourcesInfo;

    private int age;
    private String name;
    private int cash;
    private int credit;
    private int moneyFlow;
    private int mapPosition;
    private String imagePath = "src/main/resources/images/image1.png";

    public User(String filePath) throws IOException, ClassNotFoundException {
        try{
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            User copy = (User) objectInputStream.readObject();
            this.territoriesInfo = copy.territoriesInfo;
        } catch (FileNotFoundException e){
            System.out.println("Save does not exist");
        }
    }
    public User() throws IOException, ClassNotFoundException {
        try{
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/saves/testSave.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            User copy = (User) objectInputStream.readObject();
            this.territoriesInfo = copy.territoriesInfo;
        } catch (FileNotFoundException e){
            System.out.println("Save does not exist");
        }
    }
    public void serialization(String filePath) throws IOException{
        FileOutputStream outputStream = new FileOutputStream(filePath);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.close();
    }
    public void serialization() throws IOException {
        this.serialization("src/main/resources/saves/testSave.ser");
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
