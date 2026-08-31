package com.neutronio.phi.sfx;

/**
 * A shuffled sound. There is one base ID for one sound and
 * the different-sounding instances of this sound are numbered.
 * In the {@link SoundChannel} one sound is picked randomly to play.
 */
public class ShuffleSound {

    /** The sound base ID */
    private String soundID = "rock";
    /** How many sounds of this base ID exist (e.g. sound_1, sound_2).
     * Assumed to start at 1, separated by a _. */
    private int soundCount = 3;

    public String getSoundID() {
        return soundID;
    }

    public void setSoundID(String soundID) {
        this.soundID = soundID;
    }

    public int getSoundCount() {
        return soundCount;
    }

    public void setSoundCount(int soundCount) {
        this.soundCount = soundCount;
    }
}
