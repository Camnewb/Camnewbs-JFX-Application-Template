package org.whstsa.library.gui.dialogs;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.GridPane;
import org.whstsa.library.World;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.DayGenerator;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class SimulateMetaDialogs {

    private static final String SIMULATE = "Days";

    public static void simulateDay(Callback<Integer> callback) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Simulate Days")
                .addTextField(SIMULATE)
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            int days;
            try {
                days = Integer.parseInt(results.get(SIMULATE).getString());
                if (days > 365) {
                    DialogUtils.createDialog("Error: Too Many Days", "You have exceeded the amount of days allowed to simulate at once, reducing to one year.", null, Alert.AlertType.ERROR).show();
                    days = 365;
                }
            } catch (NumberFormatException ex) {
                days = 0;
            }
            displaySimulateTable(days);
            callback.callback(days);
        }, SIMULATE);
    }

    public static Table<String> simulateTable (Table<String> table, int days) {
        table.addColumn("Results", cellData -> new ReadOnlyStringWrapper(cellData.getValue()) , false, TableColumn.SortType.DESCENDING, 2500);
        List<String> tableItems = FXCollections.observableArrayList();
        for (int index = 0; index < days;index++) {
            tableItems.add(World.getDate().toString());
            List<String> actions = DayGenerator.simulateDay();
            actions.forEach(action -> {
                tableItems.add(action);
            });
            Calendar cal = Calendar.getInstance();
            cal.setTime(World.getDate());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            World.setDate(cal.getTime());
        }
        ObservableReference<List<String>> observableReference = () -> tableItems;
        table.setReference(observableReference);
        table.getTable().setSelectionModel(null);
        return table;
    }

    public static void displaySimulateTable(int days) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Results")
                .build();
        Table<String> simulateTable =  new Table<>();
        simulateTable = simulateTable(simulateTable, days);
        GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
        dialogPane.addRow(0, GuiUtils.createLabel("Simulated " + days + " days in all libraries.                                   "));
        dialogPane.addRow(1, simulateTable.getTable());
        DialogUtils.getDialogResults(dialog, (results) -> {

        });
    }

}
