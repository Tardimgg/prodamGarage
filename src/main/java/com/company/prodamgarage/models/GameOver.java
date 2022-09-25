package com.company.prodamgarage.models;

public class GameOver extends Exception {

    private final String text;

    public GameOver(String text) {
        this.text = text;
    }

    public GameOver() {
        this.text = "";
    }
}
