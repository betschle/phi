package com.neutronio.phi.util;

import com.badlogic.gdx.graphics.Color;

public class ColorUtil {

    /**
     * Converts a color to a Hex string. does not consider alpha value.
     * @param color
     * @return a hex value for that component
     */
    public static String colorToHexString(Color color) {
        return colorToString(color.r) + colorToString(color.g) + colorToString(color.b);
    }

    /**
     * Converts a color to a Hex string. does not consider alpha value.
     * @param color
     * @return a hex value for that component
     */
    public static String colorToHexRGBAString(Color color) {
        return colorToString(color.r) + colorToString(color.g) + colorToString(color.b) + colorToString(color.a);
    }

    /**
     *
     * @param colorValue a color component of a color (e.g. red, green, blue or alpha)
     * @return a hex value for that integer component. Appends zeroes for single digits
     */
    public static String colorToString(float colorValue) {
        if( colorValue == 0) return "00";
        String hex = Integer.toHexString((int) (colorValue * 255f));
        if( hex.length() < 2) hex = "0" + hex;
        return hex;
    }

    /**
     * Creates multiple random variations of the provided base color
     * @param count the amount of color variations
     * @param baseColor the base color to use
     * @return
     */
    public static Color[] shuffleColors(int count, Color baseColor) {
        Color[] shuffledColors = new Color[count];
        for( int i =0; i < count; i++) {
            shuffledColors[i] = new Color( (baseColor.r + MathUtil.getRandomFloat(1, 0.2f)) / 2f,
                (baseColor.g + MathUtil.getRandomFloat(1, 0.2f)) / 2f,
                (baseColor.b + MathUtil.getRandomFloat(1, 0.2f)) / 2f,
                1f);
            shuffledColors[i].clamp();
        }
        return shuffledColors;
    }


    /**
     * Creates multiple random variations of the provided base color
     * @param count the amount of color variations
     * @param baseColor the base color to use
     * @param variance How strongly the color should vary, factor 1-0
     * @return
     */
    public static Color[] shuffleColors(int count, float variance, Color baseColor) {

        Color[] shuffledColors = new Color[count];
        for( int i =0; i < count; i++) {
            shuffledColors[i] = new Color( (baseColor.r + MathUtil.getRandomFloat(1, 0.2f)) / 2f,
                (baseColor.g + MathUtil.getRandomFloat(variance, variance * 0.1f )) / 2f,
                (baseColor.b + MathUtil.getRandomFloat(variance, variance * 0.1f )) / 2f,
                1f);
            shuffledColors[i].clamp();
        }
        return shuffledColors;
    }

    /**
     * Multiplies without affecting alpha
     * @param color
     */
    public static Color mult(Color color, float intensity) {
        // move to ColorUtil?
        color.r *= intensity;
        color.g *= intensity;
        color.b *= intensity;
        color.clamp();
        return color;
    }
}
