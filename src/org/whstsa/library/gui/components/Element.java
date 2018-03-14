package org.whstsa.library.gui.components;

import javafx.scene.Node;

/**
 * Interface for easily customizable and easy-to-implement elements.
 * Used internally to replace stock JavaFX elements and create custom nodes.<br>
 * Most if not all Element classes should have static methods in GuiUtils for creating the elements
 */
public interface Element {

    /**
     * Used in DialogueBuilder, should return a SplitPane with a label and the desired element
     * @return SplitPane with label and element
     */
    Node getComputedElement();

    /**
     * Unique ID for the element, such as "backButton"
     * @return The ID
     */
    String getID();

    void setID(String id);

    /**
     * Used to fetch any user input from an element
     * @return The result as an Object
     */
    Object getResult();

    /**
     * Used to fetch a String input from an element
     * @return The result as a String
     */
    String getString();

    /**
     *Used to fetch a boolean input from an element
     * @return The result as a Boolean
     */
    boolean getBoolean();
}
