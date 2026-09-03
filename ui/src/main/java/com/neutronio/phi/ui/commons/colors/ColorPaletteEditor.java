package com.neutronio.phi.ui.commons.colors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.components.SimplePanel;
import com.neutronio.phi.ui.commons.pagination.BubblePaginator;

import java.util.Map;

/**
 * A color palette editor as used in Paint Tool Panel
 */
public class ColorPaletteEditor extends SimplePanel {

    private ColorPalette colorPalette;
    private BubblePaginator paginator;
    private RGBSlider RGBSlider;

    public ColorPaletteEditor(ComponentFactory componentFactory) {
        super(componentFactory);

        this.colorPalette = new ColorPalette(componentFactory);
        this.colorPalette.fillWithButtons("color");
        this.colorPalette.updateToContent();

        this.colorPalette.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Color color = null;
                if( colorPalette.getSelected() != null) {
                    // apply color from palette to picker. Do not copy to reflect changes
                    color = colorPalette.getSelected().cpy();
                    RGBSlider.setPickedColor(color);
                }
            }
        });

        this.paginator = new BubblePaginator(componentFactory);
        this.paginator.setPaginator(this.colorPalette);
        this.paginator.align(Align.center);

        this.RGBSlider = new RGBSlider( componentFactory );
        this.RGBSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Color selected = colorPalette.getSelected();
                if( selected != null) {

                    Color newColor = RGBSlider.getPickedColor();
//                    if(Objects.equals(newColor, Color.WHITE)) {
//                        System.out.println("Derp");
//                    }
                    selected.r = newColor.r;
                    selected.g = newColor.g;
                    selected.b = newColor.b;

                    // update style of respective button
                    colorPalette.updateButtonStyle(colorPalette.getSelectedButton(), selected);
                }
            }
        });

        SimplePanel table = new SimplePanel(componentFactory);
        table.addValue(this.colorPalette);
        table.row();
        table.addValue(this.paginator).center();

        this.addValue(table);
        this.addValue(this.RGBSlider);
    }

    /**
     *
     * @param palette directly modifies the palette
     */
    public void setColorPalette(Map<String, Color> palette) {
        for( Color color: palette.values() ) {
            this.colorPalette.addItem(color);
        }
        this.colorPalette.updateToContent();
        this.paginator.updateChange();
    }

    public Color getSelectedColor() {
        return this.colorPalette.getSelected();
    }
}
