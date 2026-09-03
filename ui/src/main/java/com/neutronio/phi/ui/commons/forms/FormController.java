package com.neutronio.phi.ui.commons.forms;

import com.neutronio.phi.ui.ComponentFactory;

/**
 * Controller for a single, non-tabbed form container.
 * Includes buttons and a way to customize them. Does not
 * include layout but provides general form logic instead.
 * @param <F>
 */
public class FormController<F extends AbstractForm>
            extends FormControllerBase {

    private F form;

    public FormController(ComponentFactory componentFactory) {
        super(componentFactory);
    }

    public void setForm(F form) {
        this.form = form;
    }

    @Override
    public AbstractForm getActiveForm() {
        return this.form;
    }

    /**
     * Resets the form
     */
    public void resetForm() {
        this.form.resetStyles();
        this.form.resetValues();
        this.validationResult = null;
    }

    public void validateForm() {
        this.validationResult = new ValidationResult();
        this.form.validateForm(validationResult);

        this.showValidationResults( validationResult );

        if( validationResult.getStatus() == Form.ValidationStatus.VALID) {
            this.notifyOnSubmit(getActiveForm());
        }
    }

    /**
     * Shows validation messages on the form container.
     * @param validationResult
     */
    public void showValidationResults(ValidationResult validationResult) {
        // TODO reinstate?
//        AstraXApp.astraX.showValidationMessages(validationResult);
        this.form.markLabels(validationResult);
    }
}
