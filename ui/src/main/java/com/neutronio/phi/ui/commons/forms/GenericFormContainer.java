package com.neutronio.phi.ui.commons.forms;

import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.components.SimplePanel;
import com.neutronio.phi.ui.commons.buttons.AstraXTextButton;

/**
 * A Generic Form Container featuring a FormController and a Form, with
 * control buttons to go. Use the static method to generically create one.
 * Add a FormListener to the FormController to Listen to button events.
 */
public class GenericFormContainer<F extends AbstractForm> extends SimplePanel {

    protected FormController<F> formController;
    protected F form;

    /**
     *
     * @param componentFactory
     * @param form
     * @param backgroundStyle the background to use, null for none
     * @param cancelTranslationID a translation ID that is translated when creating the cancel button. Null for no button
     * @param resetTranslationID a translation ID that is translated when creating the reset button. Null for no button
     * @param submitTranslationID a translation ID that is translated when creating the submit button. Null for no button
     * @param <T>
     * @return
     */
    public static <T extends AbstractForm> GenericFormContainer<T> createFormContainer (
        ComponentFactory componentFactory, String backgroundStyle, T form,
        String cancelTranslationID, String resetTranslationID, String submitTranslationID
    )
    {
        GenericFormContainer<T> formContainer = new GenericFormContainer<>(componentFactory, backgroundStyle, form);
        int colspan = 0;
        if(cancelTranslationID != null) colspan++;
        if(resetTranslationID != null) colspan++;
        if(submitTranslationID != null) colspan++;

        formContainer.addValue(formContainer.form).colspan(colspan);
        formContainer.row();
        if( cancelTranslationID != null) {
            AstraXTextButton cancelButton = formContainer.formController
                    .addBackButton(componentFactory.translate(cancelTranslationID));
            cancelButton.setSquishable(false);
            formContainer.addValueCentered(cancelButton).minWidth(120);
        }
        if( resetTranslationID != null) {
            AstraXTextButton resetButton = formContainer.formController
                    .addResetButton(componentFactory.translate(resetTranslationID));
            resetButton.setSquishable(false);
            formContainer.addValueCentered(resetButton).minWidth(120);
        }
        if( submitTranslationID != null) {
            AstraXTextButton submitButton = formContainer.formController
                    .addSubmitButton(componentFactory.translate(submitTranslationID));
            submitButton.setSquishable(false);
            formContainer.addValueCentered(submitButton).minWidth(120);
        }
        formContainer.pack();
        return formContainer;
    }
    /**
     *
     * @param componentFactory
     * @param form
     * @param cancelTranslationID a translation ID that is translated when creating the cancel button. Null for no button
     * @param resetTranslationID a translation ID that is translated when creating the reset button. Null for no button
     * @param submitTranslationID a translation ID that is translated when creating the submit button. Null for no button
     * @param <T>
     * @return
     */
    public static <T extends AbstractForm> GenericFormContainer<T> createFormContainer (
            ComponentFactory componentFactory, T form,
            String cancelTranslationID, String resetTranslationID, String submitTranslationID
    ) {
        return createFormContainer(componentFactory, null, form, cancelTranslationID, resetTranslationID, submitTranslationID);
    }

    protected GenericFormContainer(ComponentFactory componentFactory, String backgroundStyle, F form) {
        super(componentFactory, "default", backgroundStyle);
        this.form = form;
        this.formController = new FormController<>(componentFactory);
        this.formController.setForm(this.form);
    }

    protected GenericFormContainer(ComponentFactory componentFactory, F form) {
        super(componentFactory);
        this.form = form;
        this.formController = new FormController<>(componentFactory);
        this.formController.setForm(this.form);
    }

    public FormController<F> getFormController() {
        return formController;
    }

    public F getForm() {
        return form;
    }
}
