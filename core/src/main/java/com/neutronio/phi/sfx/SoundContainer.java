package com.neutronio.phi.sfx;

import com.badlogic.gdx.audio.Sound;
import com.neutronio.phi.PhiException;
import com.neutronio.phi.util.metrics.UsageChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used for loading sounds. Use {@link SoundLoader} and
 * an instance of {@link SoundChannel} to load sounds into.
 */
public class SoundContainer {
    protected Logger logger;
    /** Cached sounds */
    protected Map<String, Sound> sounds = new HashMap<>();
    protected UsageChecker usageChecker = new UsageChecker();
    /**
     * Checks if sound registered with soundID exists
     * @param soundID
     * @return
     */
    public boolean hasSound( String soundID ) {
        return this.sounds.containsKey(soundID);
    }

    public Sound getSound(String soundID) {
        // TODO add usage checker
        return sounds.get(soundID);
    }
    /**
     * Adds a sound, ready to play
     * @param soundID
     * @param sound
     */
    public void addSound(String soundID, Sound sound) {
        if( soundID != null && sound != null) {
            this.usageChecker.addId(soundID);
            this.sounds.put(soundID, sound);
        } else {
            logger.log(Level.WARNING, "Could not add sound!");
        }
    }

    /**
     * Effectively unloads a sound by removing it from this container
     * @param soundID
     */
    public void removeSound(String soundID) {
        this.sounds.remove(soundID);
    }

    /**
     * Stop all sound instances belonging to the list sound and dispose the gdx sound object.
     * Also unregisters the sounds, leaving them unavailable to be played.
     * @param sounds
     */
    public void unloadSounds(List<SoundSettings.SoundToLoad> sounds) {
        if( sounds == null) throw new PhiException(PhiException.ErrorCode.E0004, "Sounds empty!");
        for( SoundSettings.SoundToLoad sound : sounds) {
            Sound soundObject = this.sounds.get(sound.identifier);
            // stop all sound instances belonging to SoundToLoad and dispose sound object
            soundObject.stop();
            soundObject.dispose();
            this.sounds.remove(sound.identifier);
        }
    }
}
