package com.neutronio.phi.app.screen;

import com.badlogic.gdx.Screen;

import java.util.logging.Logger;

/**
 * An Abstract Screen providing basic functionality for games Screens.
 * @param <A> the parent game container
 */
public abstract class PhiScreen<A> implements Screen {

    protected Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
    protected final A app; // TODO A = AstraXApp
    /** If this screen was loaded. */
    protected boolean loaded = false;
    /** If this screen is hidden. */
    protected boolean hidden = true;
    /** if this screen was paused. */
    protected boolean paused = false;
    /** if this screen was created. */
    protected boolean created = false;

    protected PhiScreen(A game) {
        this.app = game;
    }

    /**
     * Intended for inserting additional rendering code
     */
    public void renderOverride(float delta) {
    }
    /**
     * Intended for inserting additional update code
     */
    public void updateOverride(float delta) {}

    /**
     * Creates everything to display this screen without errors.
     */
    public abstract void create();

    @Override
    public void resize(int width, int height) {
        // TODO hook up to application listener
    }

    public boolean isLoaded() {
        return loaded;
    }

    protected void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    @Override
    public void show() {
        this.hidden = false;
    }

    @Override
    public void hide() {
        this.hidden = true;
    }

    @Override
    public void pause() {
        this.paused = true;
    }

    @Override
    public void resume() {
        this.paused = false;
    }

    public boolean isCreated() {
        return created;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isPaused() {
        return paused;
    }

    public A getApp() {
        return app;
    }
}
