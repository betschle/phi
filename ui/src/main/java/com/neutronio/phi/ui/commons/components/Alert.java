package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.Tweening;

/**
 * A text with a message and icon that vanishes
 * after a while.
 */
public class Alert extends IconLabel {

    // TODO only change background and text color here
    public Alert(ComponentFactory componentFactory, String style) {
        super(componentFactory, style, "");
        this.setLabelAlign(Align.left);
        this.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                hide();
            }
        });
    }

    public Alert(ComponentFactory componentFactory, IconLabelStyle style) {
        // TODO bug: background missing
        super(componentFactory);
        this.setStyle(style);
        this.setLabelAlign(Align.left);
        this.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                hide();
            }
        });
    }
    /**
     * Causes the breadcrumb to fade in. This method is not supposed to be called twice in a row!
     */
    public void show(float shopDuration) {
        this.addAction( Tweening.getGrowlAnimation(shopDuration) );
    }

    /**
     * Stops all actions, hides the alert
     */
    public void hide() {
        this.clearActions();
        this.addAction( Actions.sequence(
                Actions.alpha(0f, 0.4f),
                Actions.touchable(Touchable.disabled),
                Actions.hide())
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    /**
     * Sets a text to this alert.
     * @param text
     */
    public void setText(String text) {
        this.label.setText(text);
    }
}
