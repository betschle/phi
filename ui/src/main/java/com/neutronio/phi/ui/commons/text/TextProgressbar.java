package com.neutronio.phi.ui.commons.text;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.neutronio.astrax.ui.ComponentFactory;
import com.neutronio.astrax.ui.commons.ProgressBar;
import com.neutronio.astrax.util.Phi;
import com.neutronio.astrax.util.StandardFormats;

/**
 * A Text Progressbar that displays its value, formatted, on the bar.
 */
@Phi(Phi.PENDING)
public class TextProgressbar extends Stack {
    // TODO add styling to this component
    public static class Style {
        public Label.LabelStyle labelStyle;
        public ProgressBar.ProgressBarStyle progressBarStyle;
    }

    private ProgressBar progressBar;
    private Label label;

    // same code with TextSlider
    /** Format for text display. */
    private StandardFormats format = StandardFormats.FACTOR;
    /** Displays only the percentage as text, "x %" */
    private boolean usePercent;
    /** Displays current and max value as text, "x / max"
     *  If not using percent display, this value is used as base value for this calculation. */
    private float baseValue = 1;

    public TextProgressbar(ComponentFactory componentFactory) {
        this(componentFactory, "default");
    }

    public TextProgressbar(ComponentFactory componentFactory, String style) {
        this(componentFactory, componentFactory.getSkin().get(style, Style.class));
    }

    public TextProgressbar(ComponentFactory componentFactory, Style style) {
        this.progressBar = new ProgressBar(componentFactory, style.progressBarStyle);
        this.label = new Label("0", style.labelStyle);
        this.label.setAlignment(Align.center, Align.center);
        this.label.setTouchable(Touchable.disabled);

        this.add(this.progressBar);
        this.add(this.label);
        this.pack();
    }


    // Note: duplicate code with TextSlider
    /**
     * Sets the numbers format to be used
     * @param format
     */
    public void setFormat(StandardFormats format) {
        this.format = format;
        this.setValue(this.progressBar.getValue());
    }

    /**
     *
     * @param value a factor 0-1
     */
    public void setValue(float value) {
        this.progressBar.setValue( value );
        if( this.usePercent ) {
            this.label.setText( this.format.format(value * 100f) + " %");
        } else {
            this.label.setText( this.format.format(value*this.baseValue)
                    + " / " + this.format.format(this.baseValue) );
        }
        this.progressBar.setHeight( this.label.getHeight() + 5);
        this.progressBar.pack();
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
    }

    /**
     *
     * @param baseValue If not using percent display, this value is used as base value for text display
     */
    public void setBaseValue(float baseValue) {
        this.baseValue = baseValue;
    }
}
