package com.neutronio.phi.ui.commons;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.neutronio.phi.ui.ComponentFactory;

/**
 * Provides a basic class for {@link Table}-based AstraX GUI Components.
 * Only for components where advanced layouts are necessary. With increasing complexity
 * (+300 lines), such components should be broken down and constructed
 * via {@link ComponentFactory}.
 */
public abstract class AstraXComponent extends Table {

    protected ComponentFactory componentFactory;
    protected Background background;

    /**
     * Creates a component without background
     * @param componentFactory
     */
    public AstraXComponent(ComponentFactory componentFactory) {
        this(componentFactory, null);
    }

    /**
     * Pre-initializes a background
     * @param componentFactory
     * @param backgroundStyle null for no background
     */
    public AstraXComponent(ComponentFactory componentFactory, String backgroundStyle) {
        super(componentFactory.getSkin());
        this.componentFactory = componentFactory;
        if( backgroundStyle != null) {
            this.background = new Background(componentFactory.getSkin(), backgroundStyle);
            this.pad( this.background.getStyle().padding);
        }
    }

    public Background.BackgroundStyle getBackgroundStyle() {
        return this.background.style;
    }

    /**
     * Changes the background style for this component.
     * @param style
     */
    public void setBackgroundStyle(Background.BackgroundStyle style) {
        if( style == null && this.background != null) {
            this.background.remove();
            return;
        }
        if( this.background != null) {
            this.background.setStyle(style);
        } else {
            this.background = new Background(componentFactory.getSkin(), style);
            this.addActor(this.background);
            this.background.toBack();
        }
        this.pad(style.padding);
    }

    @Override
    public void setBackground(Drawable background) {
        throw new UnsupportedOperationException("Do not use this method. Use the Background class instead");
    }

    @Override
    public void setBackground(String drawableName) {
        throw new UnsupportedOperationException("Do not use this method. Use the Background class instead");
    }

    /**
     * Constructs/layouts this component after all its subcomponents
     * have been set. This only should be called once.
     * On multiple calls, this method should work without breaking
     * the component's function or its layout.
     */
    public void construct() {
        if( this.background != null) {
            this.addActor(this.background);
            this.background.toBack();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if( this.background != null) {
            this.background.setSize(getWidth(), getHeight());
        }
    }

    /**
     * Translates an identifier into a translated string in the
     * current locale.
     * @param identifier
     * @return
     */
    protected String getTranslation(String identifier) {
        return this.componentFactory.translate(identifier);
    }
}
