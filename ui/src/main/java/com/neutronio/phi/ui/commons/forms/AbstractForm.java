package com.neutronio.phi.ui.commons.forms;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.components.AbstractPanel;
import com.neutronio.phi.ui.commons.text.AstraXLabel;
import com.neutronio.phi.ui.skin.ReactiveColor;

import java.util.ArrayList;
import java.util.List;

/**
 * An Abstract Form
 */
public abstract class AbstractForm
        extends AbstractPanel
        implements Form {

    public List<AstraXLabel> labelColumn = new ArrayList<>();
    public List<Actor> valueColumn = new ArrayList<>();

    public AbstractForm(ComponentFactory componentFactory, String style, String backgroundStyle) {
        super(componentFactory, style, backgroundStyle);
    }

    @Deprecated
    public AbstractForm(ComponentFactory componentFactory, String style) {
        super(componentFactory, style);
    }

    @Deprecated
    public AbstractForm(ComponentFactory componentFactory) {
        super(componentFactory);
    }

    @Override
    public void clearChildren() {
        super.clearChildren();
        this.labelColumn.clear();
        this.valueColumn.clear();
    }

    /**
     * Adds a row where label is registered for form validation markup.
     * Only use this for components that need to be accessible.
     * Can be used with other calls like  {@link #addLabel(String)} or
     * {@link #addHeader(String, int)}.
     * @param labelText
     * @param valueWidget
     */
    public void addRow( String labelText, Actor valueWidget, int valueColspan) {
        AstraXLabel label = this.addLabel(labelText);
        this.addValue( valueWidget, valueColspan );
        this.labelColumn.add( label );
        this.valueColumn.add( valueWidget );
    }

    /**
     * Marks those labels where a validation error occurred. MUST be called during validation!
     * @param validationResult important: this object must be constructed using this form.
     */
    public void markLabels(ValidationResult validationResult) {
        this.resetStyles();
        for( ValidationResult.ValidationMessage message : validationResult.getMessages() ) {
            // after validation, the components where the error occured should be marked somehow.
            if( message.component != null ) {
                int componentIndex = this.valueColumn.indexOf(message.component);
                if( componentIndex >= 0) {
                    AstraXLabel labelForComponent = this.labelColumn.get(componentIndex);
                    labelForComponent.getBackground().setState(ReactiveColor.ReactiveState.CHECKED);
                }
            }
        }
    }

    @Override
    public void resetStyles() {
        for( AstraXLabel label : this.labelColumn ) {
            label.setStyle( this.panelStyle.labelStyle );
            label.getBackground().setState(ReactiveColor.ReactiveState.UP);
        }
    }
}
