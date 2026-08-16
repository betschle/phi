package com.neutronio.phi.ui.commons.buttons;


import java.util.Collection;
import java.util.List;

/**
 * Selection Interface for Button Groups
 * @param <T> the button type
 */
public interface ButtonSelection<T extends Button>  {

    /**
     * Adds a button to the selector.
     * Unchecks the button if it was already checked and cannot stay checked by the rules of its
     * ButtonSelection subclass
     * @param button
     */
    void add(T button);

    /**
     * Adds multiple buttons to the selector.
     * Unchecks the button if it was already checked and cannot stay checked by the rules of its
     * ButtonSelection subclass
     * @param buttons
     */
    void add(T... buttons);

    /**
     * Adds multiple buttons to the selector.
     * Unchecks the button if it was already checked and cannot stay checked by the rules of its
     * ButtonSelection subclass
     * @param buttons
     */
    void addAll(Collection<T> buttons);
    List<T> getButtons();
    void remove(T button);
    void remove(T... buttons);
    /**
     * Manages button state of the button group and sets the new state if
     * the rules of the group allow it.
     */
    void updateCheckedState(T button, boolean newState);
    void uncheckAll();
    void clear();
}
