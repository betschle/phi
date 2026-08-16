package com.neutronio.phi.ui.commons.buttons;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * A common Button interface
 */
public interface Button {

    // TODO Interface Panel? for allowing focus/over reaction on SimplePanel?
    void toggle();

    void setButtonGroup(ButtonSelection buttonGroup);

    /**
     * Bruteforces the button to checked state
     * @param checked
     */
    void setChecked(boolean checked);

    /** Bruteforces the button to checked state, allows for firing an event doing so
     *
     * @param checked
     * @param fireEvents
     */
    void setChecked(boolean checked, boolean fireEvents);
    /**
     * @return true if this button can be checked. Else only allows for
     * pressed.
     */
    boolean canCheck();

    /**
     * Sets whether this button can be checked. Else only allows for
     * pressed.
     * @param canCheck
     */
    void setCanCheck(boolean canCheck);

    /**
     *
     * @return If this button was checked.
     */
    boolean isChecked();

    /**
     *
     * @return if this button is currently pressed by the user.
     */
    boolean isPressed();

    /**
     *
     * @return if this button is currently being hovered over with the user's mouse pointer.
     */
    boolean isOver();

    void setDisabled(boolean disabled);

    boolean isDisabled();

    boolean hasKeyboardFocus ();
    /** If false, {@link #setChecked(boolean)} and {@link #toggle()} will not fire {@link ChangeListener.ChangeEvent}. The event will only be
     * fired only when the user clicks the button */
    void setProgrammaticChangeEvents (boolean programmaticChangeEvents);
}

