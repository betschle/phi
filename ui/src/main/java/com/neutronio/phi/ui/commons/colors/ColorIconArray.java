package com.neutronio.phi.ui.commons.colors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.components.IconPicker;
import com.neutronio.phi.ui.commons.components.SimplePanel;

import java.util.Collection;

/**
 * Works like a selectbox or {@link IconPicker}. Does not offer pagination (yet)
 * Allows
 */
public class ColorIconArray extends SimplePanel {

    // TODO how to deal with no selection?
    protected ComponentFactory factory;

    public static class Style {
        public String soundSelect = "zoom:in";
        public Drawable entryDrawable;
        public Drawable overDrawable;
        public String overDrawableColor;
        /** entry icon width */
        public int width = 8;
        /** entry icon width */
        public int height = 8;
    }

    protected Style style;
    protected Image[] icons;
    /** The maximum icon count per row */
    protected int iconsPerRow = 4;
    protected int selectedIndex = -1;
    /** Floats on top selected icon */
    protected Image overIcon;
    /** Currently selected icon to obtain color from */
    protected Image selectedIcon;

    private ColorIconArrayListener listener;

    public interface ColorIconArrayListener {
        /**
         * Invoked when a color was clicked on this array component.
         * @param index
         * @param color
         */
        void onColorSelected(int index, Color color);
    }

    public ColorIconArray(ComponentFactory componentFactory, String backgroundStyle, Style style) {
        super(componentFactory, "buttons", backgroundStyle);
        this.pad(7); // override background padding

        this.factory = componentFactory;
        this.overIcon = new Image();
        this.overIcon.setTouchable(Touchable.disabled);
        this.overIcon.setOrigin(Align.center);
        this.addActor(this.overIcon);
        setStyle(style);
    }

    public ColorIconArray(ComponentFactory componentFactory, String backgroundStyle, String style) {
        this(componentFactory, backgroundStyle, componentFactory.getSkin().get(style, Style.class));
    }

    public ColorIconArray(ComponentFactory componentFactory, String backgroundStyle) {
        this(componentFactory, backgroundStyle, componentFactory.getSkin().get("default", Style.class));
    }

    public void setStyle(Style style) {
        this.style = style;
        this.overIcon.setDrawable(style.overDrawable);
        this.overIcon.setSize(style.width, style.height);
        this.overIcon.setOrigin(Align.center);
        this.overIcon.setColor(factory.getSkin().getColor(style.overDrawableColor));

        if(this.icons == null) return;
        for(Image icon : this.icons) {
            icon.setDrawable(style.entryDrawable);
            icon.setSize(style.width, style.height);
        }
        this.moveOverIconToSelectedIcon(this.selectedIcon, true);
        this.overIcon.toFront();
    }

//    @Override
//    public Style getStyle() {
//        return style;
//    }

    public void setIconsPerRow(int iconPerRow) {
        this.iconsPerRow = iconPerRow;
    }

    /**
     * Also creates the buttons
     * @param colors
     */
    public void setColorPalette(Collection<Color> colors) {
        this.icons = new Image[colors.size()];

        int i = 0;
        for( Color color : colors) {
            this.icons[i] = new Image(this.style.entryDrawable);
            this.icons[i].setSize(style.width, style.height);
            this.icons[i].setUserObject(i);
            this.icons[i].setColor(color);
            this.icons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if( !hasActions() ) {
                        Image selectedImage = (Image) event.getTarget();
                        moveOverIconToSelectedIcon(selectedImage, false);
                        if( getListener() != null) getListener().onColorSelected( (int) selectedImage.getUserObject(),selectedImage.getColor());
                        factory.playUISound(style.soundSelect);
                    }
                }
            });
            this.addValueCentered(this.icons[i]).pad(1f).size(style.width, style.height);
            if( (i+1) % this.iconsPerRow == 0) {
                this.row();
            }
            i++;
        }
        this.pack();
        this.moveOverIconToSelectedIcon(this.icons[0], true);
        this.overIcon.toFront();
    }

    public void setColorPalette(Color... colors) {
        this.icons = new Image[colors.length];

        for( int i =0;i < this.icons.length; i++) {
            this.icons[i] = new Image(this.style.entryDrawable);
            this.icons[i].setSize(style.width, style.height);
            this.icons[i].setUserObject(i);
            this.icons[i].setColor(colors[i]);
            this.icons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if( !hasActions() ) {
                        Image selectedImage = (Image) event.getTarget();
                        moveOverIconToSelectedIcon(selectedImage, false);
                        if( getListener() != null) getListener().onColorSelected( (int) selectedImage.getUserObject(),selectedImage.getColor());
                        factory.playUISound(style.soundSelect);
                    }
                }
            });
            this.addValueCentered(this.icons[i]).pad(1f).size(style.width, style.height);
            if( (i+1) % this.iconsPerRow == 0) {
                this.row();
            }
        }
        this.pack();
        this.moveOverIconToSelectedIcon(this.icons[0], true);
        this.overIcon.toFront();
    }

    public void selectColor(int index) {
        if(index == this.selectedIndex) return;
        this.selectedIndex = index;
        if(index < 0) {
            this.hideOverIcon();
        }
        else {
            this.moveOverIconToSelectedIcon(this.icons[index], true);
        }
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    public Color getSelectedColor() {
        if(this.selectedIcon == null) return null;
        return this.selectedIcon.getColor();
    }

    public void setListener(ColorIconArrayListener listener) {
        this.listener = listener;
    }

    private ColorIconArrayListener getListener() {
        return listener;
    }

    private void hideOverIcon() {
        this.selectedIcon = null;
        if (this.overIcon.getColor().a == 1 && this.isVisible())
            this.overIcon.addAction(Actions.fadeOut(0.1f));
    }

    private void moveOverIconToSelectedIcon(Image icon, boolean instant) {
        this.selectedIcon = icon;
        if(!instant) {
            this.overIcon.addAction(Actions.parallel(
                            Actions.fadeIn(1),
                            Actions.sequence(
                                    Actions.moveTo( icon.getX() + icon.getWidth()/2f - this.overIcon.getWidth()/2f,
                                            icon.getY() + icon.getHeight()/2f - this.overIcon.getHeight()/2f, 0.2f, Interpolation.pow2In)
                            ),
                            Actions.sequence(
                                    Actions.scaleBy(1.1f, 1.1f, 0.1f),
                                    Actions.scaleBy(-1.1f, -1.1f,0.3f))
                    )
            );
        } else {
            this.overIcon.getColor().a = 1;
            this.overIcon.setPosition(icon.getX() + icon.getWidth()/2f - this.overIcon.getWidth()/2f, icon.getY() + icon.getHeight()/2f - this.overIcon.getHeight()/2f);
        }
    }
}
