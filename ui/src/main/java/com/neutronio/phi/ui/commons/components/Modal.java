package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.Tweening;

import java.util.logging.Logger;

/**
 * A Modal dialog, includes an exit button
 */
public class Modal<C extends Actor> extends Group {

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
    protected C component;
    protected Table content;
    protected Image background;

    protected Header header;

    public Modal(final ComponentFactory componentFactory, C actor) {
        this.component = actor;

        this.background = new Image();
        this.background.setDrawable(new NinePatchDrawable(componentFactory.getSkin().getPatch("background_rect_frame_tiny") ));
        this.background.setColor(0, 0, 0, 0.5f);
        this.background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.background.setVisible(false);
        this.background.setTouchable(Touchable.disabled);

        this.header = new Header(componentFactory);
        this.header.construct();
        this.header.getExitButton().addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if( !header.getExitButton().isDisabled() ) {
                    hide();
                }
            }
        });
        this.content = new Table();
        this.content.add(this.header).colspan(2).fill(); //
        this.content.row();
        this.content.add(this.component).colspan(2).minWidth(300);
        this.content.pack();
        this.content.setVisible(false);

        this.addActor(this.background);
        this.addActor(this.content);

        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                logger.finest("Actor target: " + event.getTarget().getClass()
                        + ", bubbles: " + event.getBubbles()
                        + ", content visible: " + content.isVisible()
                        + ", background touchable: " + background.getTouchable()
                );
                if( !event.getBubbles() ) return;
                // hide the modal if it bugs out
                if( !content.isVisible() && background.isVisible() ) {
                    logger.warning("Modal bugging out!");
                    hide();
                }
                getStage().setKeyboardFocus(event.getListenerActor());
            }
        });
        this.addListener( new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if( content.isVisible() && keycode == Input.Keys.ESCAPE && !header.exitButton.isDisabled()) {
                    logger.finest("ESC was pressed!");
                    hide();
                    return true;
                }
                return false;
            }
        });
    }

    public void pack() {
        this.content.pack();
    }

    public Table getContent() {
        return content;
    }

    /**
     * If the exit button in the modal header should be disabled.
     * Important: If the exit button is disabled, auto-close via escape key is disabled
     * and the modal must be closeable via other means.
     * @param disabled
     */
    public void setExitButtonDisabled(boolean disabled) {
        this.header.exitButton.setDisabled(disabled);
    }

    /**
     * Customizes the header icon
     * @param inlineIcon
     */
    public void setHeaderIcon(String inlineIcon) {
        this.header.setHeaderIcon(inlineIcon);
    }

    /**
     * Customizes the header text
     * @param text
     */
    public void setHeaderText(String text) {
        this.header.setTitle(text);
    }

    public void hide() {
        logger.finest("Hide modal");
        this.background.addAction(Actions.sequence(
                                    Actions.alpha(0, 0.2f),
                                    Actions.touchable(Touchable.disabled),
                                    Actions.hide()
                                    )
        );
        this.background.addAction(Actions.alpha(0, 0.2f));
        this.content.addAction(Tweening.getCenteredFadeOut());
    }

    public void show() {
        logger.finest("Show modal");
        this.background.addAction(Actions.sequence(
                                    Actions.show(),
                                    Actions.touchable(Touchable.enabled),
                                    Actions.alpha(0.5f, 0.2f)
                                    )
        );
        this.content.addAction(Tweening.getCenteredFadeIn());
    }
}
