package com.neutronio.phi.ui.commons.forms;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.buttons.AstraXTextButton;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for both {@link FormController} and {@link MultiFormController}.
 * Provides form validation and button actions.
 */
public abstract class FormControllerBase {

    protected ComponentFactory componentFactory;

    /** Submit Button */
    protected AstraXTextButton submitButton;
    /** Back or Cancel Button */
    protected AstraXTextButton backButton;
    /** Reset Button */
    protected AstraXTextButton resetButton;
    /** Custom Buttons for different actions */
    protected Map<String, AstraXTextButton> customButtons = new LinkedHashMap<>();

    protected ValidationResult validationResult;
    protected List<FormListener> listeners = new ArrayList<>();

    public FormControllerBase(ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
    }

    public AstraXTextButton addSubmitButton(String text, String soundID) {
        this.submitButton = new AstraXTextButton(this.componentFactory, "primary");
        this.submitButton.setCanCheck(false);
        this.submitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                validateForm();
            }
        });
        return this.submitButton;
    }

    public AstraXTextButton addSubmitButton(String text) {
        return this.addSubmitButton(text, "submit");
    }

    public AstraXTextButton addResetButton(String text) {
        this.resetButton = new AstraXTextButton(this.componentFactory, "secondary");
        this.resetButton.setCanCheck(false);
        this.resetButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                notifyOnReset(getActiveForm());
            }
        });
        return this.resetButton;
    }

    public AstraXTextButton addBackButton(String text) {
        this.backButton = new AstraXTextButton(this.componentFactory, "secondary");
        this.backButton.setCanCheck(false);
        this.backButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                notifyOnCancel(getActiveForm());
            }
        });
        return this.backButton;
    }

    /**
     * Adds a custom buttons thasat fires an onFormAction(action) event
     * @param text
     * @param style
     * @param sound
     * @param action
     * @return
     */
    public AstraXTextButton addCustomButton(String text, String style, final String action) {
        AstraXTextButton customButton = new AstraXTextButton(this.componentFactory, style);
        customButton.setCanCheck(false);
        customButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                notifyOnFormAction(getActiveForm(), action);
            }
        });
        this.customButtons.put(action, customButton);
        return customButton;
    }

    public AstraXTextButton getSubmitButton() {
        return submitButton;
    }

    public AstraXTextButton getBackButton() {
        return backButton;
    }

    public AstraXTextButton getResetButton() {
        return resetButton;
    }

    /**
     * Adds a listener to this FormController
     * @param defaultFormListener
     */
    public void addListener(FormListener defaultFormListener) {
        if( defaultFormListener == null) return;
        this.listeners.add(defaultFormListener);
    }

    protected void notifyOnCancel(AbstractForm form) {
        for( FormListener defaultFormListener : listeners) {
            defaultFormListener.onFormCancel(form);
        }
    }

    protected void notifyOnReset(AbstractForm form) {
        for( FormListener defaultFormListener : listeners) {
            defaultFormListener.onFormReset(form);
        }
    }

    protected void notifyOnSubmit(AbstractForm form) {
        for( FormListener defaultFormListener : this.listeners) {
            defaultFormListener.onFormSubmit(form);
        }
    }

    protected void notifyOnFormAction(AbstractForm form, String action) {
        for( FormListener defaultFormListener : this.listeners) {
            defaultFormListener.onFormAction(form, action);
        }
    }

    public abstract AbstractForm getActiveForm();

    /**
     * Resets the form(s)
     */
    public abstract void resetForm();

    /**
     * Validates the form(s)
     */
    public abstract void validateForm();

    /**
     * Shows the validation results messages.
     * @param validationResult
     */
    public abstract void showValidationResults(ValidationResult validationResult);
}
