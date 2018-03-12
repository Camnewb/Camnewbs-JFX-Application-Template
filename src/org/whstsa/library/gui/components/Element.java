package org.whstsa.library.gui.components;

import javafx.scene.Node;

/**
 * Interface for easily customizable and easy-to-implement elements.
 * Used internally to replace stock JavaFX elements and create custom nodes.
 */
public interface Element {

    /**
     * Used in DialogueBuilder, should return a SplitPane with a label and the desired element
     * @return SplitPane with label and element
     */
    Node getComputedElement();

    /**
     * Unique ID for the element, such as "backButton"
     * @return
     */
    String getID();

    void setID(String id);

    /**
     * Used to fetch any user input from an element
     * @return
     */
    Object getResult();

    /**
     * Used to fetch a String input from an element
     * @return
     */
    String getString();

    /**
     *Used to fetch a boolean input from an element
     * @return
     */
    boolean getBoolean();
}
