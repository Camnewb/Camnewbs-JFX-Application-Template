package org.whstsa.library.gui.dialogs;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.ComputedProperty;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.*;
import org.whstsa.library.api.impl.library.Library;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.components.TextFieldElement;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.gui.factories.LibraryManagerUtils;
import org.whstsa.library.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javafx.scene.layout.Priority.ALWAYS;

public class CheckoutMetaDialogs {

    private static String CHECKOUT = "Checkout";
    private static String PAYFINE = "Pay Fine";
    private static String BOOK = "Book";
    private static String RETURN = "Return";
    private static String QUANTITY = "Quantity";

    public static void checkOutPreMenu(Callback<IMember> callback, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Choose a member to checkout")
                .addChoiceBox(CHECKOUT, LibraryManagerUtils.getMemberNames(libraryReference.poll()), true, 0)
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            if (results.get(CHECKOUT).getResult() != null) {
                IMember selectedMember = LibraryManagerUtils.getMemberFromName((String) results.get(CHECKOUT).getResult(), libraryReference.poll());
                assert selectedMember != null;
                checkoutMemberDialog(member -> callback.callback(selectedMember), selectedMember, libraryReference);
            }
        });
    }

    private static void checkoutMemberDialog(Callback<IMember> callback, IMember member, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Checking out " + member.getName() + ".")
                .addChoiceBox(BOOK, LibraryManagerUtils.getBookTitles(libraryReference.poll()), true, -1)
                .addCheckBox(PAYFINE, false, true, member.getFine() <= 0)
                .build();
        if (member.getFine() > 0) {
            GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
            LabelElement fineLabel = GuiUtils.createLabel("$" + member.getFine(), 12);
            fineLabel.setTextFill(Color.RED);
            dialogPane.add(fineLabel, 1, 1);
        }
        DialogUtils.getDialogResults(dialog, (results) -> {
            if (member.getFine() > 0) {
                if (!results.get(PAYFINE).getBoolean()) {
                    DialogUtils.createDialog("Couldn't pay fine.", null, null, Alert.AlertType.ERROR).show();
                    return;
                }
                try {
                    member.getCheckouts().forEach(ICheckout::getFine);
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
            IBook book = LibraryManagerUtils.getBookFromTitle((String) results.get(BOOK).getResult(), libraryReference.poll());
            Integer quantity = null;
            Element quantityElement = results.get(QUANTITY);
            if (quantityElement instanceof TextFieldElement) {
                TextFieldElement textFieldQuantityElement = (TextFieldElement) quantityElement;
                quantity = textFieldQuantityElement.getNumber();
            }
            if (quantity == null) {
                quantity = 1;
            }
            try {
                libraryReference.poll().reserveBook(member, book, quantity);
                callback.callback(member);
            } catch (BookNotRegisteredException | OutOfStockException | MaximumCheckoutsException ex) {
                DialogUtils.createDialog("There was an error.", ex.getMessage(), null, Alert.AlertType.ERROR).show();
            }
        });
    }

    private static void checkoutManagementView(boolean returning, Callback<IMember> callback, IMember member, BorderPane mainContainer, Table<IBook> bookTable, ToggleButton viewBooks, ToggleButton viewMembers, ILibrary library) {
        viewBooks.setDisable(true);
        viewMembers.setDisable(true);

        Button completionButton = GuiUtils.createButton(returning ? "Return" : "Checkout", true, GuiUtils.defaultClickHandler());
        completionButton.setStyle("-fx-base: #4fa9dd;");

        HBox mainSpacer = new HBox();
        HBox.setHgrow(mainSpacer, Priority.ALWAYS);

        List<Node> toolBarNodes = new ArrayList<>();

        final boolean hasFine = member.getFine() > 0;

        final ToolBar toolBar = new ToolBar();
        toolBar.setId("toolbar");
        toolBar.setBackground(new Background(new BackgroundFill(Color.web("#d1e3ff"), null, null)));

        final Table table = returning ? new Table<ICheckout>() : bookTable;
        table.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getTable().getSelectionModel().selectedIndexProperty().addListener((obs, oldSelection, newSelection) -> {
            toolBar.getItems().set(1, GuiUtils.createTextFlow(returning ? "checkin" : "checkout", 15, "-fx-base: #000000;",
                    returning ? "Returning" : "Checking out ",
                    table.getTable().getSelectionModel().getSelectedIndices().size() + "",
                    returning ? " books from " : " books to ",
                    member.getName() + "."));
        });

        CheckBox shouldPayFine = null;
        ComputedProperty<Boolean, CheckBox> userDidConsentToPayFine = checkBox -> checkBox == null ? false : checkBox.selectedProperty().get();

        Button closeButton = GuiUtils.createButton("X", false, 5, Pos.CENTER_RIGHT, event -> {
            ((VBox) mainContainer.getTop()).getChildren().set(1, new HBox());
            table.getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            mainContainer.setCenter(bookTable.getTable());
            viewBooks.setDisable(true);
            viewBooks.setSelected(true);
            viewMembers.setDisable(false);
            viewMembers.setSelected(false);
        });
        closeButton.setStyle("-fx-base: #ff8787;");

        toolBarNodes.add(completionButton);
        toolBarNodes.add(GuiUtils.createTextFlow("checkout", 15, "-fx-base: #1e1e1e;", returning ? "Returning " : "Checking out ", "0", returning ? " books from " : " books to ", member.getName() + "."));

        if (hasFine) {
            LabelElement spacer = GuiUtils.createLabel("      ");

            TextFlow fineLabel = GuiUtils.createTextFlow("Fine", 14, "", "Outstanding fine of ", "$" + member.getFine(), ". Pay fine?");
            ((Text) fineLabel.getChildren().get(1)).setFill(Color.RED);

            shouldPayFine = GuiUtils.createCheckBox(null, false);
            shouldPayFine.selectedProperty().addListener(((observable, oldValue, newValue) -> completionButton.setDisable(!newValue)));

            completionButton.setDisable(true);

            toolBarNodes.add(spacer);
            toolBarNodes.add(fineLabel);
            toolBarNodes.add(shouldPayFine);
        }

        toolBarNodes.add(mainSpacer);
        toolBarNodes.add(closeButton);

        toolBar.getItems().addAll(toolBarNodes);

        if (returning) {
            Table<ICheckout> checkoutTable = (Table<ICheckout>) table;
            checkoutTable.addColumn("Title", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getBook().getName()), true, TableColumn.SortType.DESCENDING, 200);
            checkoutTable.addColumn("Author", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getBook().getAuthorName()), true, TableColumn.SortType.DESCENDING, 100);
            checkoutTable.addColumn("Genre", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getBook().getType().getGenre()), true, TableColumn.SortType.DESCENDING, 50);//TODO needs better fields for table columns
            ObservableReference<List<ICheckout>> observableReference = member::getCheckouts;
            checkoutTable.setReference(observableReference);

            checkoutTable.getTable().setId("returnTable");
        } else {
            LibraryManagerUtils.addTooltip(table.getTable(), "CTRL + Click to select multiple books");
        }

        mainContainer.setCenter(table.getTable());
        ((VBox) mainContainer.getTop()).getChildren().set(1, toolBar);

        final CheckBox _shouldPayFine = shouldPayFine;

        completionButton.setOnMouseClicked(event -> {
            if (hasFine) {
                if (!userDidConsentToPayFine.get(_shouldPayFine)) {
                    Logger.DEFAULT_LOGGER.debug("Ignoring click because the user did not pay fine.");
                    // TODO: Outstanding fines
                    return;
                }
                member.getCheckouts().stream()
                        .filter(ICheckout::isOverdue)
                        .filter(checkout -> !checkout.isReturned())
                        .forEach(ICheckout::payFine);
            }

            viewBooks.setDisable(true);
            viewBooks.setSelected(true);
            viewMembers.setDisable(false);
            viewMembers.setSelected(false);

            ObservableList selectedBooks = table.getTable().getSelectionModel().getSelectedItems();

            selectedBooks.forEach(book -> {
                try {
                    if (returning) {
                        member.returnCheckout(((ICheckout) book));
                    } else {
                        library.reserveBook(member, (IBook) book, 1);
                    }
                } catch (OutOfStockException | MaximumCheckoutsException ex) {
                    DialogUtils.createDialog("Error.", ex.getMessage(), null, Alert.AlertType.ERROR).showAndWait();
                }
                callback.callback(member);
                mainContainer.setCenter(bookTable.getTable());
                ((VBox) mainContainer.getTop()).getChildren().set(1, new HBox());
                table.getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            });
        });
    }

    public static void checkInPreMenu(Callback<IMember> callback, ObservableReference<ILibrary> libraryReference) {

        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Choose a member to checkin")
                .addRequiredChoiceBox(RETURN, LibraryManagerUtils.getMemberNames(libraryReference.poll()), true, 0, false)
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            if (results.get(RETURN).getResult() != null) {
                IMember selectedMember = LibraryManagerUtils.getMemberFromName((String) results.get(RETURN).getResult(), libraryReference.poll());
                checkinMemberDialog(member -> callback.callback(selectedMember), selectedMember, libraryReference);
            }
        });
    }

    public static void checkoutMember(Callback<IMember> callback, IMember member, BorderPane mainController, Table<IBook> bookTable, ToggleButton viewBooks, ToggleButton viewMembers, ObservableReference<ILibrary> library) {
        checkoutManagementView(false, callback, member, mainController, bookTable, viewBooks, viewMembers, library.poll());
    }

    public static void checkinMember(Callback<IMember> callback, IMember member, BorderPane mainController, Table<IBook> bookTable, ToggleButton viewBooks, ToggleButton viewMembers, ObservableReference<ILibrary> library) {
        checkoutManagementView(true, callback, member, mainController, bookTable, viewBooks, viewMembers, library.poll());
    }

    private static void checkinMemberDialog(Callback<IMember> callback, IMember member, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Returning " + member.getName() + "'s books.")
                .addRequiredChoiceBox(RETURN, member.getCheckoutMap(), true, -1, false)
                .addCheckBox("Pay Fine", false, true, member.getFine() <= 0, event ->
                        member.getCheckouts().stream().filter(checkout -> checkout.getFine() > 0).forEach(ICheckout::payFine))
                .build();
        if (member.getFine() > 0) {
            GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
            LabelElement fineLabel = GuiUtils.createLabel("$" + member.getFine(), 12);
            fineLabel.setTextFill(Color.RED);
            dialogPane.add(fineLabel, 1, 1);
        }
        DialogUtils.getDialogResults(dialog, (results) -> {
            if (results.get(RETURN).getResult() != null) {
                IBook returnBook = LibraryManagerUtils.getBookFromTitle((String) results.get(RETURN).getResult(), libraryReference.poll());
                List<ICheckout> checkouts = member.getCheckouts(true);
                List<ICheckout> matches = checkouts.stream().filter(checkout -> checkout.getBook().equals(returnBook)).collect(Collectors.toList());
                if (matches.size() == 0) {
                    DialogUtils.createDialog("Error.", "Checkout does not exist", null, Alert.AlertType.ERROR).show();
                    return;
                }
                ICheckout checkout = matches.get(0);
                try {
                    member.returnCheckout(checkout);
                    callback.callback(member);
                } catch (OutstandingFinesException | MemberMismatchException e) {
                    DialogUtils.createDialog("Error.", e.getMessage(), null, Alert.AlertType.ERROR).show();
                }
            }

        });
    }
}
