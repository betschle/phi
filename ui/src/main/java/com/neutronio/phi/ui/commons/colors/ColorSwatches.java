package com.neutronio.phi.ui.commons.colors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.components.SimplePanel;

import java.util.*;

/**
 * Display only color swatches.
 */
public class ColorSwatches extends SimplePanel {

    protected ImageComparator imageComparator = new ImageComparator();
    protected List<Image> icons = new ArrayList<>();
    /** The maximum icon count per row */
    protected int iconsPerRow = 4;
    protected String colorSwatchDrawable = "background_rect_icon";

    private class ImageComparator implements Comparator<Image> {
        @Override
        public int compare(Image o1, Image o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public ColorSwatches(ComponentFactory componentFactory) {
        super(componentFactory);
    }

    public void setIconsPerRow(int iconsPerRow) {
        this.iconsPerRow = iconsPerRow;
    }

    /**
     * Order is determined by order in color array
     * @param colors
     */
    public void setColorPalette(Color... colors) {
        throw new UnsupportedOperationException("Must be reinstated");
        // TODO reinstate
//        List<Color> colorsList = AstraXUtil.asList(colors);
//        this.setColorPalette(colorsList);
    }

    /**
     * Order is determined by order in color array
     * @param colors
     */
    public void setColorPalette(List<Color> colors) {
        this.icons.clear();
        this.clearChildren();

        int i = 0;
        for( ;i < colors.size(); i++) {
            Image image = new Image(componentFactory.getSkin(), this.colorSwatchDrawable);
            image.setColor(colors.get(i));
            image.setName(i+"");
            this.icons.add(image);
        }
        Collections.sort(this.icons, this.imageComparator);
        i = 0;
        for( Image image : this.icons) {
            this.addValueCentered(image).pad(1f);
            if( (i+1) % this.iconsPerRow == 0) {
                this.row();
            }
            i++;
        }
        this.pack();
    }

    /**
     * Sorts the swatches by the name/keys of the color map.
     * @param palette
     */
    public void setColorPalette(Map<String, Color> palette) {
        this.icons.clear();
        this.clearChildren();
        for( String key : palette.keySet()) {
            Image image = new Image(componentFactory.getSkin(), this.colorSwatchDrawable);
            image.setColor(palette.get(key));
            image.setName(key);
            this.icons.add(image);
        }
        Collections.sort(this.icons, this.imageComparator);

        int i = 0;
        for( Image image : this.icons) {
            this.addValueCentered(image).pad(1f);
            if( (i+1) % this.iconsPerRow == 0) {
                this.row();
            }
            i++;
        }
        this.pack();
    }
}
