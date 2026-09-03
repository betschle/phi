package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.buttons.AstraXButton;
import com.neutronio.phi.ui.commons.buttons.SoloButtonSelection;

/**
 * User interface for basic live player features such as:
 * play, stop, next, previous
 */
public class PlayerControlWidget extends HorizontalGroup {
    AstraXButton playButton;
    AstraXButton stopButton;
    AstraXButton pauseButton;
    AstraXButton nextButton;
    AstraXButton previousButton;

    SoloButtonSelection<AstraXButton> buttonGroup;
    UserPlayerListener listener;

    public interface UserPlayerListener {
        void onPlay();
        void onStop();
        void onPause();
        void onNext();
        void onPrevious();
    }

    public PlayerControlWidget(ComponentFactory componentFactory) {
        this.space(5);
        this.buttonGroup = new SoloButtonSelection<>();
        this.buttonGroup.setUncheckLast(true);

        this.playButton = new AstraXButton(componentFactory, "icon_inline_play", "primary-no-check");
        this.playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if( getListener() != null) getListener().onPlay();
            }
        });

        this.stopButton = new AstraXButton(componentFactory, "icon_inline_stop", "primary-no-check");
        this.stopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if( getListener() != null) getListener().onStop();
            }
        });

        this.pauseButton = new AstraXButton(componentFactory, "icon_inline_pause", "primary-no-check");
        this.pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if( getListener() != null) getListener().onPause();
            }
        });

        this.nextButton = new AstraXButton(componentFactory, "icon_inline_next", "primary-no-check");
        this.nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if( getListener() != null) getListener().onNext();
            }
        });

        this.previousButton = new AstraXButton(componentFactory, "icon_inline_previous", "primary-no-check");
        this.previousButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if( getListener() != null) getListener().onPrevious();
            }
        });

        this.buttonGroup.add(this.playButton, this.stopButton, this.pauseButton,
                             this.nextButton, this.previousButton);

        this.addActor(this.previousButton);
        this.addActor(this.playButton);
        this.addActor(this.pauseButton);
        this.addActor(this.stopButton);
        this.addActor(this.nextButton);
    }

    public UserPlayerListener getListener() {
        return listener;
    }

    public void setListener(UserPlayerListener listener) {
        this.listener = listener;
    }
}
