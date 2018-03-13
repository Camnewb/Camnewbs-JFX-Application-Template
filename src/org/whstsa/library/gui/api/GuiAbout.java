package org.whstsa.library.gui.api;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import org.whstsa.library.AppMain;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.gui.text.AboutText;

public class GuiAbout implements Gui {

    private VBox window;

    GuiAbout(AppMain appMain) {

        Button backButton = GuiUtils.createButton("Back to Main Menu", true,
                event -> GuiUtils.goBack(appMain));

        LabelElement title = GuiUtils.createTitle("About");

        AboutText text = new AboutText();
        TextFlow mainTextFlow = text.getTextFlow();
        mainTextFlow.setMaxWidth(800);

        VBox container = GuiUtils.createVBox(15, backButton, title, mainTextFlow);
        container.setSpacing(10);

        this.window = container;
    }


    @Override
    public Scene draw() {
        return new Scene(window, 800, 512);
    }

    @Override
    public String getUUID() {
        return "GUI_ABOUT_R";
    }
}
