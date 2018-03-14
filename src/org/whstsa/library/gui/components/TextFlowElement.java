package org.whstsa.library.gui.components;

import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;

/**
 * TextFlow class which provides and easier way to create TextFlows<br>
 * Use the <code>createTextFlow()</code> method in GuiUtils to create TextFlows
 */
public class TextFlowElement extends TextFlow implements Element {

    private String id;
    private double size;
    private String css;
    private List<String> fields;

    public TextFlowElement(String id, double size, String css, List<String> fields) {
        super();
        this.id = id;
        this.size = size;
        this.fields = fields;
        this.css = css;

        this.fields.forEach(field -> super.getChildren().add(new Text(field)));
        super.setStyle(this.css);
        super.getChildren().forEach(node -> ((Text) node).setFont(Font.font(size)));
    }

    @Override
    public Node getComputedElement() {
        return this;
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public String getString() {
        return super.toString();
    }

    @Override
    public boolean getBoolean() {
        return false;
    }


}
