package com.neutronio.phi.ui.commons.buttons;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pools;
import com.neutronio.phi.sfx.ButtonSounds;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.CustomAction;
import com.neutronio.phi.ui.Tweening;
import com.neutronio.phi.ui.commons.SimplePanel;
import com.neutronio.phi.ui.tooltips.ToolTipListener;
import com.neutronio.phi.ui.tooltips.ToolTipManager;

/**
 * a class that is based on table layout but behaves like a button
 * Internally closely resembles a {@link BaseButton}. Fires a
 * {@link ChangeListener.ChangeEvent}
 * on mouse/click changes.
 */
public class CompositeButton
            extends
                SimplePanel
            implements
                Button {

    // TODO BUG background is not always centered in table layout!
    protected ClickListener clickListener;
    protected ButtonSelection buttonGroup;
    protected boolean isChecked;
    protected boolean isDisabled;
    protected boolean canCheck = true;
    protected boolean programmaticChangeEvents;
    /**  True if the button does a squish on mouse exit or enter */
    private boolean squishable = false;
    /** A custom button action. Will be executed after the button finished tweening. */
    protected CustomAction buttonAction;
    /** Specifies what tooltip class is shown and the data to display. */
    public ToolTipManager.ToolTipInfo toolTipInfo;
    /** The listener to react to tooltip events */
    public ToolTipListener toolTipListener;

    public static class CompositeButtonStyle extends AbstractPanelStyle {
        public ButtonSounds buttonSounds;
    }

    public CompositeButton(ComponentFactory factory, CompositeButtonStyle style, String backgroundStyle) {
        super(factory, style, backgroundStyle);
    }

    public CompositeButton(ComponentFactory factory, String buttonStyle, String backgroundStyle) {
        this(factory, factory.getSkin().get(buttonStyle, CompositeButtonStyle.class), backgroundStyle);
        this.clickListener = new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!event.getBubbles()) return false;
                if(getCompositeStyle().buttonSounds.soundPressed != null) {
                    componentFactory.playUISound(getCompositeStyle().buttonSounds.soundPressed);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(!event.getBubbles() || isDisabled()) return;
                if(getCompositeStyle().buttonSounds.soundRelease != null) {
                    componentFactory.playUISound(getCompositeStyle().buttonSounds.soundRelease);
                }
                if(!isChecked())  {
                    background.addAction(Tweening.getSquish(0.03f, buttonAction));
                }
                else {
                    background.addAction(Tweening.getSquish(0.01f, buttonAction));
                }
                // Use ButtonGroup to manage selection, if defined
                // if not defined, button shall manage itself and simply toggle itself
                if(buttonGroup != null) {
                    checkViaButtonGroup();
                } else {
                    setChecked(!isChecked(), true);
                }
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if(isDisabled() || !event.getBubbles()) return;
                super.enter(event, x, y, pointer, fromActor);
                if(fromActor == null) return;
                if(getCompositeStyle().buttonSounds.soundOver != null && pointer < 0 &&
                        fromActor != null && !fromActor.isDescendantOf(event.getListenerActor())) {
                    componentFactory.playUISound(getCompositeStyle().buttonSounds.soundOver);
                }
                if(isSquishable()) {
                    background.addAction(Tweening.getSquish(0.1f, null));
                }
                if(toolTipListener != null && toolTipInfo != null) toolTipListener.onEnterTooltip(toolTipInfo, x, y, fromActor);
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                if(toActor == null) return;
                if(toolTipListener != null && toolTipInfo != null && !toActor.isDescendantOf(event.getListenerActor()))
                    toolTipListener.onExitTooltip(toolTipInfo, x, y, toActor);
            }
        };
        this.addListener(this.clickListener);
    }

    public CompositeButton(ComponentFactory factory) {
        this(factory, "default", "outset-button");
    }

    protected CompositeButtonStyle getCompositeStyle() {
        return (CompositeButtonStyle) this.panelStyle;
    }

    protected void checkViaButtonGroup() {
        buttonGroup.updateCheckedState(this, !this.isChecked());
    }

    /**
     * Whether the Button squish tweens when hovering the mouse over it.
     * @return
     */
    public boolean isSquishable() {
        return squishable;
    }

    /**
     * Sets whether the Button squish tweens when hovering the mouse over it.
     * @param squishable
     */
    public void setSquishable(boolean squishable) {
        this.squishable = squishable;
    }

    /**
     * Centers the background such that it won't be misaligned on tweening.
     * To be called after pack()
     */
    public void centerBackground() {
        if(this.background == null) return;
        this.background.setSize(getWidth(), getHeight()); // same size as actor
        this.background.setAlign(Align.center);
        this.background.setOrigin(
                this.background.getWidth()/2f,
                this.background.getHeight()/2f);
    }

    public void setChecked(boolean isChecked, boolean fireEvent) {
        if (!this.canCheck) return;
        if (this.isChecked == isChecked) return;
        boolean changeState = this.isChecked != isChecked;
        this.isChecked = isChecked;
        if(changeState) {
            if (fireEvent) {
                ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);
                // Reset if cancelled
                if (fire(changeEvent)) this.isChecked = !isChecked;
                Pools.free(changeEvent);
            }
        }
    }

    public void setButtonAction(CustomAction buttonAction) {
        this.buttonAction = buttonAction;
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

    public void setDisabled(boolean disabled) {
        this.isDisabled = disabled;
    }

    @Override
    public boolean isDisabled() {
        return isDisabled;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    @Override
    public void setButtonGroup(ButtonSelection buttonGroup) {
        this.buttonGroup = buttonGroup;
    }

    @Override
    public void setChecked(boolean isChecked) {
        setChecked(isChecked, programmaticChangeEvents);
    }

    @Override
    public boolean canCheck() {
        return canCheck;
    }

    @Override
    public void setCanCheck(boolean canCheck) {
        this.canCheck = canCheck;
    }

    @Override
    public void setProgrammaticChangeEvents(boolean programmaticChangeEvents) {
        this.programmaticChangeEvents = programmaticChangeEvents;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.background.setColor( this.background.getStyle().color.getColor(this) );
        super.draw(batch, parentAlpha);
    }

    @Override
    public void setUserObject(Object userObject) {
        super.setUserObject(userObject);
        this.background.setUserObject(userObject);
    }
}
