package com.neutronio.phi.sfx;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Plays sound according to distance and location in a scene.
 */
public class SpatialSoundManager extends SoundManager {
    // TODO SpatialSoundManager and SoundManager need a common interface here to work with AstraX (possibly)
    //  I dont think SpatialSoundManager requires any of the regular SoundManager functionality.
    //  There are two flavors of similar functionality and the same structural pattern here that also
    //  require the same classes (SoundManager - logic, SoundInstance - logic helper class, SoundToLoad - class for loading)

    /** The maximum distance at which sounds are being played */
    private float maxDistance = 5000f;

    private OrthographicCamera camera;
    private List<LoopedSpatialSound> spatialSounds = new ArrayList<>();

    public class LoopedSpatialSound {
        protected SoundInstance soundInstance;
        /** The calculated distance to the camera*/
        protected float distance;

        /** Panning of sound. */
        protected float panning = 0;
        /** Volume of sound. Calculated from the distance to observer. */
        protected float volume = 0.5f;
        /** Customizable volume of sound. Multiplied with calculated volume. */
        protected float volumeMultiplier = 1f;
        /** Customizable pitch of sound so it can be modified as it plays. */
        protected float pitch = 1;

        /**
         * To be called only once.
         * @param id
         */
        public void init(String id) {
            if( soundInstance != null) return;
            Sound sound = sounds.get(id);
            if( sound != null ) {
                soundInstance = new SoundInstance(-1, sound);
                usageChecker.record(id);
            } else {
                logger.severe("Sound of ID '" + id + "' not found!");
            }
        }

        /**
         * Loops this spatial sound.
         */
        public void loop() {
            if( soundInstance == null) return;
            soundInstance.loop(volume * this.volumeMultiplier);
        }

        /**
         * Plays this spatial sound once.
         */
        public void play() {
            if( soundInstance == null) return;
            soundInstance.play(volume * this.volumeMultiplier);
        }

        public void pause() {
            if( soundInstance == null) return;
            soundInstance.pause();
        }

        public void resume() {
            if( soundInstance == null) return;
            soundInstance.resume();
        }

        public void stop() {
            if( soundInstance == null) return;
            soundInstance.stop();
        }

        public void setPitch(float pitch) {
            this.pitch = MathUtils.clamp( pitch, 0.5f, 2f);
        }

        /**
         * Sets a factor that modifies the calculated volume.
         * @param volumeMultiplier a factor 0-1
         */
        public void setVolumeMultiplier(float volumeMultiplier) {
            this.volumeMultiplier = MathUtils.clamp(volumeMultiplier, 0f, maxVolume);
        }

        public float getPanning() {
            return panning;
        }

        public float getVolume() {
            return volume;
        }

        public float getPitch() {
            return pitch;
        }

        public float getDistance() {
            return distance;
        }

        /**
         * Updates the spatial sound: Puts the camera coordinates in relation to
         * the coordinates of the sound source. Calculates pan and volume based on this.
         * @param delta
         * @param globalX The global X coordinates of the sound source
         * @param globalY The global Y coordinates of the sound source
         */
        public void update(float delta, float globalX, float globalY ) {
            this.distance = Vector2.dst(camera.position.x, camera.position.y,
                globalX,globalY);
            this.panning = -1 * MathUtils.clamp( (camera.position.x - globalX) / camera.viewportWidth, -1f, 1f);
            this.volume = MathUtils.clamp(1 - (distance / maxDistance), 0f, 1f);

            this.soundInstance.sound.setPan(this.soundInstance.id, this.panning, MathUtils.clamp( this.volume*this.volumeMultiplier, 0f, maxVolume) );
            this.soundInstance.sound.setPitch(this.soundInstance.id, this.pitch);
        }
    }

    public SpatialSoundManager(String name) {
        super(name);
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    /**
     * Obtains a spatial sound object that controls volume and panning according to
     * distance and relative location to observer and sound.
     * @param soundId
     * @return null if the camera was not set yet
     */
    public LoopedSpatialSound createSpatialSound(String soundId) {
        if( this.camera == null) return null;
        LoopedSpatialSound spatialSound = new LoopedSpatialSound();
        spatialSound.init(soundId);
        this.spatialSounds.add(spatialSound);
        return  spatialSound;
    }

    /**
     * Resumes all registered spatial sounds.
     */
    public void resumeSpatialSounds() {
        for( LoopedSpatialSound sound : this.spatialSounds) {
            sound.resume();
        }
    }

    /**
     * Pauses all registered spatial sounds.
     */
    public void pauseSpatialSounds() {
        for( LoopedSpatialSound sound : this.spatialSounds) {
            sound.pause();
        }
    }

    /**
     * Removes a spatial sound from this manager, e.g. when the sound carrier is being destroyed.
     * @param spatialSound
     */
    public void removeSpatialSound(LoopedSpatialSound spatialSound) {
        this.sounds.remove(spatialSound);
    }
}
