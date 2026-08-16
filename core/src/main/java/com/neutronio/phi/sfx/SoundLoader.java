package com.neutronio.phi.sfx;

import com.badlogic.gdx.Gdx;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class to load and unload sounds from {@link SoundManager}
 */
public class SoundLoader {

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    /**
     * Creates a sound manager based on available settings
     * @param name the name of the sound manager, corresponds to groups found in settings
     * @param settings
     */
    public SoundManager createAndLoadSoundManager(String name, String directory, SoundSettings settings) {
        logger.log(Level.INFO, "Create and load sound manager %s", name);
        SoundManager soundManager = new SoundManager(name);
        List<SoundSettings.SoundToLoad> soundsToLoad = settings.getSoundsToLoad(name);
        if(soundsToLoad.isEmpty())
            logger.log(Level.WARNING, "No sounds found to load for sound group '%s'", name);
        for(SoundSettings.SoundToLoad sound : soundsToLoad) {
            logger.log(Level.INFO, sound.getIdentifier() + " @ " + sound.getPath());
            soundManager.addSound(sound.getIdentifier(), Gdx.audio.newSound(Gdx.files.local(directory + sound.getPath())));
        }
        return soundManager;
    }

    /**
     * Loads sounds from a settings object into an existing sound manager.
     * @param soundManager the sound manager to add the sounds to
     * @param directory additional directory
     * @param settings the object that contains sound configuration
     */
    public void load(SoundManager soundManager, String directory, SoundSettings settings) {
        logger.log(Level.INFO, "Loading sounds into manager %s", soundManager.name);
        for(SoundSettings.SoundToLoad sound : settings.getSoundsToLoad(soundManager.name)) {
            logger.log(Level.INFO, sound.getIdentifier() + " @ " + sound.getPath());
            soundManager.addSound(sound.getIdentifier(), Gdx.audio.newSound(Gdx.files.local(directory + sound.getPath())));
        }
    }

    /**
     * Unloads sounds based on a settings object
     * @param soundManager the sound manager to remove the sounds from
     * @param settings the object that contains sound configuration to remove
     */
    public void unload(SoundManager soundManager, SoundSettings settings) {
        logger.log(Level.INFO, "Unloading sounds from manager %s", soundManager.name);
        soundManager.unloadSounds(settings.getSoundsToLoad(soundManager.name));
    }
}
