package com.company.prodamgarage.models.dialog.consoleDialogs;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.GameOver;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.user.UserChanges;

import java.util.List;

public class SelectionConsoleDialog implements Dialog {

    String title;
    String mainText;
    List<Pair<String, UserChanges>> changes;


    public SelectionConsoleDialog(String title, String mainText, List<Pair<String, UserChanges>> changes) {
        this.title = title;
        this.mainText = mainText;
        this.changes = changes;
    }

    @Override
    public Object create() throws GameOver {
        ConsoleIOSystem io = ConsoleIOSystem.getInstance();

        io.println(title);
        io.println(mainText);
        io.println("Выбор: ");

        for (int i = 0; i < changes.size(); ++i) {
            Pair<String, UserChanges> pair = changes.get(i);
            String tab = "\t";
            io.println(i + 1 + ": " + pair.getKey());
            io.println(tab + pair.getValue());
        }
        io.println("Выберите действие");

        while (true) {

            int i = io.readInt();

            if (i >= 1 && i <= changes.size()) {

                // if (Game.getInstance().isPossible) {
                changes.get(i - 1).getValue().apply();
                break;

            } else {
                io.println("Попробуйте ещё раз");
            }
        }
        return null;

    }
}
