package com.company.prodamgarage.models.dialog.consoleDialogs;

import com.company.prodamgarage.models.Game;
import com.company.prodamgarage.models.GameOver;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.user.UserChanges;

public class NotificationConsoleDialog implements Dialog {

    String title;
    String mainText;
    UserChanges changes;

    public NotificationConsoleDialog(String title, String mainText, UserChanges changes) {
        this.title = title;
        this.mainText = mainText;
        this.changes = changes;
    }

    @Override
    public Object show() throws GameOver {
        ConsoleIOSystem io = ConsoleIOSystem.getInstance();

        io.println(title);
        io.println(mainText);
        io.println("Изменения: ");
        io.println(changes);

//        if (Game.getInstance().isPossible) {
        io.println("Нажмите enter для продолжения игры");

        io.readEnter();

        changes.apply().blockingAwait();
//        } else {
//            throw new GameOver();

        return null;

    }
}
