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

        public String getIdentifier() {
            return identifier;
        }

        public String getPath() {
            return path;
        }
    }

    /**
     * Sound groups to load
     */
    private Map<String, List<SoundToLoad>> soundsToLoad = new HashMap<>();

    public List<SoundToLoad> getSoundsToLoad(String category) {
        return soundsToLoad.get(category);
    }

    /**
     *
     * @param category sfx or ui, which go with its own soundmanager
     * @param soundsToLoad
     */
    public void addSoundsToLoad(String category, List<SoundToLoad> soundsToLoad) {
        this.soundsToLoad.put(category, soundsToLoad);
    }

    public void addSoundsToLoad(String category, String identifier, String path) {
        List<SoundToLoad> soundToLoad = this.soundsToLoad.get(category);
        if ( soundToLoad == null) {
            soundToLoad = new ArrayList<>();
            this.soundsToLoad.put(category, soundToLoad);
        }
        soundToLoad.add(new SoundToLoad(identifier, path));
    }
}
