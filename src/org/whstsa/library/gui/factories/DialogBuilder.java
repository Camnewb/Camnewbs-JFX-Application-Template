package org.whstsa.library.gui.factories;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.Operator;
import org.whstsa.library.gui.components.Element;

import java.util.*;

public class DialogBuilder {

    private List<Element> elementList;
    private String title;
    private boolean isCancellable;
    private List<ButtonType> buttons;
    private Map<ButtonType, Callback<Event>> buttonActionMap;
    private List<ButtonType> closingButtons;

    private Operator<GridPane, List<Element>, GridPane> gridPaneOperator;

    {
        this.elementList = new ArrayList<>();
        this.title = null;
        this.isCancellable = true;
        this.setFormatAssembler((grid, data) -> {
            int increment = 0;
            for (Element element : this.elementList) {
                grid.add(element.getComputedElement(), 0, increment);
                increment++;
            }
            return grid;
        });
        this.buttons = new ArrayList<>();
        this.buttonActionMap = new HashMap<>();
        this.closingButtons = new ArrayList<>();
    }

    public DialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public DialogBuilder addElement(Element element) {
        return this.addAllElements(element);
    }

    public DialogBuilder addAllElements(List<Element> elements) {
        this.elementList.addAll(elements);
        return this;
    }

    public DialogBuilder addAllElements(Element ...elements) {
        return this.addAllElements(Arrays.asList(elements));
    }

    public DialogBuilder addTextField(String prompt, String placeholder, boolean inline) {
        return this.addElement(GuiUtils.createTextField(prompt, inline, placeholder));
    }

    public DialogBuilder addTextField(String prompt, String placeholder) {
        return this.addTextField(prompt, placeholder, false);
    }

    public DialogBuilder addTextField(String prompt) {
        return this.addTextField(prompt, null);
    }

    public DialogBuilder addCheckBox(String prompt, boolean selected, boolean inline) {
        return this.addElement(GuiUtils.createCheckBox(prompt, selected, inline));
    }

    public DialogBuilder addCheckBox(String prompt, boolean selected) {
        return this.addCheckBox(prompt, selected, false);
    }

    public DialogBuilder addCheckBox(String prompt) {
        return this.addCheckBox(prompt, false);
    }

    public DialogBuilder setIsCancellable(boolean cancellable) {
        this.isCancellable = cancellable;
        return this;
    }

    public DialogBuilder addButton(ButtonType button, boolean closer, Callback<Event> clickConsumer) {
        this.addButton(button);
        return this.onClick(button, clickConsumer);
    }

    public DialogBuilder addButton(ButtonType button) {
        return this.addButtons(button);
    }

    public DialogBuilder addButtons(ButtonType ...buttons) {
        return this.addButtons(Arrays.asList(buttons));
    }

    public DialogBuilder addButtons(List<ButtonType> buttons) {
        this.buttons.addAll(buttons);
        return this;
    }

    public DialogBuilder onClick(ButtonType button, Callback<Event> clickConsumer) {
        this.buttonActionMap.put(button, clickConsumer);
        return this;
    }

    public List<ButtonType> getButtons() {
        return this.buttons;
    }

    public DialogBuilder setFormatAssembler(Operator<GridPane, List<Element>, GridPane> gridPaneOperator) {
        this.gridPaneOperator = gridPaneOperator;
        return this;
    }

    public boolean isCancellable() {
        return this.isCancellable;
    }

    public List<Element> getElementList() {
        return this.elementList;
    }

    public Dialog<Map<String, Element>> build() {
        Dialog<Map<String, Element>> dialog = new Dialog<>();
        dialog.setTitle(this.title);

        dialog.getDialogPane().getButtonTypes().addAll(this.getButtonList());

        GridPane grid = this.gridPaneOperator.mutate(DialogUtils.buildGridPane(), this.elementList);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> {
            if (grid.getChildren().size() == 0) {
                return;
            }
            grid.getChildren().get(0).requestFocus();
        });

        DialogUtils.addInputFieldMatcher(dialog);

        this.buttonActionMap.forEach((button, action) -> {
            Node buttonNode = dialog.getDialogPane().lookupButton(button);
            if (buttonNode != null) {
                buttonNode.addEventFilter(ActionEvent.ACTION, event -> {
                    action.callback(event);
                    if (this.closingButtons.contains(button)) {
                        buttonNode.getScene().getWindow().hide();
                    }
                });
            }
        });

        this.closingButtons.stream().filter(button -> !this.buttonActionMap.containsKey(button)).forEach(button -> {
            Node buttonNode = dialog.getDialogPane().lookupButton(button);
            if (buttonNode != null) {
                buttonNode.addEventFilter(ActionEvent.ACTION, event -> {
                    buttonNode.getScene().getWindow().hide();
                });
            }
        });

        return dialog;
    }

    protected final List<ButtonType> getRawButtonList() {
        List<ButtonType> buttonList = this.buttons;
        if (buttonList.size() == 0) {
            buttonList.add(ButtonType.OK);
        }
        return buttonList;
    }

    protected final List<ButtonType> getButtonList() {
        if (!this.isCancellable) {
            return this.getRawButtonList();
        }
        List<ButtonType> buttonList = new ArrayList<>();
        buttonList.add(ButtonType.CANCEL);
        buttonList.addAll(this.getRawButtonList());
        return buttonList;
    }

}
