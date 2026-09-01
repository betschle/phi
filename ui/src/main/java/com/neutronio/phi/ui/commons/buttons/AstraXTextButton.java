package com.neutronio.phi.ui.commons.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.Tweening;
import com.neutronio.phi.ui.tooltips.ToolTipListener;
import com.neutronio.phi.ui.tooltips.ToolTipManager;

/**
 * A Text Button that has a fixed size from style, but
 * grows automatically with padding if it needs to be larger.
 * This behavior is not present in GDX implementations. The
 * described behavior is typically present in GDX table implementations
 * and for this reason, this text button might be bloaty.
 */
public class AstraXTextButton extends BaseButton { // TODO rename to TextButton

    // TODO create a test for different usecases:
    //  text larger than button
    //  text smaller than button
    //  all of them in table vs standalone

    // TODO Much better solution with container, still has minor problems with size:
    //  Does not go well with table.add(button).fill(), that causes baseImage to diverge from actual size
    //  temporary fix is to simply set the defined size from style

    // TODO I think the issue is  pref/min/max sizes. Ensure I can set these to different
    //  values (userdefined, from style, adapt from container) as needed

    // TODO simplify this component
    // TODO cleanup style required
    protected ComponentFactory factory;
    protected AstraXTextButtonStyle style;
    /** A base background image */
    private Image baseImage;
    /** The inline icon(image */
    private Image iconImage;

    /** True if the button does a squish on mouse exit or enter */
    private boolean squishable = true;
    /** True if the button does a squish on click */
    private boolean squishOnClick = true;
    protected Container<Label> container;
    protected Label label;
    /** Specifies what tooltip class is shown and the data to display. */
    public ToolTipManager.ToolTipInfo toolTipInfo;
    /** The listener to react to tooltip events */
    public ToolTipListener toolTipListener;

    public static class AstraXTextButtonStyle extends AstraXButton.AstraXButtonStyle {
        public String labelStyle;
        public int labelPadding;
        public int labelAlignment = Align.center;
        public int labelMinWidth = 0; // TODO remove
        public int labelMinHeight = 0; // TODO remove

        public AstraXTextButtonStyle copy() {
            AstraXTextButtonStyle copy = new AstraXTextButtonStyle();
            copy.labelStyle = this.labelStyle;
            copy.labelPadding = this.labelPadding;
            copy.width = this.width;
            copy.height = this.height;
            copy.padding = this.padding;
            copy.buttonSounds.soundRelease = this.buttonSounds.soundRelease;
            copy.buttonSounds.soundPressed = this.buttonSounds.soundPressed;
            copy.buttonSounds.soundOver = this.buttonSounds.soundOver;

            copy.baseDrawable = this.baseDrawable;
            if( this.baseColor != null) {
                copy.baseColor = this.baseColor.copy();
            }

            copy.iconDrawable = this.iconDrawable;
            if( this.iconColor != null) {
                copy.iconColor = this.iconColor.copy();
            }
            return copy;
        }
    }
    @Deprecated
    public AstraXTextButton(ComponentFactory componentFactory, String buttonStyle) {
        this(componentFactory, "", null, buttonStyle);
    }

    public AstraXTextButton(ComponentFactory componentFactory, String icon, String buttonStyle) {
        this(componentFactory, "", icon, buttonStyle);
    }

