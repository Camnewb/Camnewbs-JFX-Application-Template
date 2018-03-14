package org.whstsa.library.gui.text;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.factories.GuiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpText {

    private List<String> titles = new ArrayList<>();
    private List<TextFlow> pages;

    /**
     * Initializes all text for help pages. Titles are added here
     */
    public HelpText() {
        titles.addAll(Arrays.asList("Help"));
        this.pages = createPages();
    }

    /**
     * Where all information in the help pages is added
     * @return List of textflows for each page
     */
    private static List<TextFlow> createPages() {
        List<TextFlow> textFlows = new ArrayList<>();

        textFlows.add(toTextFlow(toText("" +
                "This help menu is the place to look for correct usages, possible fixes, or a tutorial for " +
                "beginners to this application. \n")));

        return textFlows;
    }

    private static TextFlow toTextFlow(Text... text) {
        return new TextFlow(text);
    }

    private static Text toText(String text) {
        return new Text(text);
    }

    private LabelElement getTitle(int pageIndex) {
        return GuiUtils.createTitle(titles.get(pageIndex));
    }

    public VBox getContent(int pageIndex) {
        LabelElement title = getTitle(pageIndex);
        TextFlow text = getPage(pageIndex);
        return GuiUtils.createVBox(5, title, text);
    }

    private TextFlow getPage(int pageIndex) {
        return this.pages.get(pageIndex);
    }

    public int getPageAmount() {
        return this.titles.size();
    }

}
