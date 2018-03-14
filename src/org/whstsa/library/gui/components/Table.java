package org.whstsa.library.gui.components;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.whstsa.library.api.ObservableReference;

import java.util.List;

/**
 * The Table class provides a simple interface for creating and managing Tables and TableColumns
 * @param <T> Object being displayed
 *
 * mainTable.addColumn("Books", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getCheckouts().size() + ""), true, TableColumn.SortType.DESCENDING, 25);
 * ObservableReference<List<IMember>> observableReference = () -> libraryReference.poll().getMembers();
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Table<T> {


    private TableView<T> view;
    private ObservableReference<List<T>> observableReference;

    {
        view = new TableView<>();
    }

    public TableView<T> getTable() {
        return this.view;
    }

    public void clearItems() {
        this.view.getItems().clear();
    }

    /**
     * Sets the reference for the table's items. Changes to the list will be updated in the table
     * after using <code>refresh()</code>.
     * @param observableReference In format <code>setReference(() -> [Object list]) </code>
     */
    public void setReference(ObservableReference<List<T>> observableReference) {
        this.observableReference = observableReference;
        this.getTable().setItems(this.getItems());
    }

    /**
     * Add a new column to the table using the parameters included
     * @param title Column title
     * @param property Column value property. In format <code>(cellData) -> new ReadOnlyStringWrapper(cellData.getValue()...)</code>
     * @param sortable Whether the table will be sortable or not
     * @param sortType <code>TableColumn.SortType.DESCENDING</code> or <code>TableColumn.SortType.ASCENDING</code>
     * @param width Width in pixels
     */
    public void addColumn(String title, Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>> property, boolean sortable, TableColumn.SortType sortType, Integer width) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setCellValueFactory(property);
        column.setSortable(sortable);
        column.setSortType(sortType);
        if (width != null) {
            this.view.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            column.setMaxWidth(1f * Integer.MAX_VALUE * width);
        }
        this.view.getColumns().add(column);
    }

    public void addColumn(String title, String property, boolean sortable, TableColumn.SortType sortType, Integer width) {
        this.addColumn(title, new PropertyValueFactory<>(property), sortable, sortType, width);
    }

    public void addColumn(String title, String property, boolean sortable, TableColumn.SortType sortType) {
        this.addColumn(title, property, sortable, sortType, null);
    }

    public void addColumn(String title, String property, boolean sortable) {
        this.addColumn(title, property, sortable, TableColumn.SortType.DESCENDING);
    }

    public void addColumn(String title, String property) {
        this.addColumn(title, property, true);
    }

    /**
     * Returns the selected row item from the table
     * @return The selected item
     */
    public T getSelected() {
        return this.getTable().getSelectionModel().getSelectedItem();
    }

    /**
     * @return The selected items
     */
    public List<T> getSelectedItems() {
        return this.getTable().getSelectionModel().getSelectedItems();
    }

    /**
     * Reloads the table by resetting table items and switching the table view off then on
     */
    public void refresh() {
        this.pollItems();
        TableColumn<T, ?> tableColumn = this.view.getColumns().get(0);
        tableColumn.setVisible(false);
        tableColumn.setVisible(true);
    }

    /**
     * @return ObservableList of the table items
     */
    public ObservableList<T> getItems() {
        if (this.observableReference == null) {
            return FXCollections.observableArrayList();
        }
        return FXCollections.observableList(observableReference.poll());
    }

    /**
     * Reloads the table by resetting the table items
     */
    private void pollItems() {
        this.view.setItems(this.getItems());
    }

}
