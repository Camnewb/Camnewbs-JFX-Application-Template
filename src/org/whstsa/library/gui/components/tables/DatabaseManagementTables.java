package org.whstsa.library.gui.components.tables;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.impl.library.Library;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.dialogs.LibraryMetaDialogs;
import org.whstsa.library.gui.dialogs.PersonMetaDialogs;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import static org.whstsa.library.gui.api.LibraryManagerUtils.returnPeople;

public class DatabaseManagementTables {

    public static StackPane libraryOverviewTable() {
        Table<ILibrary> libraryTable = new Table<>();
        libraryTable.addColumn("Library Name", "name", true, TableColumn.SortType.DESCENDING, 100);
        ObservableReference<List<ILibrary>> observableReference = () -> ObjectDelegate.getLibraries();
        libraryTable.setReference(observableReference);

        Button newLibraryButton = GuiUtils.createButton("New Library", (event) -> {
            LibraryMetaDialogs.createLibrary((library) -> {
                if (library == null) {
                    return;
                }
                Loader.getLoader().loadLibrary(library);
                libraryTable.pollItems();
            });
        });

        Button editLibraryButton = GuiUtils.createButton("Edit Library", (event) -> {
            ILibrary selectedLibrary = libraryTable.getSelected();
            if (selectedLibrary == null) {
                return;
            }
            LibraryMetaDialogs.updateLibrary(selectedLibrary, (library) -> {
                libraryTable.refresh();
            });
        });
        editLibraryButton.setDisable(true);

        Button deleteLibraryButton = GuiUtils.createButton("Delete Library", (event) -> {
            ILibrary selectedLibrary = libraryTable.getSelected();
            if (selectedLibrary == null) {
                return;
            }
            LibraryMetaDialogs.deleteLibrary(selectedLibrary, (library) -> {
                libraryTable.pollItems();
            });
        });
        Button openLibraryButton = GuiUtils.createButton("Open Library");

        libraryTable.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            editLibraryButton.setDisable(newSelection == null);
        });

        StackPane libraryButtonContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.VERTICAL, newLibraryButton, editLibraryButton, deleteLibraryButton, openLibraryButton);

        return GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, libraryButtonContainer, libraryTable.getTable());
    }

    public static StackPane personOverviewTable() {
        Table<IPerson> personTable = new Table<>();
        personTable.addColumn("First Name", "firstName", true, TableColumn.SortType.DESCENDING, 50);
        personTable.addColumn("Last Name", "lastName", true, TableColumn.SortType.DESCENDING, 50);
        ObservableReference<List<IPerson>> observableReference = () -> ObjectDelegate.getPeople();
        personTable.setReference(observableReference);

        Button newPersonButton = GuiUtils.createButton("New Person", event -> {
            PersonMetaDialogs.createPerson(person -> {
                if (person == null) {
                    return;
                }
                Loader.getLoader().loadPerson(person);
                personTable.pollItems();
            });
        });

        Button editPersonButton = GuiUtils.createButton("Edit Person", event -> {
            IPerson selectedPerson = personTable.getSelected();
            if (selectedPerson == null) {
                return;
            }
            PersonMetaDialogs.updatePerson(selectedPerson, person -> {
                personTable.refresh();
            });
        });
        editPersonButton.setDisable(true);

        Button deletePersonButton = GuiUtils.createButton("Delete Person", event -> {
            IPerson selectedPerson = personTable.getSelected();
            if (selectedPerson == null) {
                return;
            }
            PersonMetaDialogs.deletePerson(selectedPerson, person -> {
                personTable.pollItems();
            });
        });
        deletePersonButton.setDisable(true);

        personTable.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean disabled = newSelection == null;
            editPersonButton.setDisable(disabled);
            deletePersonButton.setDisable(disabled);
        });

        StackPane personButtonContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.VERTICAL, newPersonButton, editPersonButton, deletePersonButton);

        return GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, personTable.getTable(), personButtonContainer);
    }

    public static BorderPane libraryManagerTable() {
        Table<IPerson> mainPersonTable = new Table<>();
        Table<IBook> mainBookTable = new Table<>();
        memberManagerTable(mainPersonTable);
        bookManagerTable(mainBookTable);

        //Toggle Button Group
        ToggleGroup viewButtons = new ToggleGroup();

        ToggleButton viewMembers = GuiUtils.createToggleButton("Members");
        viewMembers.setUserData(true);
        viewMembers.setToggleGroup(viewButtons);
        viewMembers.setSelected(true);
        ToggleButton viewBooks = GuiUtils.createToggleButton("Books");
        viewBooks.setToggleGroup(viewButtons);
        viewBooks.setUserData(false);


        HBox viewSwitch = GuiUtils.createHBox(2, viewMembers, viewBooks);

        Label membersLabel = GuiUtils.createLabel("Members", 16);
        Button memberNew = GuiUtils.createButton("New", GuiUtils.defaultClickHandler());
        Button memberList = GuiUtils.createButton("List", GuiUtils.defaultClickHandler());
        Button memberSearch = GuiUtils.createButton("Search", GuiUtils.defaultClickHandler());
        Button memberDelete = GuiUtils.createButton("Delete", GuiUtils.defaultClickHandler());


        Label booksLabel = GuiUtils.createLabel("Books", 16);
        Button bookAdd = GuiUtils.createButton("Add", GuiUtils.defaultClickHandler());
        Button bookList = GuiUtils.createButton("List", GuiUtils.defaultClickHandler());
        Button bookDelete = GuiUtils.createButton("Delete", GuiUtils.defaultClickHandler());
        Button bookSearch = GuiUtils.createButton("Search", GuiUtils.defaultClickHandler());

        Button settingsButton = GuiUtils.createButton("Settings", GuiUtils.defaultClickHandler());

        VBox buttonGroup = GuiUtils.createVBox(15, viewSwitch, membersLabel, memberNew, memberList, memberSearch, memberDelete, booksLabel, bookAdd, bookList, bookDelete, bookSearch, settingsButton);
        buttonGroup.setSpacing(5.0);

        BorderPane mainContainer = GuiUtils.createBorderPane(GuiUtils.Direction.LEFTHAND, mainPersonTable.getTable(), buttonGroup);

        viewButtons.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
                if (new_toggle == null) {
                    return;
                }
                else {
                    if ((boolean) viewButtons.getSelectedToggle().getUserData()) {
                        LibraryDB.LOGGER.debug("Swictching to Member table");
                        mainContainer.setCenter(mainPersonTable.getTable());
                    }
                    else {
                        LibraryDB.LOGGER.debug("Swictching to Book table");
                        mainContainer.setCenter(mainBookTable.getTable());
                    }
                }

            }
        });
        return mainContainer;
    }

    public static Table memberManagerTable(Table<IPerson> mainTable) {
        mainTable.addColumn("First Name", "firstName", true, TableColumn.SortType.DESCENDING, 100);
        mainTable.addColumn("Last Name", "lastName", true, TableColumn.SortType.DESCENDING, 100);
        mainTable.addColumn("Teacher", "teacher", true, TableColumn.SortType.DESCENDING, 50);
        mainTable.addColumn("Fines", "fines", true, TableColumn.SortType.DESCENDING, 50);//TODO make method for this
        ObservableReference<List<IPerson>> observableReference = () -> ObjectDelegate.getPeople();//TODO use function that gets members from library
        mainTable.setReference(observableReference);
        return mainTable;
    }

    public static Table bookManagerTable(Table<IBook> mainTable) {
        mainTable.addColumn("Title", "title", true, TableColumn.SortType.DESCENDING, 200);
        mainTable.addColumn("Author", "authorName", true, TableColumn.SortType.DESCENDING, 100);
        mainTable.addColumn("Genre", "type", true, TableColumn.SortType.DESCENDING, 50);
        mainTable.addColumn("Copies", "copies", true, TableColumn.SortType.DESCENDING, 50);//TODO make method for this
        ObservableReference<List<IBook>> observableReference = () -> ObjectDelegate.getLibraries().get(0).getBooks();
        mainTable.setReference(observableReference);
        return mainTable;
    }

}
