package org.whstsa.library.gui.dialogs;

import javafx.scene.control.Dialog;
import org.whstsa.library.AppMain;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;

/**
 * This is an example of how a dialog might be made
 */
public class ExampleDialogs {

    private static String FIRST_NAME_FIELD = "First Name";
    private static String LAST_NAME_FIELD = "Last Name";

    public static void nameField() {

        Dialog dialog = new DialogBuilder().setTitle("What's your name?")
                .setIsCancellable(true)
                .addTextField(FIRST_NAME_FIELD, "", true, true)
                .addTextField(LAST_NAME_FIELD, "", true, true)
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            String firstName = results.get(FIRST_NAME_FIELD).getString();
            String lastName = results.get(LAST_NAME_FIELD).getString();
            AppMain.LOGGER.debug(firstName + " " + lastName);
        });

    }



}
