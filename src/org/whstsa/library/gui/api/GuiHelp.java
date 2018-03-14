package org.whstsa.library.gui.api;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.whstsa.library.AppMain;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.gui.text.HelpText;

/**
 * Gui page for more detailed information and tutorials about the application
 */
public class GuiHelp implements Gui {

    private BorderPane window;
    private HelpText helpText;

    GuiHelp(AppMain appMain) {

        Button backButton = GuiUtils.createButton("Back", true, event -> GuiUtils.goBack(appMain));
        VBox buttonBar = GuiUtils.createVBox(backButton);
        buttonBar.setAlignment(Pos.CENTER);

        helpText = new HelpText();

        Pagination pageSelect = new Pagination();
        pageSelect.setPageFactory(this::displayPage);
        pageSelect.setPageCount(helpText.getPageAmount());

        BorderPane container = new BorderPane();
        container.setCenter(pageSelect);
        container.setTop(buttonBar);

        this.window = container;
    }

    private VBox displayPage(int pageIndex) {
        return helpText.getContent(pageIndex);
    }


    @Override
    public Scene draw() {
        return new Scene(window, 800, 600);
    }

    @Override
    public String getUUID() {
        return "GUI_HELP";
    }
}
