package com.neutronio.phi.sfx;

import com.badlogic.gdx.audio.Sound;

/**
 * A looping sound instance used in {@link SoundManager}
 * for keeping track and modifying them.
 */
public class SoundInstance {

    /** OpenAL id obtained when playing the sound */
    protected long id;
    /** The sound itself */
    protected Sound sound;
    /** This value is only updated and relevant when looping a sound. */
    protected boolean isPlaying;
    /** Variable set when the sound is used in a loop. Does not change afterwards unlike isPlaying */
    protected boolean isLooped;

    public SoundInstance(long id, Sound sound) {
        this.id = id;
        this.sound = sound;
    }

    /**
     * Loops this sound instance.
     */
    public void loop(float volume) {
        if( this.isPlaying ) return;
        this.isPlaying = true;
        this.isLooped = true;
        this.id = sound.loop(volume);
    }

    public void resume() {
        if( this.isLooped && !this.isPlaying ) {
            this.sound.resume(this.id);
            this.isPlaying = true;
        }
    }

    public void pause() {
        if( this.isLooped ) {
            this.sound.pause(this.id);
            this.isPlaying = false;
        }
    }

    /**
     * Plays this sound instance once.
     */
    public void play(float volume) {
        this.id = sound.play(volume);
    }

    /**
     * Stops the sound instance. Only relevant when playing on loop.
     */
    public void stop() {
        if( this.isPlaying ) {
            this.sound.stop(this.id);
            this.id = -1;
            this.isPlaying = false;
        }
    }
}

