package com.neutronio.phi.ui.commons.tabs;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.buttons.AstraXTextButton;
import com.neutronio.phi.ui.commons.components.IconLabel;
import com.neutronio.phi.ui.commons.forms.AbstractForm;
import com.neutronio.phi.ui.commons.forms.Form;
import com.neutronio.phi.ui.commons.forms.ValidationResult;
import com.neutronio.phi.ui.skin.PhiUITranslations;

import java.util.ArrayList;
import java.util.List;

/**
 * A tabbed component that features multiple forms. All forms
 * must be validated. Outputs one object that has a nested structure,
 * where each form tab represents one nested object.
 *
 * Form actions can be customized in two different ways.
 * <ul>
 *     <li>a) the FormListener, for instantly invoked actions, e.g. validation or applying values. </li>
 *     <li>b) AstraXButton's CustomActions. These Actions are invoked after the buttons have finished Tweening.
 *     Use {@link AstraXTextButton#setButtonAction(CustomAction)} to define them.</li>
 * </ul>
 * Both of these can be used simultaneously. Which has to be decided per case basis. As general rule of thumb,
 * context switches (screen or component) should be invoked with CustomActions for aesthetic reasons.
 */
public abstract class TabbedForm<OUTPUT>
        extends Tab {

    public enum ValidationMode {
        /** Default mode. ALL forms in the tab need to be valid in order for a tabbed form
         * to be valid. */
        ALL,
        /** Only the currently active form needs to be valid for a
         * tabbed form to be valid. */
        SOLO;
    }

    private String successMessage = null;

    private ValidationMode validationMode = ValidationMode.ALL;
    private Form.ValidationStatus validationStatus = Form.ValidationStatus.UNTOUCHED;
    private IconLabel messageLabel;
    private AstraXTextButton startButton;
    private AstraXTextButton backButton;

    private List<AbstractForm> forms = new ArrayList<>();
    private List<GroupFormListener> listeners = new ArrayList<>();

    public interface GroupFormListener<OUTPUT> {
        void onBack();
        void onValidate(ValidationResult validationResult);
        void onFinished(OUTPUT outputSettings);
    }

    public TabbedForm(ComponentFactory componentFactory, String style, String backgroundStyle) {
        super(componentFactory, style,backgroundStyle);

        // TODO translations
        this.startButton = new AstraXTextButton(componentFactory, "primary");
        this.startButton.setText(componentFactory.translate(PhiUITranslations.BUTTON_TEXT_START));
        this.startButton.setCanCheck(false);
        this.startButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                validateForms();
                event.stop();
            }
        });

        this.backButton = new AstraXTextButton( componentFactory, "secondary");
        this.backButton.setText(componentFactory.translate(PhiUITranslations.BUTTON_TEXT_BACK));
        this.backButton.setCanCheck(false);
        this.backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if( event.getTarget() != null) {
                    notifyOnBack();
                    event.stop();
                }
            }
        });
        this.messageLabel = new IconLabel(componentFactory, "empty");
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    /**
     * Should not be changed before, during or after form validation
     * @param validationMode
     */
    public void setValidationMode(ValidationMode validationMode) {
        this.validationMode = validationMode;
    }

    @Override
    public void addTab(String title, Group content) {
        super.addTab(title, content);
        logger.warning(String.format("Warning: you are using TabbedForm.addTab to add a '%s' (Tab: '%s'). This component will not be " +
                "affected by form validation. Are you sure you wanted to do that?", content.getClass().getCanonicalName(), title));
    }

    public void setBackButtonVisible(boolean visible) {
        this.backButton.setVisible(visible);
    }

    public AstraXTextButton getStartButton() {
        return startButton;
    }

    public AstraXTextButton getBackButton() {
        return backButton;
    }

    public void setButtonTexts(String backText, String submitText ) {
        this.backButton.setText(backText);
        this.startButton.setText(submitText);
    }

    public void addForm(String tabName, AbstractForm form) {
        if(form == null) return;
        super.addTab(tabName, form);
        this.forms.add(form);
    }

    public void addFormListener(GroupFormListener<OUTPUT> formListener) {
        if( formListener == null) return;
        if( this.listeners.contains(formListener)) return;
        this.listeners.add(formListener);
    }

    public void removeFormListener(GroupFormListener formListener) {
        if( formListener == null) return;
        this.listeners.add(formListener);
    }

    protected void notifyOnBack() {
        for(GroupFormListener formListener : listeners) {
            formListener.onBack();
        }
    }

    protected void notifyOnValidate( ValidationResult validationResult) {
        for(GroupFormListener formListener : listeners) {
            formListener.onValidate(validationResult);
        }
    }

    protected void notifyOnFinished( OUTPUT object) {
        for(GroupFormListener formListener : this.listeners) {
            formListener.onFinished(object);
        }
    }

    @Override
    public void construct() {
        this.pad(15f);
        super.construct();

        this.row();

        Table buttonTable = new Table();
        buttonTable.add(this.backButton).left();
        buttonTable.add()
                .minSize(120, 50)
                .padLeft(25)
                .padRight(25);

        buttonTable.add(this.startButton).right();
        buttonTable.pack();

        add(buttonTable).fill();

        this.pack();
    }

    public Form.ValidationStatus getValidationStatus() {
        return validationStatus;
    }

    public void validateForms() {
        ValidationResult validationResult = new ValidationResult();
        switch( this.validationMode ) {
            case ALL: {
                for( Form form : this.forms) {
                    form.validateForm(validationResult);
                }
            }
            case SOLO:
            {
                // current content may not  be a form
                if( stackController.getCurrentContent() instanceof Form) {
                    Form form = (Form) stackController.getCurrentContent();
                    form.validateForm(validationResult);
                }
            }
        }
        this.validationStatus = validationResult.getStatus();
        this.showValidationResults( validationResult );

        this.notifyOnValidate(validationResult);

        if( validationResult.getStatus() == Form.ValidationStatus.VALID) {
            this.notifyOnFinished( getObject() );
        }
    }

    /**
     * Shows validation messages on the form container.
     * @param validationResult
     */
    public void showValidationResults(ValidationResult validationResult) {

        switch ( validationResult.getStatus() ) {
            case VALID:
                this.messageLabel.setStyle(getSkin().get("empty", IconLabel.IconLabelStyle.class ));
                break;
            case INVALID:
                this.messageLabel.setStyle(getSkin().get("warning", IconLabel.IconLabelStyle.class ));
                break;
            case UNTOUCHED:
                this.messageLabel.setStyle(getSkin().get("empty", IconLabel.IconLabelStyle.class ));
                break;
        }

        // TODO Reinstate, this needs to be called on validating the form but only on demand
//        for( ValidationResult.ValidationMessage message : validationResult.getMessages() ) {
//            AstraXApp.astraX.showMessageAlert( new Message(Message.MessageType.EXCEPTION, message.message));
//        }
//        if( validationResult.getStatus() == Form.ValidationStatus.VALID && this.successMessage != null) {
//            AstraXApp.astraX.showSuccessAlert(this.successMessage);
//        }
    }

    public abstract OUTPUT getObject();
}
