package org.whstsa.library.gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.NotEnoughMoneyException;
import org.whstsa.library.api.impl.Person;
import org.whstsa.library.api.impl.library.Checkout;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.api.LibraryManagerUtils;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;

import java.util.Map;

public class CheckoutMetaDialogs {

    private static final String BOOK = "Checkout Books";
    private static final String RETURN = "Return Books";
    private static final String PAYFINE = "Pay Fine";

    public static void checkoutMember(Callback<IMember> callback, IMember member, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Checkout")
                .addChoiceBox(BOOK, LibraryManagerUtils.getBookTitles(libraryReference), true, -1)
                .addCheckBox(PAYFINE, false, true, member.getFine() <= 0)
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            if (member.getFine() > 0) {
                if (!results.get(PAYFINE).getBoolean()) {
                    DialogUtils.createDialog("Couldn't pay fine. Member does not have enough money.", null, null, Alert.AlertType.ERROR).show();
                    return;
                }
                try {
                    member.getCheckouts().forEach(checkout -> {
                        checkout.getFine();
                    });
                }
            }
            IBook book = LibraryManagerUtils.getBookFromTitle((String) results.get(BOOK).getResult(), libraryReference.poll());
            try {
                libraryReference.poll().reserveBook(member, book);
            } catch (Exception ex) {
                DialogUtils.createDialog("There was an error.", ex.getMessage(), null, Alert.AlertType.ERROR).show();
            }

        });
        callback.callback(member);
    }

    public static void checkinMember(Callback<IMember> callback, IMember member, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Check In")
                .addChoiceBox(RETURN, LibraryManagerUtils.getBookTitlesFromMember(member), true, -1)
                .addCheckBox("Pay Fine", false, true, member.getFine() <= 0, event -> {
                    member.getCheckouts().forEach(checkout -> {
                        try {
                            checkout.payFine();
                        } catch (NotEnoughMoneyException ex) {
                            DialogUtils.createDialog("Couldn't pay fine. Member does not have enough money.", ex.getMessage(), null, Alert.AlertType.ERROR).show();
                        }
                    });
                })
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            if (results.get(RETURN).getResult() != null) {
                IBook returnBook = LibraryManagerUtils.getBookFromTitle((String) results.get(RETURN).getResult(), libraryReference.poll());
                member.getCheckout(returnBook).
            }

        });
        callback.callback(member);
    }

}
