package com.neutronio.phi.ui.commons.buttons;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Pools;
import com.neutronio.phi.sfx.ButtonSounds;
import com.neutronio.phi.ui.CustomAction;

/**
 * A base class for any buttons, providing methods and fields for general button implementations.
 */
public abstract class BaseButton extends WidgetGroup implements Button {

    // TODO add these fields to spare some duplicate code to TradeItemButton & TargetItem
    protected ClickListener clickListener;
    protected ButtonSelection buttonGroup;
    protected ButtonSounds buttonSounds = new ButtonSounds();
    protected boolean isChecked;
    protected boolean isDisabled;
    protected boolean canCheck = true;
    protected boolean programmaticChangeEvents;
    /** A custom button action. Will be executed after the button finished tweening. */
    protected CustomAction buttonAction;

    public void setButtonSounds(ButtonSounds buttonSounds) {
        this.buttonSounds = buttonSounds;
    }

    @Override
    public void setButtonGroup(ButtonSelection buttonGroup) {
        this.buttonGroup = buttonGroup;
    }

    @Override
    public boolean canCheck() {
        return canCheck;
    }

    public void setCanCheck(boolean canCheck) {
        this.canCheck = canCheck;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    @Override
    public void setChecked(boolean isChecked) {
        this.setChecked(isChecked, this.programmaticChangeEvents);
    }

    protected void checkViaButtonGroup() {
        this.getButtonGroup().updateCheckedState(this, !this.isChecked());
    }

    public void setChecked(boolean isChecked, boolean fireEvent) {
        if (!this.canCheck) return;
        if (this.isChecked == isChecked) return;
        boolean changeState = this.isChecked != isChecked;
        this.isChecked = isChecked;
        if(changeState) {
            this.updateDrawable();
            if (fireEvent) {
                ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);
                // Reset if cancelled
                if (fire(changeEvent)) this.isChecked = !isChecked;
                Pools.free(changeEvent);
            }
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public boolean isPressed() {
        if (this.clickListener == null) return false;
        return clickListener.isVisualPressed();
    }

    @Override
    public boolean isOver() {
        if (this.clickListener == null) return false;
        return clickListener.isOver();
    }

    public ClickListener getClickListener() {
        return clickListener;
    }

    @Override
    public boolean isDisabled() {
        return isDisabled;
    }

    /**
     * When true, the button will not toggle {@link #isChecked()} when clicked and will not fire a {@link ChangeListener.ChangeEvent}.
     */
    public void setDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    protected void updateDrawable() {

    }

    /**
     * If false, {@link #setChecked(boolean)} and {@link #toggle()} will not fire {@link ChangeListener.ChangeEvent}. The event will only be
     * fired only when the user clicks the button
     */
    public void setProgrammaticChangeEvents(boolean programmaticChangeEvents) {
        this.programmaticChangeEvents = programmaticChangeEvents;
    }

    public ButtonSelection getButtonGroup() {
        return buttonGroup;
    }

    public CustomAction getButtonAction() {
        return buttonAction;
    }

    public void setButtonAction(CustomAction buttonAction) {
        this.buttonAction = buttonAction;
    }
}

