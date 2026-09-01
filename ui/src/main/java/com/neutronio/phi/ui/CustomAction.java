package com.neutronio.phi.ui;

import com.badlogic.gdx.scenes.scene2d.Action;

public class CustomAction extends Action {

    private Runnable runnable;

    public CustomAction(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public boolean act(float delta) {
        this.runnable.run();
        return true;
    }

    public static CustomAction get(Runnable runnable) {
        return new CustomAction( runnable );
    }
}
