package com.neutronio.phi.sfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.neutronio.phi.PhiException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class to load and unload sounds from {@link SoundChannel}
 */
public class SoundLoader {

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    /**
     * Loads sounds into the specified sound channel
     * @param channel
     * @param directory
     * @param settings
     */
    public void load(SoundChannel channel, String directory, SoundSettings settings) {
        List<SoundSettings.SoundToLoad> soundsToLoad = settings.getSoundsToLoad(channel.getName());
        if(soundsToLoad.isEmpty())
            logger.log(Level.WARNING, "No sounds found to load for sound group '%s'", channel.getName());
        for(SoundSettings.SoundToLoad sound : soundsToLoad) {
            logger.log(Level.INFO, sound.getIdentifier() + " @ " + sound.getPath());
            channel.getSounds().addSound(sound.getIdentifier(), Gdx.audio.newSound(Gdx.files.local(directory + sound.getPath())));
        }
    }

    /**
     * Unloads sounds from a sound channel based on a settings object
     * @param channel the sound channel to remove the sounds from
     * @param settings the object that contains sound configuration to remove
     */
    public void unload(SoundChannel channel, SoundSettings settings) {
        logger.log(Level.INFO, "Unloading sounds from manager %s", channel.getName());
        List<SoundSettings.SoundToLoad> sounds = settings.getSoundsToLoad(channel.getName());
        if(sounds == null) throw new PhiException(PhiException.ErrorCode.E0004, "Sounds empty!");
        for(SoundSettings.SoundToLoad sound : sounds) {
            Sound soundObject = channel.getSounds().getSound(sound.identifier);
            // stop all sound instances belonging to SoundToLoad and dispose sound object
            soundObject.stop();
            soundObject.dispose();
            channel.getSounds().removeSound(sound.identifier);
        }
    }
}
