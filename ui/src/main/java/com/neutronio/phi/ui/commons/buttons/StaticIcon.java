package com.neutronio.phi.ui.commons.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.neutronio.astrax.AstraXApp;
import com.neutronio.astrax.ui.commons.tooltips.ToolTipListener;
import com.neutronio.astrax.ui.commons.tooltips.ToolTipManager;

/**
 * A simple static icon. Loosely based off {@link AstraXButton}
 */
public class StaticIcon extends Group {

    private Skin skin;

    private StaticIconStyle style;
    /** A base background image */
    private Image baseImage;
    /** The inline icon(image */
    private Image iconImage;
    /** Specifies what tooltip class is shown and the data to display. */
    public ToolTipManager.ToolTipInfo toolTipInfo;
    /** The listener to react to tooltip events */
    public ToolTipListener toolTipListener;

    public static class StaticIconStyle {
        public int width = 32;
        public int height = 32;
        public int padding = 10;

        private Drawable base;
        private Color baseColor;
        private Drawable icon;
        private Color iconColor;

    }

    public StaticIcon(Skin skin, String baseStyle) {
        this(skin, skin.get(baseStyle, StaticIconStyle.class));
    }

    public StaticIcon(Skin skin, StaticIconStyle style) {
        this.skin = skin;
        this.baseImage = new Image();
        this.addActor(this.baseImage);

        this.iconImage = new Image();
        this.addActor(this.iconImage);
        this.setStyle( style);
        this.toolTipListener = AstraXApp.astraX.getComponentFactory().getToolTipManager();

        // for now added to the icon but must be added to the whole component
        // for that size must be set properly. a problem for later
        this.iconImage.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if(toolTipListener != null && toolTipInfo != null) toolTipListener.onEnterTooltip(toolTipInfo, x, y, baseImage.getParent());
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                if(toolTipListener != null && toolTipInfo != null) toolTipListener.onExitTooltip(toolTipInfo, x, y, baseImage.getParent());
            }
        });
    }

    @Deprecated
    public StaticIcon(StaticIconStyle style) {
        this.baseImage = new Image();
        this.addActor(this.baseImage);

        this.iconImage = new Image();
        this.addActor(this.iconImage);
        this.setStyle( style );
        this.toolTipListener = AstraXApp.astraX.getComponentFactory().getToolTipManager();

        this.iconImage.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if(toolTipListener != null && toolTipInfo != null) toolTipListener.onEnterTooltip(toolTipInfo, x, y, baseImage.getParent());
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                if(toolTipListener != null && toolTipInfo != null) toolTipListener.onExitTooltip(toolTipInfo, x, y, baseImage.getParent());
            }
        });
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public StaticIconStyle getStyle() {
        return style;
    }

    /**
     * Sets the inline icon via drawable name
     * @param drawable must exist in the used skin
     */
    public void setInlineIcon(String drawable) {
        setInlineIcon(skin.getDrawable(drawable));
    }

    /**
     * Sets a custom color on the icon
     * @param color
     */
    public void setInlineIconColor(Color color) {
        this.iconImage.setColor(color);
    }
    /**
     * Changes the inline icon
     * @param iconImage
     */
    public void setInlineIcon(Drawable iconImage) {
        this.style.icon = iconImage;
        if( this.style.icon != null) {
            this.iconImage.setVisible(true);
            this.iconImage.setDrawable(this.style.icon);
            this.iconImage.setSize(this.style.width - this.style.padding*2, this.style.height - this.style.padding*2 );
            this.iconImage.setColor(this.style.iconColor);
            this.iconImage.setAlign(Align.center);
            this.iconImage.setOrigin(Align.center);
            this.iconImage.layout();
            this.iconImage.setPosition(
                    this.style.width/2f - (this.style.width - this.style.padding*2)/2f,
                    this.style.height/2f - (this.style.height - this.style.padding*2)/2f );
        } else {
            this.iconImage.setVisible(false);
        }
    }

    public void setStyle(StaticIconStyle style) {
        this.style = style;
        if( this.style.base != null) {
            this.baseImage.setVisible(true);
            this.baseImage.setDrawable(this.style.base);
            this.baseImage.setSize(this.style.width, this.style.height);
            this.baseImage.setColor(this.style.baseColor);
            this.baseImage.setAlign(Align.center);
            this.baseImage.layout();
            this.baseImage.setOrigin(
                    this.baseImage.getWidth() / 2f,
                    this.baseImage.getHeight() / 2f);
        } else {
            this.baseImage.setVisible(false);
        }
        this.setInlineIcon( this.style.icon );
        this.setSize(this.style.width, this.style.height);
    }


}
