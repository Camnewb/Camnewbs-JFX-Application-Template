package org.whstsa.library.gui.api;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.whstsa.library.AppMain;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.gui.factories.GuiUtils;

/**
 * Main Gui page
 */
public class GuiMain implements Gui {

    private AppMain appMain;

    public GuiMain(AppMain appMain) {
        this.appMain = appMain;
    }

    @Override
    public Scene draw() {

        MainMenuBar menuBar = new MainMenuBar(appMain);

        StackPane libraryContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, new Table<String>().getTable());

        StackPane personContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, new Table<String>().getTable());

        StackPane tableContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, libraryContainer, personContainer);

        LabelElement title = GuiUtils.createTitle("Application");

        VBox container = new VBox(menuBar.getMenu(), title, tableContainer);
        container.setSpacing(10);
        container.setAlignment(Pos.TOP_CENTER);

        return new Scene(container, 800, 512);
    }

    @Override
    public String getUUID() {
        return "GUI_MAIN";
    }

}
