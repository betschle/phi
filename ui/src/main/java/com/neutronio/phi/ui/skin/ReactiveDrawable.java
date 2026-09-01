package com.neutronio.phi.ui.skin;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.neutronio.phi.ui.commons.buttons.Button;

/**
 * A Drawable that changes its drawable on button state.
 */
public class ReactiveDrawable {

    public Drawable upRegion;
    public Drawable downRegion;
    public Drawable overRegion;
    public Drawable checkedRegion;
    public Drawable disabledRegion;
    public Drawable focusedRegion;

    public ReactiveDrawable copy() {
        ReactiveDrawable copy = new ReactiveDrawable();
        copy.upRegion = this.upRegion;
        copy.downRegion = this.downRegion;
        copy.overRegion = this.overRegion;
        copy.checkedRegion = this.checkedRegion;
        copy.disabledRegion = this.disabledRegion;
        copy.focusedRegion = this.focusedRegion;
        return copy;
    }

    public Drawable getDrawable(Button button) {
        if (button.isDisabled() && disabledRegion != null) return disabledRegion;
        if (button.isPressed()) {
            if (button.isChecked() && checkedRegion != null) return checkedRegion;
            if (downRegion != null) return downRegion;
        }
        if (button.isOver()) {
            if (button.isChecked()) {
                if (checkedRegion != null) return checkedRegion;
            } else {
                if (overRegion != null) return overRegion;
            }
        }
        boolean focused = button.hasKeyboardFocus();
        if (button.isChecked()) {
            if (focused && focusedRegion != null) return focusedRegion;
            if (checkedRegion != null) return checkedRegion;
            if (button.isOver() && overRegion != null) return overRegion;
        }
        if (focused && focusedRegion != null) return focusedRegion;
        return upRegion;
    }

//    /**
//     * Copied & adapted from {@link com.badlogic.gdx.scenes.scene2d.ui.Button#getBackgroundDrawable()} to provide the same behavior.
//     */
//    protected Color getColor(Button button) {
//        if (button.isDisabled() && disabledColor != null) return disabledColor;
//        if (button.isPressed()) {
//            if (button.isChecked() && checkedDownColor != null) return checkedDownColor;
//            if (downColor != null) return downColor;
//        }
//        if (button.isOver()) {
//            if (button.isChecked()) {
//                if (checkedOverColor != null) return checkedOverColor;
//            } else {
//                if (overColor != null) return overColor;
//            }
//        }
//        boolean focused = button.hasKeyboardFocus();
//        if (button.isChecked()) {
//            if (focused && focusedCheckedColor != null) return focusedCheckedColor;
//            if (checkedColor != null) return checkedColor;
//            if (button.isOver() && overColor != null) return overColor;
//        }
//        if (focused && focusedColor != null) return focusedColor;
//        return upColor;
//    }
}

