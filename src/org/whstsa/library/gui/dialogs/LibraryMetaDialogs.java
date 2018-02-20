package org.whstsa.library.gui.dialogs;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.impl.library.Library;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.db.Loader;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;

import java.util.Map;

public class LibraryMetaDialogs {

    private static final String LIBRARY_FIELD = "Library Name";

    public static void createLibrary(Callback<ILibrary> callback) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .addButton(ButtonType.FINISH)
                .setIsCancellable(true)
                .setTitle("Please provide a library name")
                .addTextField(LIBRARY_FIELD, null, true, true)
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            if (results.get(LIBRARY_FIELD).getString() == null) {
                callback.callback(null);
                return;
            }
            ILibrary library = new Library((String) results.get(LIBRARY_FIELD).getResult());
            callback.callback(library);
        });
    }

    public static void updateLibrary(ILibrary library, Callback<ILibrary> callback) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder().addButton(ButtonType.FINISH)
                .setIsCancellable(true)
                .setTitle("Updating Library")
                .addTextField(LIBRARY_FIELD, null, library.getName(), true, true)
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            String newName = (String) results.get(LIBRARY_FIELD).getResult();
            if (newName == null) {
                callback.callback(library);
                return;
            }
            library.setName(newName);
            callback.callback(library);
        });
    }

    public static void deleteLibrary(ILibrary library, Callback<ILibrary> callback) {
        new DialogBuilder()
                .setTitle("Delete Library")
                .addButton(ButtonType.YES, true, event -> {
                    Loader.getLoader().unloadLibrary(library.getID());
                    callback.callback(library);
                })
                .addButton(ButtonType.NO, true, event -> {
                    callback.callback(null);
                })
                .setIsCancellable(false)
                .build()
                .show();
    }

}
