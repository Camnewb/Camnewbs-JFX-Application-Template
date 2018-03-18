package org.whstsa.library.gui.dialogs;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.whstsa.library.AppMain;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.GuiUtils;

public class ExitDialogs {

    /**
     * Confirms that a user wants to exit the program
     */
    public static void exitConfirm() {
        Dialog dialog = new DialogBuilder()
                .setTitle("Quit?")
                .addButton(ButtonType.YES, true, event -> System.exit(0))
                .addButton(ButtonType.NO, true, event -> {})//TODO This doesn't work with the red x
                .setIsCancellable(false)
                .build();
        dialog.show();
    }

}
