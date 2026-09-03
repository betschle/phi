package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.AstraXComponent;
import com.neutronio.phi.ui.commons.buttons.AstraXTextButton;
import com.neutronio.phi.ui.commons.buttons.StaticIcon;
import com.neutronio.phi.ui.skin.PhiUITranslations;

import java.util.ArrayList;
import java.util.List;

/**
 * A DialogBox that Queries a yes or no / proceed or cancel from the user. Typical use case is
 * "Are you sure you want to do X?" to which an answer is prompted by the user.
 */
public class DialogBox extends AstraXComponent {

    private Label messageLabel;
    private StaticIcon icon;
    private AstraXTextButton proceedButton;
    private AstraXTextButton cancelButton;

    /** A set of listeners for UI state changes */
    private List<DialogBoxListener> listeners = new ArrayList<>();
    /** A single callback listener object. This callback can change externally and will be executed
     * last. */
    private DialogBoxListener callback;

    public DialogBox(ComponentFactory componentFactory) {
        super(componentFactory, "window-body");

        this.messageLabel = new Label("", componentFactory.getSkin(), "default");
        this.messageLabel.setAlignment(Align.center, Align.center);

        this.proceedButton = new AstraXTextButton( componentFactory, "primary");
        this.proceedButton.setText(componentFactory.translate(PhiUITranslations.BUTTON_TEXT_PROCEED));
        this.proceedButton.setCanCheck(false);
        this.proceedButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                for( DialogBoxListener listener : listeners) {
                    listener.onProceed();
                }
                if( getCallback() != null) getCallback().onProceed();

            }
        });

        this.cancelButton = new AstraXTextButton( componentFactory, "secondary");
        this.cancelButton.setText(componentFactory.translate(PhiUITranslations.BUTTON_TEXT_CANCEL));
        this.cancelButton.setCanCheck(false);
        this.cancelButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                for( DialogBoxListener listener : listeners) {
                    listener.onCancel();
                }
                if( getCallback() != null) getCallback().onCancel();
            }
        });

        this.icon = new StaticIcon(componentFactory, "primary-round-28x28");
    }

    public interface DialogBoxListener {
        void onProceed();
        void onCancel();
    }

    /**
     * Adds a permanent listener to this DialogBox to react to UI changes
     * @param listener
     */
    public void addListener(DialogBoxListener listener) {
        if( listener == null) return;
        this.listeners.add(listener);
    }

    /**
     * Removes a permanent listener from this DialogBox
     * @param listener
     */
    public void removeListener(DialogBoxListener listener) {
        if( listener == null) return;
        this.listeners.remove(listener);
    }

    /**
     * Gets the callback which is used to react to user choice.
     * @return
     */
    private DialogBoxListener getCallback() {
        return callback;
    }

    /**
     * Sets the callback which is used to react to user choice.
     * @param callback
     */
    public void setCallback(DialogBoxListener callback) {
        this.callback = callback;
    }

    /**
     * An optional icon for the message box.
     * @param iconName
     */
    public void setIcon(String iconName) {
        if( iconName == null) {
            this.icon.setVisible(false);
            return;
        }
        this.icon.setInlineIcon(iconName);
    }

    public void setText(String text) {
        this.messageLabel.setText(text);
        this.pack();
    }

    /**
     *
     * @param cancelText a translation modifier string for cancel
     * @param proceedText a translation modifier string for cancel
     */
    public void setButtonTexts(String cancelText, String proceedText) {
        this.proceedButton.setText(componentFactory.translate(proceedText));
        this.cancelButton.setText(componentFactory.translate(cancelText));
    }

    @Override
    public void construct() {
        this.clear();
        super.construct();
        this.pad(this.background.getStyle().padding);
        if( this.icon.isVisible() ) {
            this.add(this.icon).pad(20).center();
            this.add(this.messageLabel).padTop(20).padRight(20).padBottom(30).top();
            this.row();
            this.add();
        } else {
            this.add(this.messageLabel).padTop(20).padBottom(30).fill();
            this.row();
        }

        Table buttonGroup = new Table();
        buttonGroup.add(this.cancelButton).minWidth(140).right();
        buttonGroup.add().minWidth(120);
        buttonGroup.add(this.proceedButton).minWidth(140).right();

        this.add(buttonGroup).bottom().colspan(2);
        this.pack();
    }
}
