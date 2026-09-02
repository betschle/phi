package com.neutronio.phi.ui.commons.text;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.components.AstraXSlider;
import com.neutronio.phi.util.format.StandardFormats;

/**
 * A horizontal slider with a label
 */
public class TextSlider extends Table {


    /* TODO add styling to this component
     * TextSlider and TextProgressbar Improvements:
     * - Different value display modes, such as:
     *      percent (100 %)
     *      formatted value only (3.4)
     *      formatted value/max (3.4/20.0)
     *      formatted max only (20.0)
     *
     */
    protected AstraXLabel label;
    protected AstraXSlider slider;

    // Note: duplicate code with TextProgressbar
    /** Format for text display. */
    private StandardFormats format = StandardFormats.FACTOR;
    /** Displays only the percentage as text, "x %" */
    private boolean usePercent = false;
    /** Displays current and max value as text, "x / max"
     *  If not using percent display, this value is used as base value for this calculation. */
    private float baseValue = 1;

    public TextSlider(ComponentFactory componentFactory) {
        super();
        this.label = new AstraXLabel(componentFactory, "0", "default", null);
        this.label.setAlignment(Align.left, Align.bottom);
        this.label.setTouchable(Touchable.disabled);

        this.slider = new AstraXSlider(componentFactory);
        this.slider.setValue(0.4f);

        this.add(this.slider).padRight(15);
        this.add(this.label).width(70);
        this.pack();

        this.slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateValueText(slider.getTargetValue());
            }
        });
    }

    // Note: duplicate code with TextProgressbar
    /**
     * Sets the numbers format to be used
     * @param format
     */
    public void setFormat(StandardFormats format) {
        this.format = format;
    }

    /**
     * Programmatically changes the value of both label and slider
     * @param value a factor 0-1
     */
    public void setValue(float value) {
        this.slider.setValue(value);
        this.updateValueText(value);
    }

    /**
     *
     * @return a factor value 0-1
     */
    public float getValue() {
        return this.slider.getTargetValue();
    }

    /**
     *
     * @return the base value that can be multiplied by the stored factor
     */
    public float getBaseValue() {
        return this.baseValue;
    }
    /**
     *
     * @param value a factor 0-1
     */
    protected void updateValueText(float value) {
        value = MathUtils.clamp(value, AstraXSlider.MIN, AstraXSlider.MAX);
        if( this.usePercent ) {
            this.label.setText( this.format.format(value * 100f) + " %");
        } else {
            this.label.setText( this.format.format(value*this.baseValue)); //  + " / " + this.format.format(this.baseValue)
        }
        this.pack();
    }

    /**
     * True: Displays only the percentage as text, "x %" +
     * False: Displays current and max value as text, "x / max". Don't forget to set the base value
     * for this!
     * @param usePercent
     */
    public void setUsePercent(boolean usePercent) {
        this.usePercent = usePercent;
        this.updateValueText(this.slider.getTargetValue());
    }

    /**
     *
     * @param baseValue If not using percent display, this value is used as base value for text display
     */
    public void setBaseValue(float baseValue) {
        this.baseValue = baseValue;
    }
}
