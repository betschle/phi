package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.Background;

/**
 * A simple, horizontal progressbar. Features value tweening. Range is always 0-1
 */
public class ProgressBar extends WidgetGroup {

    private static final float MIN = 0f;
    private static final float MAX = 1f;

    private ComponentFactory componentFactory;
    private float value;
    private float targetValue;
    protected Background bar;
    protected Background background;
    protected ProgressBarStyle style;

    public static class ProgressBarStyle {
        Background.BackgroundStyle backgroundStyle;
        Background.BackgroundStyle barStyle;
    }

    public ProgressBar(ComponentFactory componentFactory) {
        this(componentFactory, "default");
    }

    public ProgressBar(ComponentFactory componentFactory, String style) {
        this(componentFactory, componentFactory.getSkin().get(style, ProgressBarStyle.class));
    }

    public ProgressBar(ComponentFactory componentFactory, ProgressBarStyle style) {
        super();
        this.componentFactory = componentFactory;
        this.bar = new Background(componentFactory.getSkin());
        this.background = new Background(componentFactory.getSkin());
        this.setStyle(style);

        this.addActor(this.background);
        this.addActor(this.bar);
    }

    public void setStyle(ProgressBarStyle style) {
        this.style = style;
        this.background.setStyle(style.backgroundStyle);
        this.bar.setStyle(style.barStyle);
    }

    /**
     * The currently stored value, as factor 0-1
     * @return
     */
    public float getValue() {
        return value;
    }

    /**
     * The target value, value converges to target value.
     * @return
     */
    public float getTargetValue() {
        return targetValue;
    }

    public void setValue(float value) {
        this.targetValue = MathUtils.clamp(value, MIN, MAX);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.value = this.value * 0.9f + this.targetValue * 0.1f;
        int barWidth = (int)MathUtils.clamp(getWidth()*this.value, this.bar.getDrawable().getMinWidth(), getWidth());
        this.bar.setSize(barWidth, getHeight());
        this.background.setSize(getWidth(), getHeight());
        super.draw(batch, parentAlpha);
    }

    @Override
    public float getPrefWidth() {
        return 150;
    }

    @Override
    public float getPrefHeight() {
        return 20;
    }

    @Override
    public float getMaxWidth() {
        return 300;
    }

    @Override
    public float getMaxHeight() {
        return 32;
    }
}
