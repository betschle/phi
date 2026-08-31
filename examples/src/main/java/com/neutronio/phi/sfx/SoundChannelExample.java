package com.neutronio.phi.sfx;

public class SoundChannelExample {

    public static void main(String[] args) {
        // This example does not run without gdx initialized
        // requires the sounds files present too

        // use the same sound settings configuration to initialize
        // multiple sound channels
        SoundSettings settings = new SoundSettings();
        settings.addSoundsToLoad("ui", "beep", "beep.ogg");
        settings.addSoundsToLoad("ui", "beep2", "beep2.ogg");
        settings.addSoundsToLoad("sfx", "crash", "crash_2.ogg");
        settings.addSoundsToLoad("sfx", "pickup", "pickup_1.ogg");
        SoundLoader soundLoader = new SoundLoader();

        StaticSoundChannel uiChannel = new StaticSoundChannel("ui");
        StaticSoundChannel sfxChannel = new StaticSoundChannel("sfx");
        soundLoader.load(uiChannel, "resources", settings);
        soundLoader.load(sfxChannel, "resources", settings);

        uiChannel.playSoundOnce("beep2");
        uiChannel.playSoundOnce("crash"); // this will not work, as crash can only be found in sfx
        sfxChannel.playSoundOnce("pickup", 0.3f);
        sfxChannel.playSoundOnce("beep2"); // this will not work, as beep can only be found in ui
    }
}
