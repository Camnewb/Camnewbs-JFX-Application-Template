package org.whstsa.library.gui.api;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyCombination;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.Loader;
import org.whstsa.library.gui.components.MenuBarElement;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.dialogs.*;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.util.Logger;

import java.io.IOException;

public class MainMenuBar {

    private MenuBar mainMenuBar;

    public MainMenuBar() {
        this(null, null, null, null, null, null, null);
    }

    public MainMenuBar(Table<IBook> bookTable, Table<IMember> memberTable, ObservableReference<ILibrary> libraryReference, Table<ILibrary> libraryTable, Table<IPerson> personTable, LibraryDB libraryDB, GuiStatusBar statusBar) {

        MenuBarElement barElement = new MenuBarElement();
        //Ignore IDE warnings about NullPointers, they are handled
        barElement.addMenu("_File");
        barElement.addSubMenu(0, MenuBarElement.createMenu("_New"));
        barElement.addSubMenuItem(0, 0, "New _Person...", event -> PersonMetaDialogs.createPerson(person -> {if (personTable != null) {personTable.refresh();}}), KeyCombination.keyCombination("CTRL+P"));
        barElement.addSubMenuItem(0, 0, "New _Library...", event -> LibraryMetaDialogs.createLibrary(library -> {if (libraryTable != null) {libraryTable.refresh();}}), KeyCombination.keyCombination("CTRL+L"));
        barElement.addSubMenuItem(0, 0, "New _Book...", event -> BookMetaDialogs.createBook(book -> bookTable.refresh(), libraryReference), KeyCombination.keyCombination("CTRL+B"), bookTable == null);
        barElement.addSubMenuItem(0, 0, "New _Membership...", event -> MemberMetaDialogs.createMember(member -> memberTable.refresh(), libraryReference), KeyCombination.keyCombination("CTRL+M"), memberTable == null);
        barElement.addSubMenuItem(0, 0, "New _Checkout...", event -> CheckoutMetaDialogs.checkOutPreMenu(checkout -> memberTable.refresh(), libraryReference), KeyCombination.keyCombination("CTRL+C"), libraryReference == null);
        barElement.addSubMenuItem(0, 0, "New _Return...", event -> CheckoutMetaDialogs.checkInPreMenu(checkout -> memberTable.refresh(), libraryReference), KeyCombination.keyCombination("CTRL+R"), libraryReference == null);
        barElement.addMenuItem(0, "Save", event -> {
            try {
                LibraryDB.getFileDelegate().save(Loader.getLoader().computeJSON());
                if (statusBar != null) {statusBar.setSaved(true);}
                Logger.DEFAULT_LOGGER.debug("Saved a copy of the data");

            } catch (IOException ex) {
                DialogUtils.createDialog("Couldn't save", "Your data couldn't be saved. Error:\n" + ex.getLocalizedMessage()).show();
                ex.printStackTrace();
            }
        }, KeyCombination.keyCombination("CTRL+S"));
        barElement.addMenuItem(0, "_Preferences...", event -> libraryDB.getInterfaceManager().display(new GuiPreferences(libraryDB, libraryReference)), null);
        barElement.addMenuSeparator(0);
        barElement.addMenuItem(0, "_Exit", event -> ExitMetaDialogs.exitConfirm(statusBar != null && !statusBar.getSaved()), null);
        barElement.addMenu("_Edit");
        barElement.addMenuItem(1, "_Edit JSON... (Dev)", event -> EditJSONMetaDialogs.editConfirm(libraryDB.getJsonRawFile()), null);
        barElement.addSubMenu(1, MenuBarElement.createMenu("_Simulate"));
        barElement.addSubMenuItem(1, 1, "_Simulate Days", event -> SimulateMetaDialogs.simulateDay(days -> {
            if (libraryReference != null) {
                bookTable.refresh();
                memberTable.refresh();
            }
        }), KeyCombination.keyCombination("CTRL+SHIFT+S"));
        barElement.addSubMenuItem(1, 1, "Populate _Members", event -> PopulateMetaDialogs.populateMemberMenu(amount -> {
            if (libraryReference != null) {
                bookTable.refresh();
                memberTable.refresh();
            }
        }), KeyCombination.keyCombination("CTRL+SHIFT+M"));
        barElement.addSubMenuItem(1, 1, "Populate _Books", event -> PopulateMetaDialogs.populateBookMenu(amount -> {
            if (libraryReference != null) {
                bookTable.refresh();
                memberTable.refresh();
            }
        }), KeyCombination.keyCombination("CTRL+SHIFT+B"));
        barElement.addMenuItem(1, "_Refresh", event -> {if (libraryReference != null) {
            memberTable.refresh();
            bookTable.refresh();}
            else {
            libraryTable.refresh();
            personTable.refresh();
            }
        }, KeyCombination.keyCombination("F5"));
        barElement.addMenu("_Help");
        barElement.addMenuItem(2, "_About...", event -> libraryDB.getInterfaceManager().display(new GuiAbout(libraryDB, libraryReference)), null);
        barElement.addMenuItem(2, "_Help...", event -> libraryDB.getInterfaceManager().display(new GuiHelp(libraryDB, libraryReference)), null);

        this.mainMenuBar = barElement;
    }

    public MenuBar getMenu() {
        return this.mainMenuBar;
    }

}
