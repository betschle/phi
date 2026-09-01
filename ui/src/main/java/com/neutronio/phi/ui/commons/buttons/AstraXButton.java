package com.neutronio.phi.ui.commons.buttons;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.sfx.ButtonSounds;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.Tweening;
import com.neutronio.phi.ui.tooltips.ToolTipListener;
import com.neutronio.phi.ui.tooltips.ToolTipManager;
import com.neutronio.phi.ui.skin.ReactiveColor;
import com.neutronio.phi.ui.skin.ReactiveDrawable;

/**
 * A simple button that has customizable inline icon.
 */
public class AstraXButton extends BaseButton { // TODO rename to SimpleButton or IconButton

    // TODO Does not use Background Style, use it for icon and base?
    //  this does not work easily as Background is primarily meant for more static panels
    //  it might be worth it to create a ButtonBackground class with its own style (and reactive Background) used for button classes
    //  then rename current Background class to PanelBackground
    public static class AstraXButtonStyle {
        // TODO rename to iconWidth / iconHeight
        public int width = 32; // TODO BUG: for AstraXTextButton, this is both the size of the inner icon and also the component size
        public int height = 32; // TODO BUG: for AstraXTextButton, this is both the size of the inner icon and also the component size
        public int padding = 10; // TODO rename to iconPadding

        public ButtonSounds buttonSounds = new ButtonSounds();

        public ReactiveColor baseColor;
        public ReactiveDrawable baseDrawable;

        public ReactiveColor iconColor;
        public Drawable iconDrawable; // TODO should icon be a ReactiveDrawable too?

        public AstraXButtonStyle copy() {
            AstraXButtonStyle copy = new AstraXButtonStyle();
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

    protected ComponentFactory factory;
    protected AstraXButtonStyle style;

    /** A base background image */
    private Image baseImage;
    /** The inline icon(image */
    private Image iconImage; // TODO remove?
    /** Specifies what tooltip class is shown and the data to display. */
    public ToolTipManager.ToolTipInfo toolTipInfo;
    /** The listener to react to tooltip events */
    public ToolTipListener toolTipListener;

    public AstraXButton(ComponentFactory factory, String buttonStyle) {
        this(factory, null, buttonStyle);
    }

    public AstraXButton(ComponentFactory componentFactory, String iconDrawable, final String buttonStyle) {
        this(componentFactory, iconDrawable, componentFactory.getSkin().get(buttonStyle, AstraXButtonStyle.class).copy());
    }

    public AstraXButton(ComponentFactory componentFactory, String iconDrawable, AstraXButtonStyle buttonStyle) {
        super();
        this.factory = componentFactory;
        this.baseImage = new Image();
        this.addActor(this.baseImage);

        this.iconImage = new Image();
        this.iconImage.setTouchable(Touchable.disabled);
        this.addActor(this.iconImage);
        this.setStyle(buttonStyle);
        if(iconDrawable != null) {
            // override icon from style
            this.setInlineIcon(this.factory.getSkin().getDrawable(iconDrawable));
        }

        this.clickListener = new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (isDisabled()) return false;
                event.setBubbles(false); // do not forward to parent
                if( style.buttonSounds.soundPressed != null) {
                    factory.playUISound(style.buttonSounds.soundPressed);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isDisabled()) return;
                if( style.buttonSounds.soundRelease != null) {
                    factory.playUISound(style.buttonSounds.soundRelease);
                }
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if( style.baseDrawable.overRegion != null)
                    updateDrawable();
                if( style.buttonSounds.soundOver != null && pointer < 0
                    && fromActor != null && !fromActor.isDescendantOf(event.getListenerActor())) {
                    factory.playUISound(style.buttonSounds.soundOver);
                }
                if(toolTipListener != null && toolTipInfo != null) toolTipListener.onEnterTooltip(toolTipInfo, x, y, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                updateDrawable();
                if(toActor == null) return;
                if(toolTipListener != null && toolTipInfo != null && !toActor.isDescendantOf(event.getListenerActor()))
                    toolTipListener.onExitTooltip(toolTipInfo, x, y, toActor);
            }

            public void clicked (InputEvent event, float x, float y) {
                if(isDisabled() && !canCheck()) return;
                event.setBubbles(false); // do not forward to parent if button was clicked
                if(!isChecked() )  {
                    doSquish(3.125f, true);
                }
                else {
                    doSquish(1f, true);
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
        this.addListener(this.clickListener);
    }


    /**
     * Makes the button do a squishie
     * @param invokeAction if the button action should be invoked after tweening was finished
     * @param strength strength of the squish
     */
    public void doSquish(float strength, boolean invokeAction) {
        if( invokeAction ) {
            baseImage.addAction(Tweening.getSquish(0.8f * strength, buttonAction));
            iconImage.addAction(Tweening.getSquish(0.2f * strength));
        } else {
            baseImage.addAction(Tweening.getSquish(0.8f * strength));
            iconImage.addAction(Tweening.getSquish(0.2f * strength));
        }
    }

    @Override
    protected void updateDrawable() {
        this.baseImage.setDrawable(this.style.baseDrawable.getDrawable(this));
    }

    @Override
    public float getPrefWidth () {
        return style.width;
    }

    @Override
    public float getPrefHeight () {
        return style.height;
    }

    public void setStyle(AstraXButtonStyle style) {
        this.style = style;
        this.baseImage.setDrawable(this.style.baseDrawable.getDrawable(this));
        this.baseImage.setSize(this.style.width, this.style.height);
        this.baseImage.setColor(this.style.baseColor.getColor(this));
        this.baseImage.setAlign(Align.center);
        this.baseImage.setOrigin(
            this.baseImage.getWidth()/2f,
            this.baseImage.getHeight()/2f);
        this.baseImage.layout();
        if(style.iconDrawable != null) {
            this.setInlineIcon(style.iconDrawable);
        }
        this.setSize(this.style.width, this.style.height);
    }

    public Image getInlineIcon() {
        return iconImage;
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
            this.iconImage.setSize(this.style.width - this.style.padding*2, this.style.height - this.style.padding*2 );
            this.iconImage.setColor(this.style.iconColor.getColor(this));
            this.iconImage.setAlign(Align.center);
            this.iconImage.setOrigin(Align.center);
            this.iconImage.setPosition(
                this.style.width/2f - (this.style.width - this.style.padding*2)/2f,
                this.style.height/2f - (this.style.height - this.style.padding*2)/2f );
            this.iconImage.layout();
        } else {
            this.iconImage.setVisible(false);
        }
    }

    /**
     * Toggles the visibility of the inline icon.
     * @param visible
     */
    public void toggleInlineIconVisibility( boolean visible) {
        this.iconImage.setVisible(visible);
    }

    public void addActionToIcon(Action action) {
        this.iconImage.addAction(action);
    }

    public void clearActionsFromIcon() {
        this.iconImage.clearActions();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        this.baseImage.setColor(this.style.baseColor.getColor(this));
        if(this.style.iconColor != null) {
            this.iconImage.setColor(this.style.iconColor.getColor(this));
        }
    }

    public AstraXButtonStyle getStyle() {
        return style;
    }

    @Override
    public void setUserObject(Object userObject) {
        super.setUserObject(userObject);
        this.baseImage.setUserObject(userObject);
    }
}
