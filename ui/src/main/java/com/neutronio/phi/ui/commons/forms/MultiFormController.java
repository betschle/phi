package com.neutronio.phi.ui.commons.forms;

import com.neutronio.phi.ui.ComponentFactory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class MultiFormController extends FormControllerBase {

    //  validation marking is untested
    protected Map<Class, AbstractForm> forms = new LinkedHashMap<>();

    public MultiFormController(ComponentFactory componentFactory) {
        super(componentFactory);
    }

    /**
     * Puts the given form into the form hashmap.
     * @param form
     */
    public void putForm(AbstractForm form) {
        this.forms.put(form.getClass(), form);
    }

    /**
     * Gets a form by class
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends AbstractForm> T getForm(Class<T> clazz) {
        return (T) forms.get(clazz);
    }

    /**
     * Gets all Forms in this controller
     * @return
     */
    public Collection<AbstractForm> getForms() {
        return forms.values();
    }

    @Override
    public AbstractForm getActiveForm() {
        AbstractForm visibleForm = null;
        for( AbstractForm form : this.forms.values()) {
            if( form.isVisible() ) visibleForm = form;
        }
        return visibleForm;
    }

    @Override
    public void resetForm() {
        for( AbstractForm form : this.forms.values()) {
            form.resetStyles();
            form.resetValues();
        }
        this.validationResult = null;
    }

    @Override
    public void validateForm() {
        this.validationResult = new ValidationResult();
        AbstractForm visibleForm = null;
        for( AbstractForm form : this.forms.values()) {
            form.validateForm(validationResult);
            if( form.isVisible() ) visibleForm = form;
        }

        this.showValidationResults( validationResult );

        // TODO required mode: active and all. For active always return active Form, for multi
        // return null
        if( validationResult.getStatus() == Form.ValidationStatus.VALID) {
            this.notifyOnSubmit(visibleForm);
        }
    }

    @Override
    public void showValidationResults(ValidationResult validationResult) {
        // TODO reinstate?
//        if( validationResult.getStatus() == Form.ValidationStatus.VALID) {
//            validationResult.addMessage(this.componentFactory.translate(AstraXAppTranslations.FORM_VALIDATION_MESSAGE_APPLIED_CHANGES));
//        }
//        AstraXApp.astraX.showValidationMessages(validationResult);
        for( AbstractForm form : this.forms.values()) {
            form.markLabels(validationResult);
        }
    }
}
