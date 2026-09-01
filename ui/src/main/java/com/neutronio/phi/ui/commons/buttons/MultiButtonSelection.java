package com.neutronio.phi.ui.commons.buttons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Multi Selection for Buttons. Allows for multiple buttons to be checked.
 * @param <T>
 */
public class MultiButtonSelection<T extends Button> extends AbstractButtonSelection<T> {

    // TODO test this
    protected List<Integer> selected = new ArrayList<>();
    protected int minCheckCount = 1;
    protected int maxCheckCount = 1;
    protected boolean uncheckLast = true; // TODO does uncheckLast make sense here at all?
    protected Listener<T> listener;

    public interface Listener<T> {
        /** Invoked when button was added to selection */
        void onAddToSelection(T button);
        /** Invoked when button was added to selection */
        void onRemoveFromSelection(T button);
    }

    /**
     * Sets the checked buttons. Resets the current selection
     * @param indices
     */
    public void setChecked(int... indices) {
        this.uncheckAll();
        for(int i = 0; i < indices.length; i++) {
            if(i+1 < this.maxCheckCount) {
                this.buttons.get(indices[i]).setChecked(true);
            }
        }
    }
    /**
     * Gets a list of checked buttons.
     * @return a newly created instance of list
     */
    public List<T> getChecked() {
        List<T> checked = new ArrayList<>();
        for(int index : this.selected) {
            checked.add(this.buttons.get(index));
        }
        return checked;
    }

    /**
     * Gets a list of checked Button indices.
     * @return an unmodifiable list of selected indices
     */
    public List<Integer> getCheckedIndices() {
        return Collections.unmodifiableList(this.selected);
    }

    @Override
    public void add(T button) {
        if (button == null) throw new IllegalArgumentException("button cannot be null.");
        button.setButtonGroup(this);
        boolean shouldCheck = button.isChecked() || this.buttons.size() < this.minCheckCount;
        button.setChecked(false);
        this.buttons.add(button);
        button.setChecked(shouldCheck);
        if(shouldCheck) this.selected.add(this.buttons.indexOf(button));
    }

    @Override
    public void updateCheckedState(T button, boolean newState) {
        if (button.isChecked() == newState) return;
        if(newState) {
            // attempt to check
            if(this.selected.size() > this.maxCheckCount) {
                // cannot check if selected > maxCheckCount
                // terminate without changes
                return;
            } else
            if(this.selected.size() < this.maxCheckCount) {
                // can check if selected < maxCheckCount
                this.selected.add(this.buttons.indexOf(button));
                button.setChecked(true, true);
            }
        } else {
            // attempt to uncheck
            if(this.selected.size() < this.minCheckCount) {
                // cannot uncheck if selected < minCheckCkount
                // terminate without changes
                return;
            } else
            if(this.selected.size() > this.minCheckCount) {
                // can uncheck if selected > minCheckCount
                this.selected.remove( (Integer) this.buttons.indexOf(button));
                button.setChecked(false, true);
            }
        }
    }

    @Override
    public void uncheckAll() {
        int old = this.minCheckCount;
        this.minCheckCount = 0;
        for (int i = 0, n = this.buttons.size(); i < n; i++) {
            T button = this.buttons.get(i);
            button.setChecked(false, false);
        }
        this.selected.clear();
        this.minCheckCount = old;
    }

    /**
     * Sets the minimum number of buttons that must be checked. Default is 1.
     */
    public void setMinCheckCount(int minCheckCount) {
        this.minCheckCount = minCheckCount;
    }

    /**
     * Sets the maximum number of buttons that can be checked. Set to -1 for no maximum. Default is 1.
     */
    public void setMaxCheckCount(int maxCheckCount) {
        if (maxCheckCount == 0) maxCheckCount = -1;
        this.maxCheckCount = maxCheckCount;
    }

    /**
     * If true, when the maximum number of buttons are checked and an additional button is checked, the last button to be checked
     * is unchecked so that the maximum is not exceeded. If false, additional buttons beyond the maximum are not allowed to be
     * checked. Default is true.
     */
    public void setUncheckLast(boolean uncheckLast) {
        this.uncheckLast = uncheckLast;
    }
}
