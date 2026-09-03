package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.Tweening;
import com.neutronio.phi.ui.commons.AstraXComponent;
import com.neutronio.phi.ui.commons.buttons.StaticIcon;

/**
 * A Label with an icon.
 */
public class IconLabel extends AstraXComponent {

    protected Label label; // "naked" label
    protected StaticIcon staticIcon;
    protected IconLabelStyle iconLabelStyle;

    public static class IconLabelStyle {
        public Label.LabelStyle labelStyle;
        public StaticIcon.StaticIconStyle iconStyle;
        /** if the icon should be placed before the label text */
        public boolean iconBeforeText = false;
        public int labelMinWidth = 200;
        public int iconPadding = 10;
    }

    public IconLabel(ComponentFactory factory) {
        this(factory, "default", null);
    }

    public IconLabel(ComponentFactory factory, String styleName) {
        this(factory, styleName, null);
    }

    public IconLabel(ComponentFactory factory, String styleName, String backgroundName) {
        this(factory, factory.getSkin().get(styleName, IconLabelStyle.class), backgroundName);
    }

    public IconLabel(ComponentFactory factory, IconLabelStyle style, String backgroundName) {
        super(factory, backgroundName);
        this.staticIcon = new StaticIcon(factory, style.iconStyle);
        this.staticIcon.setSkin(factory.getSkin());
        this.staticIcon.setOrigin( this.staticIcon.getWidth()/2f, this.staticIcon.getHeight()/2f );

        this.label = new Label("", style.labelStyle);
        this.label.setAlignment(Align.center, Align.center);

        this.iconLabelStyle = style;

        this.flipOrder(style.iconBeforeText);
    }

    public IconLabelStyle getStyle() {
        return iconLabelStyle;
    }

    public void setFixedLabelWidth(float width) {
        this.getCell(this.label).maxWidth(width).minWidth(width).width(width);
    }
    /**
     * Adds squish tweening to this icon.
     */
    public void squishIcon() {
        this.staticIcon.addAction(Actions.repeat(3, Tweening.getSquish(3)));
    }

    public void setLabelAlign(int labelAlign) {
        getCell(this.label).align(labelAlign);
        this.label.setAlignment(labelAlign);
        this.label.setAlignment(labelAlign, labelAlign);
    }

    public void setStyle(IconLabelStyle iconLabelStyle) {
        this.iconLabelStyle = iconLabelStyle;
        this.staticIcon.setStyle(iconLabelStyle.iconStyle);
        this.label.setStyle(iconLabelStyle.labelStyle);
    }

    public void setIconVisible(boolean visible) {
        this.staticIcon.setVisible(visible);
    }

    /**
     * Sets a custom icon and text.
     * @param iconDrawable
     * @param text
     */
    public void set(String iconDrawable, String text) {
        this.staticIcon.setInlineIcon(iconDrawable);
        this.label.setText(text);
    }

    /**
     * Sets the custom icon
     * @param iconDrawable
     */
    public void setIcon(String iconDrawable) {
        this.staticIcon.setInlineIcon(iconDrawable);
    }

    /**
     * Flips the spatial order of icon and text label
     */
    public void flipOrder(boolean iconBeforeText) {
        if(iconBeforeText) {
            this.clearChildren();
            this.construct();
            this.add(this.staticIcon).padRight(this.iconLabelStyle.iconPadding);
            this.label.setAlignment(Align.left, Align.left);
            this.add(this.label).minSize(this.iconLabelStyle.labelMinWidth, 20).align(Align.left); //.fill();
            this.pack();
        } else {
            this.clearChildren();
            this.construct();
            this.add(this.label).minSize(this.iconLabelStyle.labelMinWidth, 20).align(Align.center); //.fill();
            this.add(this.staticIcon).padLeft(this.iconLabelStyle.iconPadding);
            this.pack();
        }
    }

    public Label getLabel() {
        return label;
    }

    public StaticIcon getStaticIcon() {
        return staticIcon;
    }

    /**
     * Sets a custom color on the icon
     * @param color
     */
    public void setIconColor(Color color) {
        this.staticIcon.setInlineIconColor(color);
    }

    /**
     * Only sets the text.
     * @param text
     */
    public void setText(String text) {
        this.label.setText(text);
    }
}
