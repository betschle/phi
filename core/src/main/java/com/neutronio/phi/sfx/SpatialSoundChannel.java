package com.neutronio.phi.sfx;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * A sound channel for spatial sounds, that change panning depending on
 * relative position to the observer camera. To control played instances, create
 * and use a SpatialSoundInstance.
 */
public class SpatialSoundChannel extends SoundChannel<SpatialSoundChannel.SpatialSoundInstance> {
    /** The maximum distance at which sounds are being played */
    private float maxDistance = 5000f;
    private OrthographicCamera camera;
    /** The sounds currently being updated and played */
    private List<SpatialSoundInstance> spatialSounds = new ArrayList<>();

    public class SpatialSoundInstance implements SoundInstance {

        protected StaticSoundChannel.StaticSoundInstance staticSoundInstance;
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
            if( staticSoundInstance != null) return;
            Sound sound = soundContainer.getSound(id);
            if( sound != null ) {
                staticSoundInstance = new StaticSoundChannel.StaticSoundInstance(-1, sound);
            } else {
                logger.severe("Sound of ID '" + id + "' not found!");
            }
        }

        /**
         * Loops this spatial sound.
         */
        public void loop() {
            if( staticSoundInstance == null) return;
            staticSoundInstance.loop(volume * this.volumeMultiplier);
        }

        /**
         * Plays this spatial sound once.
         */
        public void play() {
            if( staticSoundInstance == null) return;
            staticSoundInstance.play(volume * this.volumeMultiplier);
        }

        public void pause() {
            if( staticSoundInstance == null) return;
            staticSoundInstance.pause();
        }

        public void resume() {
            if( staticSoundInstance == null) return;
            staticSoundInstance.resume();
        }

        public void stop() {
            if( staticSoundInstance == null) return;
            staticSoundInstance.stop();
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
            // TODO current sound volume must still be clamped to max volume
            this.volume = MathUtils.clamp(1 - (distance / maxDistance), 0f, 1f);

            this.staticSoundInstance.sound.setPan(this.staticSoundInstance.id, this.panning, MathUtils.clamp( this.volume*this.volumeMultiplier, 0f, maxVolume) );
            this.staticSoundInstance.sound.setPitch(this.staticSoundInstance.id, this.pitch);
        }
    }

    /**
     * @param name name of the sound channel, e.g. use "ui" or "sfx" etc.
     */
    public SpatialSoundChannel(String name) {
        super(name);
    }

    @Override
    public void updateVolume() {
        // TODO current sound volume must still be clamped to max volume
//        for(SpatialSoundInstance sound : soundInstances) {
//            sound.staticSoundInstance.sound.setVolume(sounds.id, this.maxVolume);
//        }
    }

    /**
     * Sets the camera that acts as audio listener
     * @param camera
     */
    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    /**
     * Obtains a spatial sound object that controls volume and panning according to
     * distance and relative location to observer and sound.
     * @param soundId
     * @return null if the camera was not set yet
     */
    public SpatialSoundInstance createSpatialSound(String soundId) {
        if( this.camera == null) return null;
        SpatialSoundInstance spatialSound = new SpatialSoundInstance();
        spatialSound.init(soundId);
        this.spatialSounds.add(spatialSound);
        return spatialSound;
    }

    /**
     * Resumes all registered spatial sounds.
     */
    public void resumeSpatialSounds() {
        for(SpatialSoundInstance sound : this.spatialSounds) {
            sound.resume();
        }
    }

    /**
     * Pauses all registered spatial sounds.
     */
    public void pauseSpatialSounds() {
        for(SpatialSoundInstance sound : this.spatialSounds) {
            sound.pause();
        }
    }
}
