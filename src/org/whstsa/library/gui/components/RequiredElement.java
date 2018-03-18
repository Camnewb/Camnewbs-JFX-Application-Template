package org.whstsa.library.gui.components;

import java.util.function.Consumer;

/**
 * Element interface for input fields that must be completed before a user can submit a form
 */
public interface RequiredElement extends Element {

    /**
     * Whether a field is required
     */
    boolean isRequired();

    void setRequired(boolean required);

    /**
     * If a field has been filled by the user
     * @return Whether the field has been satisfied
     */
    boolean isSatisfied();

    /**
     * Action to be performed upon field satisfaction
     * @param onSatisfactionUpdate Action
     */
    void setOnSatisfactionUpdate(Consumer<Boolean> onSatisfactionUpdate);
}
