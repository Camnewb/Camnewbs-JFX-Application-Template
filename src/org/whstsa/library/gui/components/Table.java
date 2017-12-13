package org.whstsa.library.gui.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.whstsa.library.api.library.ILibrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table<T> {

    private TableView<T> view;

    {
        view = new TableView<>();
    }

    public TableView<T> getTable() {
        return this.view;
    }

    public void setItems(List<T> items) {
        this.view.setItems(FXCollections.observableList(items));
    }

    public void clearItems() {
        this.view.getItems().clear();
    }

    public void addItems(List<T> items) {
        this.view.getItems().addAll(items);
    }

    public void addItem(T item) {
        this.addItems(item);
    }

    public void addItems(T ...items) {
        this.addItems(Arrays.asList(items));
    }

    public void addColumn(String title, String property, boolean sortable, TableColumn.SortType sortType, Integer width) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setSortable(sortable);
        column.setSortType(sortType);
        if (width != null) {
            this.view.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
            column.setMaxWidth( 1f * Integer.MAX_VALUE * width );
        }
        this.view.getColumns().add(column);
    }

    public void addColumn(String title, String property, boolean sortable, TableColumn.SortType sortType) {
        this.addColumn(title, property, sortable, sortType, null);
    }

    public void addColumn(String title, String property, boolean sortable) {
        this.addColumn(title, property, sortable, TableColumn.SortType.DESCENDING);
    }

    public void addColumn(String title, String property) {
        this.addColumn(title, property, false);
    }

    public void refresh() {
        this.view.getColumns().get(0).setVisible(false);
        this.view.getColumns().get(0).setVisible(true);
    }

}
