package com.neutronio.phi.sfx;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

/**
 * A Sound Channel for static sounds, i.e. global ui sounds.
 */
public class StaticSoundChannel extends SoundChannel<StaticSoundChannel.StaticSoundInstance>{

    /**
     * A static and looping sound instance used in {@link StaticSoundChannel}
     * for keeping track and modifying them.
     */
    public static class StaticSoundInstance implements SoundInstance {

        /** OpenAL id obtained when playing the sound */
        public long id;
        /** The sound itself */
        public Sound sound;
        /** This value is only updated and relevant when looping a sound. */
        public boolean isPlaying;
        /** Variable set when the sound is used in a loop. Does not change afterwards unlike isPlaying */
        public boolean isLooped;

        public StaticSoundInstance(long id, Sound sound) {
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

    /**
     * @param name name of the sound channel, e.g. use "ui" or "sfx" etc.
     */
    public StaticSoundChannel(String name) {
        super(name);
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
        Sound sound = this.soundContainer.getSound(id);
        if( sound != null) {
            long playedID = sound.play();
            sound.setVolume(playedID, this.maxVolume * mult);
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
        Sound sound = this.soundContainer.getSound(id);
        if( sound != null) {
            long loopingSound = sound.loop(this.maxVolume);
            this.soundInstances.add(new StaticSoundInstance(loopingSound, sound));
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
        for(StaticSoundInstance sound : this.soundInstances) {
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
        for(StaticSoundInstance sound: soundInstances) {
            if(sound.id == soundId) {
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
        for(StaticSoundInstance sound : soundInstances) {
            if( sound.id == soundId) {
                sound.sound.setVolume(soundId, MathUtils.clamp(volume, 0, this.maxVolume) );
                sound.sound.setPitch(soundId, MathUtils.clamp(pitch, 0, 2));
                break;
            }
        }
    }

    @Override
    public void updateVolume() {
        for(StaticSoundInstance sounds : soundInstances) {
            sounds.sound.setVolume(sounds.id, this.maxVolume);
        }
    }
}
