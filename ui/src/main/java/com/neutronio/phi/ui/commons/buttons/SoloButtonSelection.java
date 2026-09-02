package com.neutronio.phi.ui.commons.buttons;

import com.badlogic.gdx.utils.Null;

/**
 * Single Button Selection. Only allows for one button to be checked at a time.
 * @param <T>
 */
public class SoloButtonSelection<T extends Button> extends AbstractButtonSelection<T> {

    // TODO add noSelection: if false, always have one item selected
    protected int selected = -1;
    protected boolean uncheckLast = true;
    protected Listener<T> listener;

    public interface Listener<T> {
        /** If selected button changed. Can return null if selection was set to null */
        void onSelectionChanged(T newButton, T oldButton);
    }
    /**
     *
     * @param index
     */
    public void setChecked(int index) {
        this.buttons.get(index).setChecked(true);
    }

    /**
     * @return The first checked button, or null.
     */
    public @Null T getChecked() {
        if (this.selected >= 0) return buttons.get(this.selected);
        return null;
    }

    /**
     * @return The first checked button index, or -1.
     */
    public int getCheckedIndex() {
        return this.selected;
    }

    public void setListener(Listener<T> listener) {
        this.listener = listener;
    }

    @Override
    public void add(T button) {
        if (button == null) throw new IllegalArgumentException("button cannot be null.");
        button.setButtonGroup(this);
        this.buttons.add(button);
        // force uncheck button
        if(this.selected >= 0) button.setChecked(false);
    }

    @Override
    public void updateCheckedState(T button, boolean newState) {
        if(button.isChecked() == newState) return;
        T oldButton = this.selected > -1 ? this.buttons.get(this.selected) : null;
        if(this.uncheckLast) {
            // simple deselect if button was selected and is unchecked
            if(this.selected == this.buttons.indexOf(button)) {
                this.buttons.get(this.selected).setChecked(false, true);
                this.selected = -1;
                if(this.listener != null) this.listener.onSelectionChanged(null, oldButton);
                return;
            }
            if(this.selected >= 0) this.buttons.get(this.selected).setChecked(false, true);
            this.selected = this.buttons.indexOf(button);
            button.setChecked(true, true);
            if(this.listener != null) this.listener.onSelectionChanged(button,oldButton);
            return;
        } else {
            // only works if no selection exists
            if(this.selected < 0) {
                this.selected = this.buttons.indexOf(button);
                button.setChecked(true, true);
                if(this.listener != null) this.listener.onSelectionChanged(button, oldButton);
            } else {
                // if button == selected, deselect button
                if(this.selected == this.buttons.indexOf(button)) {
                    this.selected = -1;
                    button.setChecked(false, true);
                    if(this.listener != null) this.listener.onSelectionChanged(null, oldButton);
                }
            }
        }
    }

    public void uncheckAll() {
        this.selected = -1;
        for(Button button : this.buttons) {
            button.setChecked(false);
        }
        // TODO finish listener
//        if(this.listener != null) this.listener.onSelectionChanged(null);
    }

    /**
     * uncheckLast = true : unchecks last selected button and changes selection
     * uncheckLast = false: user has to manually uncheck a checked button to be able to check a new one
     * @param uncheckLast
     */
    public void setUncheckLast(boolean uncheckLast) {
        this.uncheckLast = uncheckLast;
    }
}
