package org.whstsa.library.gui.api;

import javafx.scene.control.MenuBar;
import org.whstsa.library.AppMain;
import org.whstsa.library.gui.Config;
import org.whstsa.library.gui.components.MenuBarElement;
import org.whstsa.library.gui.dialogs.*;

public class MainMenuBar {

    private MenuBar mainMenuBar;

    /**
     * Where the menu bar is initialized. Use the MenuBarElement interface for building the menu bar.
     * @see MenuBarElement
     * @param appMain
     */
    MainMenuBar(AppMain appMain) {
        Config config = appMain.getConfig();
        MenuBarElement barElement = new MenuBarElement();
        barElement.addMenu("_File");
        barElement.addMenuItem(0, "_About...", event -> appMain.getInterfaceManager().display(new GuiPreferences(   appMain)), null);
        barElement.addMenuSeparator(0);
        barElement.addMenuItem(0, "_Exit", event -> ExitMetaDialogs.exitConfirm(), null);

        barElement.addMenu("_Help");
        barElement.addMenuItem(1, "_About...", event -> appMain.getInterfaceManager().display(new GuiAbout(appMain)), null);
        barElement.addMenuItem(1, "_Help...", event -> appMain.getInterfaceManager().display(new GuiHelp(appMain)), null);

        this.mainMenuBar = barElement;
    }

    public MenuBar getMenu() {
        return this.mainMenuBar;
    }

}
