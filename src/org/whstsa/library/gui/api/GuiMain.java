package org.whstsa.library.gui.api;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.impl.Person;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.InterfaceManager;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.components.tables.DatabaseManagementTables;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.ArrayUtils;
import org.whstsa.library.util.ClickHandler;

import java.util.ArrayList;
import java.util.List;

public class GuiMain implements Gui {

    private LibraryDB libraryDB;

    public GuiMain(LibraryDB libraryDB) {
        this.libraryDB = libraryDB;
    }

    @Override
    public Scene draw() {

        Table<ILibrary> libraryTable = new Table<>();
        Table<IPerson> personTable = new Table<>();

        GuiMenuBar menuBar = new GuiMenuBar(null, null, null, libraryTable, personTable, libraryDB);

        StackPane libraryContainer = DatabaseManagementTables.libraryOverviewTable(this.libraryDB, libraryTable);

        StackPane personContainer = DatabaseManagementTables.personOverviewTable(personTable);

        StackPane tableContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, libraryContainer, personContainer);

        LabelElement title = GuiUtils.createTitle("Library Manager");

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
