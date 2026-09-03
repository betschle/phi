package com.neutronio.phi.ui.commons.forms;

public interface Form {

    /**
     * Validation status of a form
     */
    enum ValidationStatus {
        /** Form was not touched/modified yet */
        UNTOUCHED,
        /** Form was validated and valid */
        VALID,
        /** Form was validated and invalid */
        INVALID;
    }
    /**
     * Adds validation messages for this form.
     * @param result a previously calculated result for chaining. Can be null
     */
    ValidationResult validateForm(ValidationResult result);

    /**
     * Resets the form to the values it was initialized with (which may include empty values).
     * Must be invoked manually?
     */
    @Deprecated
    void resetForm();

    /**
     * Resets the styles of labels, to undo validation marking.
     */
    void resetStyles();
}
