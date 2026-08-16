package com.neutronio.phi.ui.skin;

import com.badlogic.gdx.graphics.Color;
import com.neutronio.phi.io.FileHandler;
import com.neutronio.phi.util.ColorUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Configuration object that helps loading a {@link PhiSkin}.
 */
public class SkinConfiguration {

    /** the currently selected language */
    public Locale language = Locale.ENGLISH;
    /** location of the GUI skin */
    public FileHandler.FileLocation skinLocation = FileHandler.FileLocation.INTERNAL;
    /** the base directory of the skin. This is where all of its files are expected */
    public String baseDirectory = "skins/astrax-core-ui/";
    /** The file name for sounds stored in json */
    public String soundsFile = "sounds.json";
    /** The file name for the libgdx skin style file */
    public String stylesJsonFile = "styles.json";
    /** The file name for the atlas belonging to the libgdx skin */
    public String stylesAtlasFile = "styles.atlas";

    // localization settings
    public String translationsPath = "lang";
    public String translationsBundleName = "astrax";

    // custom style colors
    // expected colors: primaryColor, secondaryColor, tertiaryColor, successColor, warningColor, errorColor, disabledColor
    public Map<String, Color> uiColors = new HashMap<>();

    public static Map<String, Color> getDefaultColors() {
        Map<String, Color> colors = new HashMap<>();
        colors.put("primary", Color.valueOf("11c1d0ff"));
        colors.put("primary-dark", ColorUtil.mult(Color.valueOf( "11c1d0ff"), 0.7f));
        colors.put("primary-bright", ColorUtil.mult(Color.valueOf("11c1d0ff"), 1.3f));
        colors.put("secondary", Color.valueOf("343c42ff"));
        colors.put("secondary-alpha", Color.valueOf("343c4288"));
        colors.put("secondary-dark", ColorUtil.mult(Color.valueOf("343c42ff"), 0.7f));
        colors.put("secondary-bright", ColorUtil.mult(Color.valueOf("343c42ff"), 1.3f));
        colors.put("tertiary", Color.valueOf("ff1fe1ff"));
        colors.put("tertiary-dark", ColorUtil.mult(Color.valueOf("ff1fe1ff"),0.7f));
        colors.put("tertiary-bright", ColorUtil.mult(Color.valueOf("ff1fe1ff"),1.3f));
        return colors;
    }

    public static SkinConfiguration getDefaultSkinSettings() {
        SkinConfiguration settings = new SkinConfiguration();
        settings.language = Locale.ENGLISH;
        settings.skinLocation = FileHandler.FileLocation.INTERNAL;
        settings.baseDirectory = "skins/astrax-core-ui/";
        settings.soundsFile = "sounds.json";
        settings.stylesJsonFile = "styles.json";
        settings.stylesAtlasFile = "styles.atlas";

        settings.translationsPath = "lang";
        settings.translationsBundleName = "astrax";

        settings.uiColors = getDefaultColors();
        return settings;
    }

    public SkinConfiguration() {
    }

    public SkinConfiguration copy() {
        SkinConfiguration settings = new SkinConfiguration();
        settings.skinLocation = FileHandler.FileLocation.INTERNAL;
        settings.baseDirectory = this.baseDirectory;
        settings.soundsFile = this.soundsFile;
        settings.stylesJsonFile = this.stylesJsonFile;
        settings.stylesAtlasFile = this.stylesAtlasFile ;

        settings.translationsPath = this.translationsPath;
        settings.translationsBundleName = this.translationsBundleName;
        for(Map.Entry<String, Color> entry : this.uiColors.entrySet()) {
            settings.uiColors.put(entry.getKey(), entry.getValue());
        }
        return settings;
    }
}
