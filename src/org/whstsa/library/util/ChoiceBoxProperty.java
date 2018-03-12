package org.whstsa.library.util;

/**
 * Used as a property for ChoiceBoxes, like a ClickHandler
 * @param <T>
 */
public interface ChoiceBoxProperty<T> {

    public T property(T obj);

}
