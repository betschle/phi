package com.neutronio.phi.ui.commons;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.text.AstraXLabel;

/**
 * Meant as simple display panel.
 * Has some convenience methods for quick construction of panels.
 */
public abstract class AbstractPanel
        extends
            AstraXComponent
{

    protected AbstractPanelStyle panelStyle;

    public static class AbstractPanelStyle {
        /** Default label style */
        public Label.LabelStyle labelStyle;
        public Background.BackgroundStyle labelBackgroundStyle;
        /** Header 1 style */
        public Label.LabelStyle headerStyle;
        public Background.BackgroundStyle headerBackgroundStyle;
        /** Header 2 style */
        public Label.LabelStyle header2Style;
        public Background.BackgroundStyle header2BackgroundStyle;
        /** Padding between label/value pairs or columns. */
        public int columnPadding = 20;
        /** Padding between rows */
        public int rowPadding = 5;
        /** Minimum width of labels. */
        public int minLabelWidth = 70;
        /** Minimum width of components storing any values. */
        public int minValueWidth = 200;
        /** Maximum width of labels. */
        public int maxLabelWidth = Integer.MAX_VALUE;
        /** Maximum width of components storing any values. */
        public int maxValueWidth = Integer.MAX_VALUE;
    }

    public AbstractPanel(ComponentFactory componentFactory) {
        super(componentFactory, null);
        this.setStyle(componentFactory.getSkin().get(AbstractPanelStyle.class));
    }

    public AbstractPanel(ComponentFactory componentFactory, String style) {
        super(componentFactory);
        this.setStyle(componentFactory.getSkin().get(style, AbstractPanelStyle.class));
    }

    protected AbstractPanel(ComponentFactory componentFactory, AbstractPanelStyle style, String backgroundStyle) {
        super(componentFactory);
        this.setStyle(style);
        if( backgroundStyle != null) {
            this.setBackgroundStyle(componentFactory.getSkin().get(backgroundStyle, Background.BackgroundStyle.class));
        }
    }

    /**
     *
     * @param componentFactory
     * @param style
     * @param backgroundStyle null for none
     */
    public AbstractPanel(ComponentFactory componentFactory, String style, String backgroundStyle) {
        super(componentFactory);
        this.setStyle(componentFactory.getSkin().get(style, AbstractPanelStyle.class));
        if( backgroundStyle != null) {
            this.setBackgroundStyle(componentFactory.getSkin().get(backgroundStyle, Background.BackgroundStyle.class));
        }
    }

    public void setStyle( AbstractPanelStyle abstractPanelStyle) {
        this.panelStyle = abstractPanelStyle;
    }

    public AbstractPanelStyle getStyle() {
        return panelStyle;
    }

    /**
     * Resets displayed values to empty state.
     */
    public abstract void resetValues();

    /**
     * Adds a row where label is registered for form validation markup.
     * Only use this for components that need to be accessible.
     * Can be used with other calls like  {@link #addLabel(String)} or
     * {@link #addHeader(String, int)}.
     * @param labelText
     * @param valueWidget
     */
    public void addRow(String labelText, Actor valueWidget, int valueColspan) {
        this.addLabel(labelText);
        this.addValue( valueWidget, valueColspan );
    }


    /**
     * Adds a row where label is registered for form validation markup.
     * Only use this for components that need to be accessible.
     * Can be used with other calls like  {@link #addLabel(String)} or
     * {@link #addHeader(String, int)}.
     * @param labelText
     * @param valueWidget
     */
    public void addRow(String labelText, Actor valueWidget) {
        this.addLabel(labelText);
        this.addValue( valueWidget, 1 );
    }


    /**
     * Convenience Method. Adds any value to the form
     * @param widget
     * @return
     */
    public Cell<Actor> addValue(Actor widget, int colspan) {
        // TODO doing pad on actor cells can cause wiggle behavior. Add padding on rows instead!!!!
        return this.add(widget)
                .minWidth(this.panelStyle.minValueWidth)
                .maxWidth(this.panelStyle.maxValueWidth)
                .fill()
                .colspan(colspan)
                .padBottom(this.panelStyle.rowPadding).padRight(this.panelStyle.columnPadding);
    }

    public Cell<Actor> addValueNoMin(Actor widget, int colspan) {
        return this.add(widget)
                .fill()
                .colspan(colspan)
                .padBottom(this.panelStyle.rowPadding).padRight(this.panelStyle.columnPadding);
    }

    /**
     * Convenience Method. Adds any value to the form
     * @param widget
     */
    public Cell<Actor> addValue(Actor widget) {
        return this.addValue(widget, 1);
    }

    /**
     * Convenience Method. Adds any value to the form
     * @param widget
     */
    public Cell<Actor> addValueCentered(Actor widget) {
        return this.addValueCentered(widget, 1);
    }

    /**
     * Convenience Method. Adds any value to the form
     * @param widget
     */
    public Cell<Actor> addValueCentered(Actor widget, int colspan) {
        return this.add(widget).center()
                .colspan(colspan)
                .pad(this.panelStyle.columnPadding/2f);
    }

    /**
     * Convenience Method. Adds a descriptive label to the form. Does not register
     * the label for validation markup.
     * @param labelText
     */
    public AstraXLabel addLabel(String labelText) {
        return this.addLabel(labelText, 1);
    }

    /**
     * Convenience Method. Adds a descriptive label to the form. Does not register
     * the label for validation markup.
     * @param labelText
     */
    public AstraXLabel addLabel(String labelText, int colspan) {
        AstraXLabel label = new AstraXLabel(componentFactory, labelText, this.panelStyle.labelStyle, this.panelStyle.labelBackgroundStyle);
        this.add(label)
                .left()
                .minWidth(this.panelStyle.minLabelWidth)
                .maxWidth(this.panelStyle.maxLabelWidth)
                .padRight(this.panelStyle.columnPadding)
                .padBottom(this.panelStyle.rowPadding)
                .colspan(colspan)
                .fill();
        return label;
    }

    /**
     * Convenience Method. Adds a descriptive label to the form. Does not register
     * the label for validation markup.
     * @param labelText
     */
    public AstraXLabel addLabel(String labelText, String labelStyle) {
        return this.addLabel(labelText, labelStyle, this.panelStyle.labelBackgroundStyle);
    }

    /**
     * Convenience Method. Adds a descriptive label to the form. Does not register
     * the label for validation markup.
     * @param labelText
     */
    public AstraXLabel addLabel(String labelText, String labelStyle, String labelBackgroundStyle) {
        return this.addLabel(labelText, labelStyle, this.componentFactory.getSkin().get(labelBackgroundStyle, Background.BackgroundStyle.class));
    }

    /**
     * Convenience Method. Adds a descriptive label to the form. Does not register
     * the label for validation markup.
     * @param labelText
     */
    public AstraXLabel addLabel(String labelText, String labelStyle, Background.BackgroundStyle labelBackgroundStyle ) {
        AstraXLabel label = new AstraXLabel(componentFactory, labelText, this.componentFactory.getSkin().get(labelStyle, Label.LabelStyle.class), labelBackgroundStyle);
        this.add(label)
                .left()
                .minWidth(this.panelStyle.minLabelWidth)
                .maxWidth(this.panelStyle.maxLabelWidth)
                .padRight(this.panelStyle.columnPadding)
                .padBottom(this.panelStyle.rowPadding)
                .colspan(1);
//                .fill();
        return label;
    }

    public AstraXLabel getLabel(String text) {
        return new AstraXLabel(componentFactory, text, this.panelStyle.labelStyle, this.panelStyle.labelBackgroundStyle);
    }

    public AstraXLabel getHeader(String text) {
        AstraXLabel label = new AstraXLabel(componentFactory, text, this.panelStyle.headerStyle, this.panelStyle.headerBackgroundStyle);
        label.setAlignment(Align.center, Align.center);
        label.setAlignment(Align.center);
        return label;
    }

    public AstraXLabel getHeader2(String text) {
        AstraXLabel label = new AstraXLabel(componentFactory, text, this.panelStyle.header2Style, this.panelStyle.header2BackgroundStyle);
        label.setAlignment(Align.center, Align.center);
        label.setAlignment(Align.center);
        return label;
    }

    /**
     * Convenience Method. Adds a large static header to the form
     * @param labelText
     * @return
     */
    public Cell<AstraXLabel> addHeader(String labelText, int colspan) {
        AstraXLabel label = getHeader(labelText);
        return this.add( label )
                .center()
                .padBottom(this.panelStyle.rowPadding * 3f)
                .colspan(colspan)
                .fill();
    }

    /**
     * Convenience Method. Adds a large static title header to the form
     * @param labelText
     */
    public Cell<AstraXLabel> addHeader(String labelText, float minWidth, int colspan) {
        AstraXLabel label = new AstraXLabel(componentFactory, labelText, this.panelStyle.headerStyle, this.panelStyle.headerBackgroundStyle);
        label.setAlignment(Align.center, Align.center);
        return this.add( label )
                .center()
                .minWidth(minWidth)
                .padBottom(this.panelStyle.rowPadding * 3f)
                .colspan(colspan)
                .fill();
    }

    /**
     * Convenience Method. Adds a large static header to the form
     * @param labelText
     */
    public Cell<AstraXLabel> addHeader2(String labelText, int colspan) {
        AstraXLabel label = new AstraXLabel(componentFactory, labelText, this.panelStyle.header2Style, this.panelStyle.header2BackgroundStyle);
        label.setAlignment(Align.center, Align.center);
        return this.add( label )
                .center()
                .padBottom(this.panelStyle.rowPadding * 3f)
                .colspan(colspan)
                .fill();
    }

    /**
     * Convenience Method. Adds a large static header to the form
     * @param labelText
     */
    public void addHeader2(String labelText) {
        this.addHeader2(labelText, 1);
    }

    /**
     * Convenience Method. Adds a static separator or filler to the form.
     * This component can be used to control the min width of the form.
     */
    public void addSeparator(float minHeight, float minWidth, int colspan) {
        // TODO reactivate
//        this.add( new Separator( getSkin(), "background_line2")) // TODO make part of formstyle?
//                .center()
//                .minSize(minWidth, minHeight)
//                .padBottom(this.panelStyle.rowPadding)
//                .colspan(colspan)
//                .fill();
    }
}
