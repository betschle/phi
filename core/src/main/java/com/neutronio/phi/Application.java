package com.neutronio.phi;

import com.neutronio.phi.app.Message;
import com.neutronio.phi.app.PanningCamera;
import com.neutronio.phi.app.PhiStage;


/**
 * Central Application functionality that must exist in a game, in order to use phi and its subprojects
 * (particularly ui and atlas).
 */
public interface Application {

    /**
     * Shows a global dialog box that is intended to confirm an action
     * @param message
     * @param onCancel code to execute when the user cancels the dialog
     * @param onProceed code to execute when the user proceeds with the dialog
     */
    void showDialogBox(String message, Runnable onProceed, Runnable onCancel);

    /**
     * Shows a global message box that is intended to display hints or important
     * messages that require user input to remove
     * @param message
     * @param icon an icon string drawable reference
     */
    void showMessageBox(String message, String icon);

    /**
     * Shows alerts in the application-wide alert stack.
     * @param messages
     */
    void showMessageAlert(Message... messages);

    /**
     * Adds a quick info message to be displayed application-wide.
     * @param message
     */
    void showSuccessAlert(String... message);

    /**
     * Adds a info message to be displayed application-wide. Use sparely and only for
     * important messages.
     * @param message
     */
    void showInfoAlert(String... message);

    /**
     * Adds a quick info message to be displayed application-wide with a customizable Sound
     * @param message
     */
    void showInfoAlert(String customSound, String message);

    /**
     * Adds a quick info message to be displayed application-wide.
     * @param message
     */
    void showWarningAlert(String... message);

    /**
     * Adds a quick info message to be displayed application-wide.
     * @param message
     */
    void showErrorAlert(String... message);

    void playSoundOnce(String soundID);

    /**
     * Gets the stage of a registered id
     * Intended to replace getUISTage, getWorldStage etc
     * @param id
     * @return
     */
    PhiStage getStage(String id);

    /**
     * Gets the camera of the application
     * @return
     */
    PanningCamera getCameraController(); // TODO that actually belongs to stage

    // TODO add a converter method to convert from ValidationResult.ValidationMessage -> Message
//    /**
//     * Adds a quick info message to be displayed application-wide
//     */
//    void showErrorAlert(List<ValidationResult.ValidationMessage> messages);
}
