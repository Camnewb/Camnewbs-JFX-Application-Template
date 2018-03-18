package org.whstsa.library.gui.api;

import javafx.scene.control.MenuBar;
import org.whstsa.library.AppMain;
import org.whstsa.library.Config;
import org.whstsa.library.gui.components.MenuBarElement;
import org.whstsa.library.gui.dialogs.*;

public class MainMenuBar {

    private MenuBar mainMenuBar;

    /**
     * Where the menu bar is initialized. Use the MenuBarElement interface for building the menu bar.<br>
     * Add a "_" in the beginning of the title of a menu or menu item to allow easy navigation using the alt key
     * @see MenuBarElement
     * @param appMain AppMain
     */
    MainMenuBar(AppMain appMain) {
        Config config = appMain.getConfig();
        MenuBarElement barElement = new MenuBarElement();
        barElement.addMenu("_File");
        barElement.addMenuItem(0, "_Preferences...", event -> appMain.getInterfaceManager().display(new GuiPreferences(   appMain)), null);
        barElement.addMenuSeparator(0);
        barElement.addMenuItem(0, "_Exit", event -> ExitDialogs.exitConfirm(), null);

        barElement.addMenu("_Edit");
        barElement.addMenuItem(1, "Example Dialog...", event -> ExampleDialogs.nameField(), null);

        barElement.addMenu("_Help");
        barElement.addMenuItem(2, "_About...", event -> appMain.getInterfaceManager().display(new GuiAbout(appMain)), null);
        barElement.addMenuItem(2, "_Help...", event -> appMain.getInterfaceManager().display(new GuiHelp(appMain)), null);

        this.mainMenuBar = barElement;
    }

    public MenuBar getMenu() {
        return this.mainMenuBar;
    }

}
