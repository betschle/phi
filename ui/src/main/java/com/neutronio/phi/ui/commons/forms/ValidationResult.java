package com.neutronio.phi.ui.commons.forms;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

/**
 * A validation result for a form.
 */
public class ValidationResult {

    // TODO Validation improvements: When validating I should be able to mark those components where an error occured
    private Form.ValidationStatus status = Form.ValidationStatus.VALID;
    private List<ValidationMessage> messages = new ArrayList<>();

    public static class ValidationMessage {
        /** A validation message */
        public String message;
        /** The component that caused the validation message. */
        public Actor component;

        public ValidationMessage(String message, Actor component) {
            this.message = message;
            this.component = component;
        }
    }

    public ValidationResult(){

    }

    public ValidationResult status( Form.ValidationStatus status ) {
        this.status = status;
        return this;
    }

    public ValidationResult addMessage(String message ) {
        this.messages.add( new ValidationMessage(message, null) );
        return this;
    }

    public ValidationResult addMessage(String message, Actor affectedComponent) {
        this.messages.add( new ValidationMessage(message, affectedComponent));
        return this;
    }

    public Form.ValidationStatus getStatus() {
        return status;
    }

    public List<ValidationMessage> getMessages() {
        return messages;
    }
}
