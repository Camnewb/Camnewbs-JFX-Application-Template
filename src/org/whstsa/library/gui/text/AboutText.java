package org.whstsa.library.gui.text;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AboutText {

    private final Text aboutTextContent;

    public AboutText() {
        aboutTextContent = new Text("" +
                "This application was originally designed and programmed by the Wharton High School Technology Student " +
                "Association Software Development Team (WHSTSASDT) of Eric Rabil, Cameron Newborn, and Andre Roberts. " +
                "It was forked by myself to derive a JavaFX application template. \n" +
                "This software is available under the M.I.T. license. ");
    }

    private static TextFlow toTextFlow(Text... text) {
        return new TextFlow(text);
    }

    public TextFlow getTextFlow() {
        return toTextFlow(aboutTextContent);
    }

}
