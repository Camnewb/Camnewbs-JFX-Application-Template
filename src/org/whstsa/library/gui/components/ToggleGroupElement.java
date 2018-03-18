package org.whstsa.library.gui.components;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import org.whstsa.library.api.Callback;
import org.whstsa.library.gui.factories.GuiUtils;

import java.util.*;

/**
 * This is a class which emulates the ToggleGroup class by collecting ToggleButtons in one place
 * and provides a simpler interface for creating and consolidating ToggleButtons.
 * This class does not implement the Element interface, so it cannot be used for inputs in dialogs.<br/><br/>
 *
 * To setup the Toggle Group you first initialize this class, and add your buttons.<br/>
 * Buttons are added with the <code>addButton(User Data, Callback)</code> method. The UserData is
 * a unique identifier for that specific button's action, such as an enum or String, and
 * will be referenced inside the Callback.<br/>
 * The Callback is the action the button will
 * perform upon being clicked, and can be easily created with <code>(userData) -> {*Action*}</code>.
 * The callback must reference the UserData, though actually using the variable is optional.<br/>
 * After adding buttons, the <code>getButtons()</code> method will return an unformatted HBox with the buttons
 * for use in a GUI.
 */
public class ToggleGroupElement<T> extends ToggleGroup {

    private List<ToggleButton> buttonList;
    private Map<T, Callback<T>> toggleHandlerMap;

    /**
     * Initializes this class
     */
    public ToggleGroupElement() {
        buttonList = new ArrayList<>();
        toggleHandlerMap = new HashMap<>();
    }

    /**
     * Adds a button to the ToggleGroup
     * @param title Text that goes onto the button
     * @param userData Unique identifier for the action of the button
     * @param callback The button's action
     */
    public void addButton(String title, T userData, Callback<T> callback) {
        ToggleButton button = new ToggleButton();
        button.setText(title);
        button.setUserData(userData);
        button.setToggleGroup(this);
        buttonList.add(button);
        toggleHandlerMap.put(userData, callback);
    }

    /**
     * Assembles the ToggleButtons into an HBox and assigns the Toggle Property
     * @return An HBox of the buttons
     */
    public HBox getButtons() {
        HBox buttons = new HBox();
        this.buttonList.forEach(buttons.getChildren()::add);
        buttons.getChildren().add(GuiUtils.createLabel("Hello"));

        // When a toggle button is pressed, this toggle listener will parse through each item in the
        // toggleHandlerMap and call the callback that matches the userData from the pressed button.
        this.selectedToggleProperty().addListener((ov, toggle, new_toggle) -> {
            if (new_toggle != null) {
                toggleHandlerMap.forEach((userData, callback) -> {
                    if (new_toggle.getUserData().equals(userData)) {
                        callback.callback(userData);
                    }
                });
            }
        });

        return buttons;
    }
}
