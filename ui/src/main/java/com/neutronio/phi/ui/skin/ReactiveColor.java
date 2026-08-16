package com.neutronio.phi.ui.skin;

import com.badlogic.gdx.graphics.Color;
import com.neutronio.phi.ui.commons.buttons.Button;

/**
 * A Color picked based on a button state.
 */
public class ReactiveColor {
    public Color upColor;
    public Color downColor;
    public Color overColor;
    public Color checkedColor;
    public Color checkedOverColor;
    public Color checkedDownColor;
    public Color disabledColor;
    public Color focusedColor;
    public Color focusedCheckedColor;


    public enum ReactiveState {
        UP, DOWN, OVER, CHECKED, CHECKED_OVER, CHECKED_DOWN, DISABLED, FOCUSED, FOCUSED_CHECKED;
    }

    public ReactiveColor copy() {
        ReactiveColor copy = new ReactiveColor();
        copy.upColor = this.upColor.cpy();
        copy.downColor = this.downColor.cpy();
        copy.overColor = this.overColor.cpy();
        if( this.checkedColor != null )
            copy.checkedColor = this.checkedColor.cpy();
        if( this.checkedOverColor != null )
            copy.checkedOverColor = this.checkedOverColor.cpy();
        if( this.checkedDownColor != null )
            copy.checkedDownColor = this.checkedDownColor.cpy();
        if( this.disabledColor != null )
            copy.disabledColor = this.disabledColor.cpy();
        if( this.focusedColor != null )
            copy.focusedColor = this.focusedColor.cpy();
        if( this.focusedCheckedColor != null )
            copy.focusedCheckedColor = this.focusedCheckedColor.cpy();
        return copy;
    }

    /**
     * Copied & adapted from {@link com.badlogic.gdx.scenes.scene2d.ui.Button#getBackgroundDrawable()} to provide the same behavior.
     */
    public Color getColor(Button button) {
        if (button.isDisabled() && disabledColor != null) return disabledColor;
        if (button.isPressed()) {
            if (button.isChecked() && checkedDownColor != null && button.canCheck()) return checkedDownColor;
            if (downColor != null) return downColor;
        }
        if (button.isOver()) {
            if (button.isChecked() && button.canCheck()) {
                if (checkedOverColor != null) return checkedOverColor;
            } else {
                if (overColor != null) return overColor;
            }
        }
        boolean focused = button.hasKeyboardFocus();
        if (button.isChecked() && button.canCheck()) {
            if (focused && focusedCheckedColor != null) return focusedCheckedColor;
            if (checkedColor != null) return checkedColor;
            if (button.isOver() && overColor != null) return overColor;
        }
        if (focused && focusedColor != null) return focusedColor;
        return upColor;
    }

    /**
     * Copied & adapted from {@link com.badlogic.gdx.scenes.scene2d.ui.Button#getBackgroundDrawable()} to provide the same behavior.
     * Allows for color choice by Reactive State, which can be incomplete in the colors it provides.
     * Whatever it being used for is also not required to be a button.
     */
    public Color getColor(ReactiveState reactiveState) {
        switch( reactiveState ) {
            case DOWN: if( downColor != null) return downColor;
            case CHECKED: if( checkedColor != null) return checkedColor;
            case CHECKED_DOWN: if( checkedDownColor != null) return checkedDownColor;
            case CHECKED_OVER: if( checkedOverColor != null) return checkedOverColor;
            case OVER: if( overColor != null) return overColor;
            case DISABLED: if( disabledColor != null) return disabledColor;
            case FOCUSED: if( focusedColor != null) return focusedColor;
            case FOCUSED_CHECKED: if( focusedCheckedColor != null) return focusedCheckedColor;
            default: return upColor;
        }
    }
}
