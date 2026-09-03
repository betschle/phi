package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.ui.ComponentFactory;

/**
 * Displays an array of icons with an image hovering
 * over the currently selected icon. Receive callbacks
 * via IconPickerListener.
 */
public class IconPicker extends Group {


    protected ComponentFactory factory;
    protected String soundSelect = "zoom:in";
    /** Icons are placed in this table. Using it allows the over icon to be placed on top of it. */
    protected Table iconBody;
    /** The maximum icon count per row */
    protected int iconsPerRow = 4;
    protected Skin skin;

    protected Image[] icons;
    /** Floats on top selected icon */
    protected Image overIcon;
    protected Image selectedIcon;

    protected IconPickerListener iconPickerListener;

    public interface IconPickerListener {
        /** Invoked when an icon in the picker was clicked on.
         * @param icon
         */
        void onIconClicked(Image icon);
    }

    public IconPicker(ComponentFactory factory) {
        this.factory = factory;
        this.skin = factory.getSkin();
        this.overIcon = new Image( skin, "icon_round_over");
        this.overIcon.setOrigin(Align.center);
        this.overIcon.setColor(skin.getColor("primary"));

        this.iconBody = new Table(skin);
        this.addActor(iconBody);

        // click listener to forward selected icon changes
        this.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if( iconPickerListener != null)
                    iconPickerListener.onIconClicked(selectedIcon);
            }
        });
    }

    public void setIconColor(Color color) {
        for(Image icon : this.icons) {
            icon.setColor(color);
        }
    }

    public void setIconsPerRow(int iconPerRow) {
        this.iconsPerRow = iconPerRow;
    }

    public void setIcons(String... drawables) {
        if( drawables.length <= 0) return;

        this.iconBody.clear();
        this.icons = new Image[drawables.length];

        int i = 0;
        for( String drawable : drawables) {
            icons[i] = new Image(skin, drawable);
            icons[i].setName(drawable);
            icons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if( !hasActions() ) {
                        // place overIcon over currently hovered icon
                        setSelectedIcon((Image) event.getTarget());
                        factory.playUISound(soundSelect);
                    }
                }
            });
            this.iconBody.add(icons[i]).pad(2f);
            if( (i+1) % iconsPerRow == 0) {
                this.iconBody.row();
            }
            i++;
        }
        this.iconBody.pack();
        this.setSize(this.iconBody.getWidth(), this.iconBody.getHeight());
        this.selectedIcon = icons[0];
        this.addActor(this.overIcon);
    }

    /**
     * Finds icon by name in array and selects it properly.
     * @param name
     */
    public void setSelectedIcon(String name) {
        // find icon in array and add select it
        for( Image icon : this.icons) {
            if( icon.getName().contains(name) ) {
                this.setSelectedIcon( icon );
                return;
            }
        }
        // fallback if name of icon could not be found
        setSelectedIcon( new Image(skin.getDrawable("icon_inline_cog")));
    }

    public void setSelectedIcon(Image icon) {
        selectedIcon = icon;
        overIcon.addAction( Actions.moveTo( selectedIcon.getX() + selectedIcon.getWidth()/2f - this.overIcon.getWidth()/2f,
                                            selectedIcon.getY() + selectedIcon.getHeight()/2f - this.overIcon.getHeight()/2f, 0.2f, Interpolation.pow2In) );
        overIcon.addAction( Actions.sequence( Actions.rotateBy(90f, 0.1f),
                                              Actions.rotateBy(-90f, 0.3f)) );

    }

    public IconPickerListener getIconPickerListener() {
        return iconPickerListener;
    }

    public void setIconPickerListener(IconPickerListener iconPickerListener) {
        this.iconPickerListener = iconPickerListener;
    }

    public Drawable getSelectedDrawable() {
        return this.selectedIcon.getDrawable();
    }

    public String getSelectedDrawableName() {
        return this.selectedIcon.getName();
    }
}
