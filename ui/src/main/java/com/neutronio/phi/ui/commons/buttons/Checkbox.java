package com.neutronio.phi.ui.commons.buttons;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.sfx.ButtonSounds;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.Tweening;

/**
 * Button with a knob that moves depending on checked state
 */
public class Checkbox extends BaseButton {

    protected ComponentFactory factory;
    protected AstraXButton.AstraXButtonStyle style;

    /** A base background image */
    private Image baseImage;
    /** The inline icon(image */
    private Image knobImage;

    public Checkbox(ComponentFactory componentFactory,  String buttonStyle) {
        super();
        this.factory = componentFactory;
        this.baseImage = new Image();
        this.addActor(this.baseImage);

        this.knobImage = new Image();
        this.knobImage.setTouchable(Touchable.disabled);
        this.addActor(this.knobImage);

        this.setButtonSounds(componentFactory.getSkin().get("checkbox", ButtonSounds.class));
        AstraXButton.AstraXButtonStyle iconStyle = this.factory.getSkin().get(buttonStyle, AstraXButton.AstraXButtonStyle.class).copy();
        this.setStyle(iconStyle);
        this.clickListener = new ClickListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if( style.buttonSounds.soundOver != null && pointer < 0 &&
                        fromActor != null && !fromActor.isDescendantOf(event.getListenerActor())) {
                    factory.playUISound(style.buttonSounds.soundOver);
                }
            }

            public void clicked (InputEvent event, float x, float y) {
                if (isDisabled() && !canCheck()) return;
                event.setBubbles(false); // do not forward to parent if button was clicked
                baseImage.addAction(Tweening.getSquish(2.5f, getButtonAction()));
                setChecked(!isChecked(), true);
            }
        };
        this.addListener( this.clickListener );
    }

    public void setStyle(AstraXButton.AstraXButtonStyle style) {
        this.style = style;
        this.baseImage.setDrawable(this.style.baseDrawable.getDrawable(this));
        this.baseImage.setSize(this.style.width, this.style.height);
        this.baseImage.setColor(this.style.baseColor.getColor(this));
        this.baseImage.setAlign(Align.center);
        this.baseImage.setOrigin(
                this.baseImage.getWidth()/2f,
                this.baseImage.getHeight()/2f);
        this.setInlineIcon(style.iconDrawable);

        this.setSize(this.style.width, this.style.height);
    }

    /**
     * Changes the inline icon
     * @param iconImage
     */
    private void setInlineIcon(Drawable iconImage) {
        this.style.iconDrawable = iconImage;
        this.knobImage.setVisible(true);
        this.knobImage.setDrawable(this.style.iconDrawable);
        this.knobImage.setSize(this.knobImage.getDrawable().getMinWidth() - this.style.padding*2,
                this.knobImage.getDrawable().getMinHeight() - this.style.padding*2 );
        this.knobImage.setColor(this.style.iconColor.getColor(this));
        this.knobImage.setAlign(Align.center);
        this.knobImage.setOrigin(Align.center);
        this.knobImage.setPosition(
                this.style.width/2f - (this.style.width - this.style.padding*2)/2f,
                this.style.height/2f - (this.style.height - this.style.padding*2)/2f );
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        this.knobImage.setPosition(
                width/2f - (this.style.width - this.style.padding*2)/2f,
                height/2f - (this.style.height - this.style.padding*2)/2f );
    }

    @Override
    public float getPrefWidth () {
        return style.width;
    }

    @Override
    public float getPrefHeight () {
        return style.height;
    }


    @Override
    public void setChecked(boolean isChecked, boolean fireEvent) {
        if(fireEvent) {
            if (isChecked) {
                // play sound only when event is fired = user input
                if (style.buttonSounds.soundPressed != null) {
                    factory.playUISound(style.buttonSounds.soundPressed);
                }
                // tween to position
                this.knobImage.addAction(Actions.moveTo(
                        this.baseImage.getWidth() - this.knobImage.getWidth() - this.style.padding, this.knobImage.getY(),
                        0.07f));
            } else {
                // play sound only when event is fired = user input
                if (style.buttonSounds.soundRelease != null) {
                    factory.playUISound(style.buttonSounds.soundRelease);
                }
                // tween to position
                this.knobImage.addAction(Actions.moveTo(this.style.padding, this.knobImage.getY(), 0.2f));
            }
        } else {
            // move knob instantly to pos
            if (isChecked) {
                this.knobImage.addAction(Actions.moveTo(
                        this.baseImage.getWidth() - this.knobImage.getWidth() - this.style.padding, this.knobImage.getY()));
            } else {
                this.knobImage.addAction(Actions.moveTo(this.style.padding, this.knobImage.getY()));
            }
        }
        super.setChecked(isChecked, fireEvent);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        this.baseImage.setColor(this.style.baseColor.getColor(this));
        this.knobImage.setColor(this.style.iconColor.getColor(this));
    }

    @Override
    public void setUserObject(Object userObject) {
        super.setUserObject(userObject);
        this.baseImage.setUserObject(userObject);
    }
}
