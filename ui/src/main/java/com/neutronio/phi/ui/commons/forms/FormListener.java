package com.neutronio.phi.ui.commons.forms;

public interface FormListener {

    /**
     * Fired on form submit, after validation was processed.
     * @param form the form that was submitted
     */
    void onFormSubmit(AbstractForm form);

    /**
     * Fired when cancel button was clicked.
     * @param form the form that was cancelled
     */
    void onFormCancel(AbstractForm form);

    /**
     * Fired when reset button was clicked.
     * @param form the form that was reset
     */
    void onFormReset(AbstractForm form);

    /**
     * Fired when a custom button was clicked. Invokes additional actions on form reset. Note that
     * resetting the form is inherent already.
     * @param form the form that was active when action was triggered
     */
    void onFormAction(AbstractForm form, String action);
}
