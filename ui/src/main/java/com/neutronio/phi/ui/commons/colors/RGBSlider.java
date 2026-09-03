package com.neutronio.phi.ui.commons.colors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Pools;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.forms.AbstractForm;
import com.neutronio.phi.ui.commons.forms.ValidationResult;
import com.neutronio.phi.ui.skin.PhiUITranslations;
import com.neutronio.phi.util.ColorUtil;

/**
 * Allows for adjusting a color by its RGB values.
 * Also allows for copypasting RBG hex values.
 * Fires a change event when changing the color.
 */
public class RGBSlider extends AbstractForm {

    public static String HEX_HINT = "Copy & Paste hex codes from different programs here";

    private Slider redValue;
    private Slider blueValue;
    private Slider greenValue;
    private TextField hexValue;
    private Image preview;
    private String hexRegex = "#[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]";

    public RGBSlider(ComponentFactory componentFactory) {
        super(componentFactory, "colorpicker");

        this.hexValue = new TextField("", componentFactory.getSkin());
        this.hexValue.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if( hexValue.getText().length() > 0) {
                    updateColorFromHex();
                }
            }
        });
        this.hexValue.addListener( new Tooltip<>( new Label(getTranslation("COLOR_HEX_HINT"), componentFactory.getSkin(), "tooltip")));

        ChangeListener sliderUpdater = new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                updateColorFromSliders();
            }
        };

        this.redValue = new Slider(0, 1,  1f/255f, false, componentFactory.getSkin());
        this.redValue.addListener(sliderUpdater);

        this.blueValue = new Slider(0, 1,  1f/255f, false, componentFactory.getSkin());
        this.blueValue.addListener(sliderUpdater);

        this.greenValue = new Slider(0, 1,  1f/255f, false, componentFactory.getSkin());
        this.greenValue.addListener( sliderUpdater );

        this.preview = new Image(getSkin().getPatch("background_para"));
        this.preview.setSize(48, 48);

        this.add(this.preview).minSize(32, 32).fill()
                .colspan(2)
                .padBottom(this.panelStyle.rowPadding).padRight(this.panelStyle.columnPadding);
        this.row();

        // TODO add some basic translations for phi
        this.addRow(getTranslation(PhiUITranslations.COLOR_RED), this.redValue);
        this.row();

        this.addRow(getTranslation(PhiUITranslations.COLOR_GREEN), this.greenValue);
        this.row();

        this.addRow(getTranslation(PhiUITranslations.COLOR_BLUE), this.blueValue);
        this.row();

        this.addLabel(getTranslation(PhiUITranslations.COLOR_HINT_COPYPASTE_HEX));
        this.addValue(this.hexValue, 1);
        this.updateColorFromSliders();
    }

    private void updateColorFromSliders() {
        Color color = new Color( this.redValue.getValue(), this.greenValue.getValue(), this.blueValue.getValue(), 1 );
        this.preview.setColor( color );
        this.hexValue.setText( "#"+ ColorUtil.colorToHexString(color));

        ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);
        fire(changeEvent);
        Pools.free(changeEvent);
    }

    private void updateColorFromHex() {
        if( this.hexValue.getText().matches(this.hexRegex) ) {
            String hex = this.hexValue.getText().replace('#', ' ').trim();
            Color color = Color.valueOf(hex);
            this.setPickedColor(color);

            ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);
            fire(changeEvent);
            Pools.free(changeEvent);
        } else {
            // reset to old color
        }
    }

    /**
     * Sets the currently edited/displayed color.
     * @param color
     */
    public void setPickedColor( Color color) {
        this.redValue.setValue( color.r );
        this.greenValue.setValue( color.g );
        this.blueValue.setValue( color.b );
        this.updateColorFromSliders();
    }

    /**
     * Gets the color that is currently edited by the user in this component
     * @return
     */
    public Color getPickedColor() {
        return this.preview.getColor();
    }

    @Override
    public ValidationResult validateForm(ValidationResult result) {
        return result;
    }

    @Override
    public void resetForm() {

    }

    @Override
    public void resetValues() {

    }
}
