package com.neutronio.phi.ui.commons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.neutronio.phi.ui.skin.ReactiveColor;

/**
 * Background with an ReactiveColor. Meant as background class
 * for panels and therefore does not react much to changes.
 */
public class Background extends Image { // TODO rename to PanelBackground?

    protected BackgroundStyle style;
    /** The currently set reactive color. Can be different from getColor()*/
    protected Color currentColor;
    private ReactiveColor.ReactiveState state = ReactiveColor.ReactiveState.UP;

    public static class BackgroundStyle {
        public ReactiveColor color;
        public Drawable background;
        /** Inner Padding, used by components implementing this class, e.g. AbstractPanel*/
        public int padding = 0;

        public BackgroundStyle copy() {
            BackgroundStyle copy = new BackgroundStyle();
            copy.background = this.background;
            copy.color = this.color.copy();
            copy.padding = this.padding;
            return copy;
        }
    }

    public Background(Skin skin) {
        this(skin, "default");
    }

    public Background(BackgroundStyle backgroundStyle) {
        super();
        this.setStyle(backgroundStyle);
    }

    public Background(Skin skin, BackgroundStyle backgroundStyle) {
        super();
        this.setStyle(backgroundStyle);
    }

    public Background(Skin skin, String style) {
        super();
        this.setStyle(skin.get(style, BackgroundStyle.class));
    }

    /**
     * Specifies the reactive state of the background. Changes its color doing so, depending
     * on the style chosen. Use this for label marking mechanisms
     * @param state
     */
    public void setState(ReactiveColor.ReactiveState state) {
        this.state = state;
        this.currentColor = style.color.getColor(this.state);
        this.setColor(this.currentColor);
    }

    public ReactiveColor.ReactiveState getState() {
        return state;
    }

    /**
     *
     * @return The currently set reactive color. Can be different from getColor(), as it does not include parent
     *         alpha
     */
    public Color getCurrentColor() {
        return currentColor;
    }

    public BackgroundStyle getStyle() {
        return style;
    }

    public void setStyle(BackgroundStyle style) {
        this.style = style;
        this.setDrawable(style.background);
        this.currentColor = style.color.getColor(this.state);
        this.setColor(this.currentColor);
    }

}
