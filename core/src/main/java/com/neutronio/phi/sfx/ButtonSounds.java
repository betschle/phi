package com.neutronio.phi.sfx;

/**
 * Button sound "styles" to configure sounds
 * played on entering certain button states. Used
 * for both the UI and loading sounds for a datapack.
 */
public class ButtonSounds {

    /** Sound played when button is down/pressed once */
    public String soundPressed = "click1";
    /** Sound played when button is unchecked/released */
    public String soundRelease = "click2";
    /** Sound played when button is hovered over */
    public String soundOver;

    public ButtonSounds() {
        // empty constructor
    }

    /**
     *
     * @param soundDown Sound played when button is down/pressed once
     * @param soundUncheck Sound played when button is unchecked/released
     * @param soundHover Sound played when button is hovered over
     */
    public ButtonSounds( String soundDown, String soundUncheck, String soundHover) {
        this.soundPressed = soundDown;
        this.soundRelease = soundUncheck;
        this.soundOver = soundHover;
    }
}
