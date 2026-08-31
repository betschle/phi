package com.neutronio.phi.sfx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This object organizes sounds in different groups, which are mapped to
 * one sound manager (e.g. group "ui" maps to a sound manager of name "ui").
 */
public class SoundSettings {

    /**
     * Maps a sound loaded from a path to an identifier, by which
     * it can be played and referenced.
     */
    public static class SoundToLoad {
        public String identifier;
        public String path;

        public SoundToLoad( String identifier, String path) {
            this.identifier = identifier;
            this.path = path;
        }
    }

    /**
     * Sound groups to load
     */
    private Map<String, List<SoundToLoad>> soundGroups = new HashMap<>();

    /**
     * Gets all sounds inside of a sound group or category
     * @param category
     * @return
     */
    public List<SoundToLoad> getSoundsToLoad(String category) {
        return this.soundGroups.get(category);
    }

    /**
     * Sets the sounds for a category, overwrites existing value for category
     * @param category the category or group to use, e.g.  sfx or ui
     * @param soundsToLoad
     */
    public void addSoundsToLoad(String category, List<SoundToLoad> soundsToLoad) {
        this.soundGroups.put(category, soundsToLoad);
    }

    /**
     * Adds a sound to load
     * @param category the category or group to use, e.g.  sfx or ui
     * @param identifier the identifier by which the sound is accessed to
     * @param filename the file name
     */
    public void addSoundsToLoad(String category, String identifier, String filename) {
        List<SoundToLoad> soundToLoad = this.soundGroups.get(category);
        if ( soundToLoad == null) {
            soundToLoad = new ArrayList<>();
            this.soundGroups.put(category, soundToLoad);
        }
        soundToLoad.add(new SoundToLoad(identifier, filename));
    }
}
