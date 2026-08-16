package com.neutronio.phi.sfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.neutronio.phi.PhiException;
import com.neutronio.phi.util.metrics.UsageChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Conveniently meant to play and loop sounds & adjusts their volume to a defined maximum volume.
 * There should be SoundManagers for different uses, e.g. for ui or game sounds. Each uses their own
 * maximum volume.
 */
public class SoundManager {

    protected Logger logger;
    protected UsageChecker usageChecker = new UsageChecker();
    /** Cached sounds */
    protected Map<String, Sound> sounds = new HashMap<>();
    /** Sound instances currently being played */
    protected List<SoundInstance> soundInstances = new ArrayList<>();
    /** The maximum volume allowed in this manager */
    protected float maxVolume = 1f;
    /** Name of this sound manager, for debugging purposes. */
    protected String name;

    /**
     *
     * @param name name of the sound manager, e.g. use "ui" or "sfx" etc.
     */
    public SoundManager(String name) {
        this.name = name;
        this.logger = Logger.getLogger("SoundManager["+name+"]");
    }

    public float getMaxVolume() {
        return maxVolume;
    }

    /**
     * Checks if sound registered with soundID exists
     * @param soundID
     * @return
     */
    public boolean hasSound( String soundID ) {
        return this.sounds.containsKey(soundID);
    }

    /**
     * Adds a sound, ready to play
     * @param soundID
     * @param sound
     */
    public void addSound( String soundID, Sound sound) {
        if( soundID != null && sound != null) {
            this.usageChecker.addId(soundID);
            this.sounds.put(soundID, sound);
        } else {
            Gdx.app.debug(this.getClass().getCanonicalName(), "Could not add sound!");
        }
    }

    /**
     * Sets the maximum volume to be used for this manager
     * @param maxVolume
     */
    public void setMaxVolume(float maxVolume) {
        this.maxVolume = maxVolume;
    }

    /** Plays a shuffled sound */
    public void playSound(ShuffleSound shuffleSound) {
        this.playSoundOnce(shuffleSound.getSoundID() + MathUtils.random(1, shuffleSound.getSoundCount()));
    }

    /**
     * Plays a sound once
     * @param id the id of the sound to play
     */
    public void playSoundOnce(String id) {
        this.playSoundOnce(id, 1f);
    }

    public void playSoundOnce(String id, float mult) {
        Sound sound = this.sounds.get(id);
        if( sound != null) {
            long playedID = sound.play();
            sound.setVolume(playedID, this.maxVolume * mult);
            this.usageChecker.record(id);
            this.logger.finest("Playing sound " + id);
        } else {
            this.logger.warning("Could not find sound of ID " + id);
        }
    }

    /**
     * Loops a sound. Sound must be stopped
     * @param id an id referring to the registered sound
     * @return the sound id of the sound instance
     */
    public long playSoundAsLoop(String id) {
        Sound sound = this.sounds.get(id);
        if( sound != null) {
            long loopingSound = sound.loop(this.maxVolume);
            this.usageChecker.record(id);
            this.soundInstances.add(new SoundInstance(loopingSound, sound));
            return loopingSound;
        }
        return 0;
    }

    /**
     * Stops a previously looped sound.
     * @param soundId the sound ID
     */
    public void stopSoundLoop(long soundId) {
        SoundInstance looped = null;
        for( SoundInstance sound : soundInstances) {
            if( sound.id == soundId) {
                sound.sound.stop(soundId);
                looped = sound;
            }
        }
        if( looped != null) {
            soundInstances.remove(looped);
        }
    }

    /**
     * Modifies the volume of a looping sound.
     * @param soundId the sound ID
     */
    public void modifyLoopingSound(long soundId, float volume) {
        for( SoundInstance sound : soundInstances) {
            if( sound.id == soundId) {
                sound.sound.setVolume(soundId, MathUtils.clamp(volume, 0, this.maxVolume) );
                break;
            }
        }
    }

    /**
     * Modifies the volume and pitch of a looping sound.
     * @param soundId the sound ID
     */
    public void modifyLoopingSound(long soundId, float volume, float pitch) {
        for( SoundInstance sound : soundInstances) {
            if( sound.id == soundId) {
                sound.sound.setVolume(soundId, MathUtils.clamp(volume, 0, this.maxVolume) );
                sound.sound.setPitch(soundId, MathUtils.clamp(pitch, 0, 2));
                break;
            }
        }
    }

    /**
     * Updates the volume on all looping sound instances. To be invoked
     * when the maximum volume was changed.
     */
    public void updateVolume() {
        for( SoundInstance sounds : soundInstances) {
            sounds.sound.setVolume( sounds.id, this.maxVolume );
        }
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
            this.sounds.put(sound.identifier, null);
        }
    }

    @Override
    public String toString() {
        return "SoundManager{" +
            "maxVolume=" + maxVolume +
            ", name='" + name + '\'' +
            '}';
    }
}
