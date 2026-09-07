package com.neutronio.phi.sfx;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Abstract Sound Channel class for audio implementation. Describes
 * a channel of audio to play sounds in, with its own maximum volume and sound
 * lists.
 *
 * @param <S>
 */
public abstract class SoundChannel<S extends SoundInstance> {

    protected Logger logger;
    /** Available sounds to play in this channel */
    protected SoundContainer soundContainer = new SoundContainer();
    /** Sound instances currently being played */
    protected List<S> soundInstances = new ArrayList<>();
    /** The maximum volume allowed in this channel */
    protected float maxVolume = 1f;
    /** Name of this sound channel, for debugging purposes. */
    protected String name;

    /**
     *
     * @param name name of the sound channel, e.g. use "ui" or "sfx" etc.
     */
    public SoundChannel(String name) {
        this.name = name;
        this.logger = Logger.getLogger("SoundChannel["+name+"]");
    }

    /**
     *
     * @return name of the sound channel, e.g. use "ui" or "sfx" etc.
     */
    public String getName() {
        return name;
    }

    public float getMaxVolume() {
        return maxVolume;
    }

    public SoundContainer getSounds() {
        return this.soundContainer;
    }

    /**
     * Sets the maximum volume to be used for this manager
     * @param maxVolume
     */
    public void setMaxVolume(float maxVolume) {
        this.maxVolume = maxVolume;
    }

    /**
     * Updates the volume on all looping sound instances. To be invoked
     * when the maximum volume was changed.
     */
    public abstract void updateVolume();
}