    public AstraXTextButton(ComponentFactory componentFactory, String text, String iconDrawable, AstraXTextButtonStyle buttonStyle) {
        super();
        this.factory = componentFactory;
        this.baseImage = new Image();
        this.baseImage.setFillParent(true); // activate this to enable fill in table layouts
        this.addActor(this.baseImage);

        this.iconImage = new Image();
        this.iconImage.setTouchable(Touchable.disabled);
        this.addActor(this.iconImage);

        this.label = new Label("", componentFactory.getSkin());
        this.label.setTouchable(Touchable.disabled);

        this.container = new Container<>(this.label);
        this.container.pack();
        this.container.setFillParent(true); // activate this to enable fill in table layouts
        this.container.align(Align.center);
        this.addActor(this.container);
        this.setStyle(buttonStyle);
        if( iconDrawable != null) {
            this.setInlineIcon(factory.getSkin().getDrawable(iconDrawable));
        } else {
            this.setInlineIcon(null);
        }
        this.setText(text);

        this.clickListener = new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(isDisabled) return false; // TODO error sound when disabled?
                event.setBubbles(false); // do not forward to parent if button was clicked
                if(style.buttonSounds.soundPressed != null) {
                    factory.playUISound(style.buttonSounds.soundPressed);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(isDisabled ) return; // TODO error sound when disabled?
                // only play this when the hovering actor is self
                if(style.buttonSounds.soundRelease != null ) {
                    factory.playUISound(style.buttonSounds.soundRelease);
                }
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if(isDisabled) return;
                if(style.baseDrawable.overRegion != null)
                    updateDrawable();
                if(style.buttonSounds.soundOver != null && pointer < 0 &&
                        fromActor != null && !fromActor.isDescendantOf(event.getListenerActor())) {
                    factory.playUISound(style.buttonSounds.soundOver);
                }
                if(isSquishable()) {
                    baseImage.addAction(Tweening.getSquish(1.5f, null));
                    iconImage.addAction(Tweening.getSquish(0.5f, null));
                    label.addAction(Tweening.getSquish(0.5f, null));
                }
                if(toolTipListener != null && toolTipInfo != null) toolTipListener.onEnterTooltip(toolTipInfo, x, y, baseImage.getParent());
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                if(isSquishable()) {
                    // scale back on mouse exit
                    baseImage.addAction(Actions.scaleTo(1, 1, 0.4f, Interpolation.bounce));
                    iconImage.addAction(Actions.scaleTo(1, 1, 0.4f, Interpolation.bounce));
                    label.addAction(Actions.scaleTo(1, 1, 0.4f, Interpolation.bounce));
                }
                if(toolTipListener != null && toolTipInfo != null) toolTipListener.onExitTooltip(toolTipInfo, x, y, baseImage.getParent());
                updateDrawable();
            }

            public void clicked (InputEvent event, float x, float y) {
                if(isDisabled) return;
                event.setBubbles(false); // do not forward to parent if button was clicked
                if(!isChecked)  {
                    if(squishOnClick) {
                        baseImage.addAction(Tweening.getSquish(2.5f, getButtonAction()));
                        iconImage.addAction(Tweening.getSquish(1f, null));
                        label.addAction(Tweening.getSquish(1f, null));
                    }
                }
                else {
                    if(squishOnClick) {
                        baseImage.addAction(Tweening.getSquish(0.8f, getButtonAction()));
                        iconImage.addAction(Tweening.getSquish(0.2f));
                        label.addAction(Tweening.getSquish(0.2f));
                    }
                }
                if(!canCheck) return;
                // Use ButtonGroup to manage selection, if defined
                // if not defined, button shall manage itself and simply toggle itself
                if(getButtonGroup() != null) {
                    checkViaButtonGroup();
                } else {
                    setChecked(!isChecked(), true);
                }
            }
        };
        this.addListener( this.clickListener );
    }

    public AstraXTextButton(ComponentFactory componentFactory, String text, String iconDrawable, String buttonStyle) {
        this(componentFactory, text, iconDrawable, componentFactory.getSkin().get(buttonStyle, AstraXTextButtonStyle.class).copy());
    }

    public void setStyle(AstraXTextButtonStyle style) {
        this.style = style;
        this.label.setStyle( this.factory.getSkin().get( style.labelStyle, Label.LabelStyle.class));
        this.label.setAlignment(style.labelAlignment, Align.center); // TODO I cannot easily add label alignment to skin
        this.container.pad(this.style.labelPadding);
        this.container.minSize(this.style.width, this.style.height);

        this.baseImage.setDrawable(this.style.baseDrawable.getDrawable(this));
        this.baseImage.setColor(this.style.baseColor.getColor(this));
        if(style.iconDrawable != null) {
            this.setInlineIcon(style.iconDrawable);
        }
        this.pack();
    }

    @Override
    public void pack() {
        super.pack();
        this.baseImage.setOrigin(
                this.getWidth()/2f,
                this.getHeight()/2f);
        this.baseImage.setAlign(Align.center);
    }

    @Override
    protected void updateDrawable() {
        this.baseImage.setDrawable(this.style.baseDrawable.getDrawable(this));
    }

    /**
     * Whether the buttons squishes on click.
     */
    public boolean isSquishOnClick() {
        return squishOnClick;
    }

    /**
     * Sets whether the buttons squishes on click.
     * @param squishOnClick
     */
    public void setSquishOnClick(boolean squishOnClick) {
        this.squishOnClick = squishOnClick;
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

    public void setTextAlign(int align) {
        this.label.setAlignment(align);
    }

    public AstraXTextButtonStyle getStyle() {
        return style;
    }

    public void setText(String text) {
        this.label.setText(text);
        this.container.pack();
        float width = this.container.getWidth();
        float height = this.container.getHeight();

        this.pack();
    }

    public String getText() {
        return this.label.getText().toString();
    }

    public Image getInlineIcon() {
        return iconImage;
    }

    @Override
    public float getMaxWidth() {
        return this.container.getMaxWidth();
    }

    @Override
    public float getMaxHeight() {
        return this.container.getMaxHeight();
    }

    @Override
    public float getMinWidth() {
        return this.container.getMinWidth();
    }

    @Override
    public float getMinHeight() {
        return this.container.getMinHeight();
    }

    @Override
    public float getPrefWidth() {
        return this.container.getPrefWidth();
    }

    @Override
    public float getPrefHeight() {
        return this.container.getPrefHeight();
    }

    /**
     * Changes the inline icon
     * @param iconImage
     */
    public void setInlineIcon(Drawable iconImage) {
        if( iconImage != null ) {
            this.style.iconDrawable = iconImage;
            this.iconImage.setVisible(true);
            this.iconImage.setDrawable(this.style.iconDrawable);
            this.iconImage.setSize(this.style.width - this.style.padding, this.style.height - this.style.padding );

            if( this.style.iconColor != null) {
                this.iconImage.setColor(this.style.iconColor.getColor(this));
            } else {
                this.iconImage.setColor(Color.WHITE);
            }

            this.iconImage.setAlign(Align.center);
            this.iconImage.setOrigin(Align.center);
            this.iconImage.setPosition(
                    this.style.width/2f - (this.style.width - this.style.padding)/2f,
                    this.style.height/2f - (this.style.height - this.style.padding)/2f );
        } else {
            this.iconImage.setVisible(false);
        }
    }


    public void addActionToIcon(Action action) {
        this.iconImage.addAction(action);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        this.baseImage.setColor(this.style.baseColor.getColor(this));
        if( this.style.iconColor != null) {
            this.label.setColor(this.style.iconColor.getColor(this));
        }
        if( this.style.iconDrawable != null && this.style.iconColor != null) {
            this.iconImage.setColor(this.style.iconColor.getColor(this));
        }
    }

    @Override
    public void setUserObject(Object userObject) {
        super.setUserObject(userObject);
        this.baseImage.setUserObject(userObject);
    }
}
