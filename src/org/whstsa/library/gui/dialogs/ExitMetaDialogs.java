package org.whstsa.library.gui.dialogs;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.whstsa.library.gui.factories.DialogBuilder;

public class ExitMetaDialogs {

    public static void exitConfirm() {
        Dialog dialog = new DialogBuilder()
                .setTitle("Quit?")
                .addButton(ButtonType.YES, true, event -> {
                    System.exit(0);
                })
                .addButton(ButtonType.NO, true, event -> {
                })
                .setIsCancellable(false)
                .build();
        dialog.show();
    }

}
