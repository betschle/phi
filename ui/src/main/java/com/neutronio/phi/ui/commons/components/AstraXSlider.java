package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Pools;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.Background;

/**
 * Horizontal Slider. Range is always 0-1
 */
public class AstraXSlider extends WidgetGroup {

    public static final float MIN = 0f;
    public static final float MAX = 1f;

    private ComponentFactory componentFactory;
    private float value;
    private float targetValue;
    protected Background background;
    protected Background sliderKnob;
    protected AstraXSliderStyle style;

    public static class AstraXSliderStyle {
        Background.BackgroundStyle backgroundStyle;
        Background.BackgroundStyle knobStyle;
    }

    public AstraXSlider(ComponentFactory componentFactory, String style) {
        super();
        this.componentFactory = componentFactory;
        this.background = new Background(componentFactory.getSkin());
        this.sliderKnob = new Background(componentFactory.getSkin());
        this.setStyle( componentFactory.getSkin().get(style, AstraXSliderStyle.class));
        // confirmed to work
        this.addListener( new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                super.drag(event, x, y, pointer);
                value = (getDragX() - sliderKnob.getDrawable().getMinWidth()/2) / getWidth();
                targetValue = value;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                super.dragStop(event, x, y, pointer);
                value = (getDragX() - sliderKnob.getDrawable().getMinWidth()/2) / getWidth();
                targetValue = value;

                // fire change event
                ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);
                fire(changeEvent);
                Pools.free(changeEvent);
            }
        } );

        // confirmed to work
        this.addListener( new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if( x > 0) {
                    targetValue =  (x - sliderKnob.getDrawable().getMinWidth()/2) / getWidth();

                    // fire change event
                    ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);
                    fire(changeEvent);
                    Pools.free(changeEvent);
                }
            }
        });
        this.addActor(this.background);
        this.addActor(this.sliderKnob);
    }

    public AstraXSlider(ComponentFactory componentFactory) {
        this(componentFactory, "default");
    }

    public void setStyle(AstraXSliderStyle style) {
        this.style = style;
        this.background.setStyle(style.backgroundStyle);
        this.sliderKnob.setStyle(style.knobStyle);
        this.sliderKnob.setSize(this.sliderKnob.getDrawable().getMinWidth(), this.sliderKnob.getDrawable().getMinHeight());
    }

    /**
     * The currently stored value, as factor 0-1
     * @return
     */
    public float getValue() {
        return value;
    }

    /**
     * The target value which value converges to.
     * @return
     */
    public float getTargetValue() {
        return targetValue;
    }

    /**
     * Sets the value with interpolation
     * @param value
     */
    public void setValue(float value) {
        this.targetValue = MathUtils.clamp(value, MIN, MAX);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.value = this.value * 0.9f + this.targetValue * 0.1f;
        int knobPosition = (int)MathUtils.clamp(getWidth()*this.value, -this.sliderKnob.getDrawable().getMinWidth()/2, getWidth()-this.sliderKnob.getDrawable().getMinWidth());
        this.sliderKnob.setPosition(knobPosition,  getHeight()/2 - this.sliderKnob.getDrawable().getMinHeight()/2);
        this.background.setSize(getWidth(), getHeight());
        super.draw(batch, parentAlpha);
    }

    @Override
    public float getPrefWidth() {
        return 180;
    }

    @Override
    public float getPrefHeight() {
        return 20;
    }
}
