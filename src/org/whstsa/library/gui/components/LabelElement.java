package org.whstsa.library.gui.components;


import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class LabelElement extends Label implements Element {

    private String label;
    private String id;

    public LabelElement(String id, String label) {
        super.setText(label);
        super.setFont(Font.font(14));
        this.id = id;
    }

    public Node getComputedElement() {
        return this;
    }

    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public Boolean getResult() {
        return null;
    }

    public String getString() {
        return super.getText();
    }

    public boolean getBoolean() {
        return false;
    }
}
