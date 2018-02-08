package org.whstsa.library.gui.dialogs;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.Identifiable;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.components.*;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.gui.factories.LibraryManagerUtils;
import org.whstsa.library.util.DayGenerator;

import java.util.List;
import java.util.Map;

public class PopulateMetaDialogs {

    private static final String BOOKS = "Books:";
    private static final String MEMBERS = "Members:";
    private static final String LIBRARY = "Selected Library:";


    public static void populateMemberMenu(Callback<Integer> callback) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Auto-Populate Members")
                .addLabel("Populate all libraries or a selected library")
                .addSpinner(MEMBERS, true, 1, 500)
                .build();
        GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
        LabelElement checkBoxLabel = GuiUtils.createLabel("All Libraries:");
        LabelElement choiceBoxLabel = GuiUtils.createLabel("Selected library:");
        CheckBoxElement checkBox = GuiUtils.createCheckBox("All libraries", true, true);
        ChoiceBoxElement choiceBox = GuiUtils.createChoiceBox(LIBRARY, LibraryManagerUtils.toObservableList(LibraryManagerUtils.getNames(ObjectDelegate.getLibraries())), true, -1, true);
        dialogPane.add(checkBoxLabel, 0, 2);//I'll fix formatting later
        dialogPane.add(checkBox, 1, 2);
        dialogPane.add(choiceBoxLabel, 0, 3);
        dialogPane.add(choiceBox, 1, 3);
        //New code: Separates Member and Book populate dialogs, replaces text fields with spinners (User can only select ints, and it will never be null)
        //Creates checkbox and choicebox to choose whether or not to populate all libraries, or select a specific library to populate. STILL NEED TO IMPLEMENT LIBRARY INTO GENERATOR
        checkBox.setOnAction(event -> {
            if (checkBox.selectedProperty().get()) {//Disables choicebox upon selecting
                choiceBox.setDisable(true);
                choiceBox.getSelectionModel().select(null);
            }
            else {
                choiceBox.setDisable(false);
            }
        });
        DialogUtils.getDialogResults(dialog , (results) -> {
            int amount;
            ILibrary library;
            amount = (int) results.get(MEMBERS).getResult();//SPINNERS! THE BE-ALL END-ALL SOLUTION! NO NULLPOINTERS OR INTOVERFLOWS! BUY YOURS TODAY! CALL 1800-YOUREWELCOMEANDRE
            if (choiceBox.getResult() != null) {
                library = LibraryManagerUtils.getLibraryFromName((String) choiceBox.getResult());
                System.out.println("A wild library appeared!");
            }
            //IMPLEMENT LIBRARY HERE
            displayPopulateTable(amount, false);
            callback.callback(amount);
            }, MEMBERS);
    }

    public static void populateBookMenu(Callback<Integer> callback) {//Same code as above, but using BOOKS instead of MEMBERS
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Auto-Populate Books")
                .addLabel("Populate all libraries or a selected library")
                .addSpinner(BOOKS, true, 1, 500)
                .build();
        GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
        LabelElement checkBoxLabel = GuiUtils.createLabel("All Libraries:");
        LabelElement choiceBoxLabel = GuiUtils.createLabel("Selected library:");
        CheckBoxElement checkBox = GuiUtils.createCheckBox("All libraries", true, true);
        ChoiceBoxElement choiceBox = GuiUtils.createChoiceBox(LIBRARY, LibraryManagerUtils.toObservableList(LibraryManagerUtils.getNames(ObjectDelegate.getLibraries())), true, -1, true);
        dialogPane.add(checkBoxLabel, 0, 2);//I'll fix formatting later
        dialogPane.add(checkBox, 1, 2);
        dialogPane.add(choiceBoxLabel, 0, 3);
        dialogPane.add(choiceBox, 1, 3);
        checkBox.setOnAction(event -> {
            if (checkBox.selectedProperty().get()) {
                choiceBox.setDisable(true);
                choiceBox.getSelectionModel().select(null);
            }
            else {
                choiceBox.setDisable(false);
            }
        });
        DialogUtils.getDialogResults(dialog , (results) -> {
            int amount;
            ILibrary library;
            amount = (int) results.get(BOOKS).getResult();
            if (choiceBox.getResult() != null) {
                library = LibraryManagerUtils.getLibraryFromName((String) choiceBox.getResult());
                System.out.println("A wild library appeared!");
            }
            //IMPLEMENT LIBRARY HERE
            displayPopulateTable(amount, true);
            callback.callback(amount);
        }, BOOKS);
    }

    public static Table<String> populateTable(Table<String> table, int amount, boolean forBook) {
        table.addColumn("Results", cellData -> new ReadOnlyStringWrapper(cellData.getValue()) , false, TableColumn.SortType.DESCENDING, 2500);
        List<String> tableItems = FXCollections.observableArrayList();
        for (int index = 0; index < amount;index++) {
            tableItems.add(forBook ? DayGenerator.generateBook() : DayGenerator.generateMember(DayGenerator.randomLibrary()));
        }
        ObservableReference<List<String>> observableReference = () -> tableItems;
        table.setReference(observableReference);
        table.getTable().setSelectionModel(null);
        return table;
    }

    public static void displayPopulateTable(int amount, boolean forBook) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Results")
                .build();
        Table<String> populateTable =  new Table<>();
        populateTable = populateTable(populateTable, amount, forBook);
        GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
        dialogPane.addRow(0, GuiUtils.createLabel("Added " + amount + (forBook ? " books" : " members") + " in all libraries."));
        dialogPane.addRow(1, populateTable.getTable());
        DialogUtils.getDialogResults(dialog, (results) -> {

        });
    }

}
