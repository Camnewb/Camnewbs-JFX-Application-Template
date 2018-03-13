package org.whstsa.library.gui.api;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.whstsa.library.AppMain;
import org.whstsa.library.gui.Config;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.components.PreferenceFieldElement;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.FieldProperty;

import java.util.ArrayList;
import java.util.List;

public class GuiPreferences implements Gui {

    private AppMain appMain;
    private BorderPane window;
    private List<PreferenceFieldElement> prefs;

    GuiPreferences(AppMain appMain) {
        Config config = appMain.getConfig();
        this.appMain = appMain;
        this.prefs = new ArrayList<>();
        this.window = new BorderPane();

        Button backButton = GuiUtils.createButton("Back", true, event -> GuiUtils.goBack(appMain));
        HBox backButtonBar = GuiUtils.createHBox(backButton);
        backButtonBar.setAlignment(Pos.CENTER_LEFT);
        backButtonBar.setBackground(new Background(new BackgroundFill(Color.web("#f2f2f2"), null, null)));
        this.window.setTop(backButtonBar);

        LabelElement title = GuiUtils.createLabel("Preferences", 20);

        prefs.add(new PreferenceFieldElement("Preference:", FieldProperty.STRING, "anyThing", config));

        VBox settingsPane = GuiUtils.createVBox(title, assemblePreferenceFields());
        settingsPane.setSpacing(10);
        this.window.setCenter(settingsPane);

        LabelElement advisory = GuiUtils.createLabel("Some settings may require a restart to be applied.");
        advisory.setFont(new Font(11));
        Button okButton = GuiUtils.createButton("OK", true,
                event -> GuiUtils.goBack(appMain));
        okButton.setStyle("-fx-base: #91c4e2;");
        Button saveButton = GuiUtils.createButton("Save", true, event -> savePreferences());
        Button cancelButton = GuiUtils.createButton("Cancel", true, event -> GuiUtils.goBack(appMain));
        HBox saveButtonBar = GuiUtils.createHBox(advisory, okButton, saveButton, cancelButton);
        saveButtonBar.setAlignment(Pos.CENTER_RIGHT);
        this.window.setBottom(saveButtonBar);
    }

    private VBox assemblePreferenceFields() {
        VBox box = GuiUtils.createVBox();
        this.prefs.forEach(element -> box.getChildren().add(element));
        return box;
    }

    private void savePreferences() {
        prefs.forEach(PreferenceFieldElement::save);//Sets the interface values to the preferences object
        this.appMain.getConfig().save();//Writes the values from the preferences object
    }

    @Override
    public Scene draw() {
        return new Scene(window, 400, 600);
    }

    @Override
    public String getUUID() {
        return "GUI_PREFERENCES";
    }
}
