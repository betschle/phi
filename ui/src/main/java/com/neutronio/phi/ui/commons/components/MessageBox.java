package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pools;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.AstraXComponent;
import com.neutronio.phi.ui.commons.buttons.AstraXTextButton;

/**
 * A simple message box with a message and a button.
 * This message box is intended to be placed on the top layer of all components.
 * When the user presses the main button, a ChangedEvent is triggered.
 */
public class MessageBox extends AstraXComponent {

    // TODO make messagebox compatible with Message? Need styles for that
    // TODO this component is not modular enough
    private Label messageLabel;
    private Image icon; // optional
    private AstraXTextButton okButton;

    public static class MessageBoxStyle {
        // TODO ??
    }

    public MessageBox(ComponentFactory componentFactory) {
        super(componentFactory, "window-body");
        if( this.background != null) {
            this.pad(this.background.getStyle().padding);
        }
        this.messageLabel = new Label("", componentFactory.getSkin(), "default");
        this.messageLabel.setAlignment(Align.center, Align.center);

        this.okButton = new AstraXTextButton( componentFactory, "primary");
        this.okButton.setText("OK");
        this.okButton.setCanCheck(false);
        this.okButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);
                fire(changeEvent);
                Pools.free(changeEvent);
            }
        });
    }

    /**
     * An optional icon for the message box.
     * @param iconName
     */
    public void setIcon(String iconName) {
        if( iconName != null && this.icon == null) {
            this.icon = new Image ( getSkin().getDrawable( iconName ) );
        } else
        if( this.icon != null) {
            this.icon.setVisible(false);
        }
    }

    public void setText(String text) {
        this.messageLabel.setText(text);
        this.pack();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void construct() {
        this.clear();
        super.construct();
        if( this.icon != null && this.icon.isVisible() ) {
            this.add(this.icon).pad(20).top();
            this.add(this.messageLabel).padTop(20).padRight(20).padBottom(10).top();
            this.row();
            this.add();
            this.add(this.okButton).minWidth(140).right();
        } else {
            this.add(this.messageLabel).pad(20).fill();
            this.row();
            this.add(this.okButton).minWidth(140).right();
        }

        this.pack();
    }
}
