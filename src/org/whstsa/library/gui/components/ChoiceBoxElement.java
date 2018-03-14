package org.whstsa.library.gui.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.ChoiceBoxProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * ChoiceBox class designed for use in dialogs
 * Along with String lists, the class also supports Maps for
 * easy matching between String labels and the intended object
 * @param <T> String
 * @param <U> Object
 */
public class ChoiceBoxElement<T, U> extends ChoiceBox implements RequiredElement {

    private Label label;
    private String id;
    private Map<T, U> items;
    private boolean required;

    public ChoiceBoxElement(String id, String label, ObservableList<T> items, boolean useLabel, int selected, boolean disabled) {
        super();
        this.id = id;
        this.label = useLabel ? GuiUtils.createLabel(label, 14) : null;
        this.setItems(items);
        if (selected != -1) {
            super.getSelectionModel().select(selected);
        }
        this.setDisable(disabled);
    }

    public ChoiceBoxElement(String id, String label, Map<T, U> items, ChoiceBoxProperty<T> property, boolean useLabel, int selected) {
        super();
        this.id = id;
        this.label = useLabel ? GuiUtils.createLabel(label) : null;
        this.items = items;
        List<T> setList = new ArrayList<>(items.keySet());
        if (property != null) {
            setList.forEach(property::property);//Used to get a string for displaying in the ChoiceBox
        }
        this.setItems(FXCollections.observableArrayList(setList));
        if (selected != -1) {
            super.getSelectionModel().select(selected);
        }
    }

    @Override
    public Node getComputedElement() {
        if (this.label == null) {
            return this;
        }
        return GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, this.label, this);
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public Object getResult() {
        return this.getSelectionModel().getSelectedItem();
    }

    @Override
    public String getString() {
        Object result = this.getResult();
        return result == null ? null : result.toString();
    }

    /**
     * Used in conjunction with the map ChoiceBox, returns the value from the map
     * using the key selected in the ChoiceBox, or the result of <code>getString()</code>.<br><br>
     *
     * Example: If the map stores values <code>Name</code> and <code>ID</code>, the ChoiceBox will display <code>Name</code>,
     * the user will select a name, and <code>getItem()</code> will return <code>ID</code>.
     * @return Value from selected key
     */
    public U getItem() {
        return this.items.get(this.getString());
    }

    @Override
    public boolean getBoolean() {
        return false;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public boolean isSatisfied() {
        return this.getResult() != null;
    }

    @Override
    public void setOnSatisfactionUpdate(Consumer<Boolean> onSatisfactionUpdate) {
        this.setOnAction(event -> onSatisfactionUpdate.accept(this.isSatisfied()));
    }
}
