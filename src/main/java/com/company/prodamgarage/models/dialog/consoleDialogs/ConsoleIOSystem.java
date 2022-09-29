package com.company.prodamgarage.models.dialog.consoleDialogs;

import java.util.Scanner;

public class ConsoleIOSystem {

    private static volatile ConsoleIOSystem instance;

    Scanner scanner;

    private ConsoleIOSystem() {
        scanner = new Scanner(System.in);
    }

    public static ConsoleIOSystem getInstance() {
        ConsoleIOSystem localInstance = instance;

        if (localInstance == null) {
            synchronized (ConsoleIOSystem.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ConsoleIOSystem();
                }
            }
        }
        return localInstance;
    }

    public <T> void println(T value) {
        System.out.println(value);
    }

    public int readInt() {
        return scanner.nextInt();
    }

    public void readEnter() {
        scanner.nextLine(); // fix it. Does not force you to press enter
    }

}
