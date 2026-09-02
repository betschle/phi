package com.neutronio.phi.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.neutronio.phi.app.Message;
import com.neutronio.phi.io.FileHandler;
import com.neutronio.phi.lang.EnumTranslations;
import com.neutronio.phi.lang.I18n;
import com.neutronio.phi.sfx.StaticSoundChannel;
import com.neutronio.phi.ui.skin.PhiSkin;
import com.neutronio.phi.ui.skin.SkinConfiguration;
import com.neutronio.phi.ui.tooltips.ToolTipManager;

import java.util.Map;

/**
 * An application scoped factory for more complex AstraX Components.
 *
 */
public class ComponentFactory
//    extends AbstractApplicationFactory
{

    /** Default width of game menu tabs. Typically fills the whole screen. Depends on screen resolution. */
    public static int GAME_MENU_WIDTH;
    /** Default height of game menu tabs. Typically fills the whole screen. Depends on screen resolution. */
    public static int GAME_MENU_HEIGHT;

    /** Default width of primary tabs. Depends on screen resolution. */
    public static int PRIMARY_TAB_WIDTH;
    /** Default height of primary tabs. Depends on screen resolution. */
    public static int PRIMARY_TAB_HEIGHT;

    /** Default width of secondary tabs. Depends on screen resolution. */
    public static int SECONDARY_TAB_WIDTH;
    /** Default height of secondary tabs. Depends on screen resolution. */
    public static int SECONDARY_TAB_HEIGHT;

    /** The skin used for the UI */
    private PhiSkin skin;
    /** for playing UI sounds */
    private StaticSoundChannel soundChannel;
    /** Core translations for the AstraX GUI */
    private I18n coreTranslations;
    /** Helper object for enum-centered translations */
    private EnumTranslations enumTranslations;

    /** Additional GUI translations. Key is DataPack.Name  >*/
    private Map<String, I18n> translations; // TODO add me for mod support

    private float defaultTabWidth = 1200f;
    private float defaultTabHeight = 600;

    private ToolTipManager toolTipManager;

    public static EnumTranslations getDefaultEnumTranslations() {
        EnumTranslations enumTranslations = new EnumTranslations();
        enumTranslations.createTranslations(FileHandler.FileOperationStatus.values(), "message");
        enumTranslations.setTranslationsAsName(Message.MessageType.values());
        return enumTranslations;
    }

    /**
     *
     * @param soundChannel the sound channel to play sounds with
     */
    public ComponentFactory(StaticSoundChannel soundChannel) {
        this.soundChannel = soundChannel;
        this.enumTranslations = getDefaultEnumTranslations();
        // TODO Use SoundGroups on top of JSON Sound loading
        // TODO I could add this to styles!
//        this.toolTipManager = new ToolTipManager(this);
    }

    public ToolTipManager getToolTipManager() {
        return toolTipManager;
    }

    /**
     * Loads the default UI skin into the component factory
     */
    public void loadSkin(SkinConfiguration skinSettings) {
        GAME_MENU_WIDTH = Gdx.graphics.getWidth() - 20;
        GAME_MENU_HEIGHT = Gdx.graphics.getHeight() - 100;
        PRIMARY_TAB_WIDTH = MathUtils.clamp( Gdx.graphics.getWidth() - 200, 800, 1200);
        PRIMARY_TAB_HEIGHT =  MathUtils.clamp( Gdx.graphics.getHeight() - 200, 600, Gdx.graphics.getHeight()-200);
        SECONDARY_TAB_WIDTH  = (int) (PRIMARY_TAB_WIDTH * 0.7f);
        SECONDARY_TAB_HEIGHT = (int) (PRIMARY_TAB_HEIGHT * 0.7f);
//        logger.info("Loading skin into app...");
        FileHandle skinFile = Gdx.files.internal(skinSettings.baseDirectory + skinSettings.stylesAtlasFile);
        this.skin = new PhiSkin(skinSettings.baseDirectory, new TextureAtlas(skinFile));
        this.skin.load(skinSettings.baseDirectory+skinSettings.stylesJsonFile, skinSettings.skinLocation, skinSettings);

        // TODO fixme
//        try {
//            logger.info("Loading sounds for skin...");
//            StringBuilder fileContents = AstraXApp.astraX.getAstraXFiles().readFileContents(skinSettings.baseDirectory + skinSettings.soundsFile, skinSettings.skinLocation);
//            JSONObject json = new JSONObject(fileContents.toString());
//            RepositoryFactory repositoryFactory = AstraXApp.astraX.getRepositoryFactory();
//            JSONToSoundSettings soundSettingsConverter = repositoryFactory.getDataManager().getConverter(JSONToSoundSettings.class);
//            this.skin.setSoundSettings(soundSettingsConverter.convert(json.toMap()));
//            AstraXApp.astraX.getSoundLoader().loadIntoSoundManager(AstraXApp.astraX.getUiSoundManager(), this.skin);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // init tooltips
//        this.toolTipManager.registerTooltip( new ItemInfoPanel(this, "transparent"));
//        this.toolTipManager.registerTooltip( new HelpTooltip(this));
//        this.toolTipManager.registerTooltip( new DiscoveryTooltip(this));
    }

    /**
     * Loads the translations into the component factory
     */
    public void loadTranslations(SkinConfiguration skinSettings) {
        // Note: some resource bundles are data related and need to be located inside the mod
        // TODO duplicate code with DataManager here
        this.coreTranslations = new I18n(skinSettings.language);
        this.coreTranslations.loadBundles(skinSettings.translationsPath,skinSettings.skinLocation, skinSettings.translationsBundleName);
    }

    /**
     *
     * @return The skin used for the UI
     */
    public Skin getSkin() {
        return skin;
    }

    public ImageTextButton.ImageTextButtonStyle copy(ImageTextButton.ImageTextButtonStyle buttonStyle) {
        // TODO OMG use reflection for copying instead holy shit
        ImageTextButton.ImageTextButtonStyle buttonStyleCopy = new ImageTextButton.ImageTextButtonStyle();
        buttonStyleCopy.imageUp = buttonStyle.imageUp;
        buttonStyleCopy.imageDown = buttonStyle.imageDown;
        buttonStyleCopy.imageOver = buttonStyle.imageOver;
        buttonStyleCopy.imageChecked = buttonStyle.imageChecked;
        buttonStyleCopy.imageCheckedDown = buttonStyle.imageCheckedDown;
        buttonStyleCopy.imageCheckedOver = buttonStyle.imageCheckedOver;
        buttonStyleCopy.imageDisabled = buttonStyle.imageDisabled;

        buttonStyleCopy.font = buttonStyle.font;
        buttonStyleCopy.fontColor = buttonStyle.fontColor;
        buttonStyleCopy.downFontColor = buttonStyle.downFontColor;
        buttonStyleCopy.overFontColor = buttonStyle.overFontColor;
        buttonStyleCopy.disabledFontColor = buttonStyle.disabledFontColor;
        buttonStyleCopy.checkedFontColor = buttonStyle.checkedFontColor;
        buttonStyleCopy.checkedDownFontColor = buttonStyle.checkedDownFontColor;
        buttonStyleCopy.checkedOverFontColor = buttonStyle.checkedOverFontColor;
        buttonStyleCopy.checkedFocusedFontColor = buttonStyle.checkedFocusedFontColor;

        buttonStyleCopy.up = buttonStyle.up;
        buttonStyleCopy.down = buttonStyle.down;
        buttonStyleCopy.over = buttonStyle.over;
        buttonStyleCopy.checked = buttonStyle.checked;
        buttonStyleCopy.checkedDown = buttonStyle.checkedDown;
        buttonStyleCopy.focused = buttonStyle.focused;
        return buttonStyleCopy;
    }

    // TODO duplicate with DataManager
    /**
     * Gets the translation for a given key using the astrax-core translations
     * @param identifier a translation identifier
     * @return
     */
    public String translate(String identifier) {
        return this.coreTranslations.translate(identifier);
    }

    /**
     * Translates an enum constant using the configured enum translation.
     * @param enumConstant
     * @return
     */
    public String translate(Enum enumConstant) {
        EnumTranslations.EnumTranslation translation = this.enumTranslations.getTranslation(enumConstant);
        return translate(translation.nameTranslation);
    }

    /**
     * Translates an enum constant using the configured enum translation and also
     * injects placeholders into the string. The identifier content must possess the correct format
     * placeholders (%s, %f) for this method to work.
     * @param enumConstant
     * @return
     */
    public String translateWithFormat(Enum enumConstant, Object... parameters) {
        EnumTranslations.EnumTranslation translation = this.enumTranslations.getTranslation(enumConstant);
        String translate = translate(translation.nameTranslation);
        return String.format(translate, parameters);
    }

    // TODO duplicate with DataManager
    /**
     * Translates a marked identifier String. This is a string starting
     * with the $ symbol, which indicates the need for translation. This is intended
     * for translated game data.
     * @param key a translation present for this key (minus marker), key if no marker was present
     * @return
     */
    public String translateMarked(String key) {
        if(key.startsWith("$")) {
            return this.coreTranslations.translate(key.substring(1));
        }
        return key;
    }

    /**
     * Translates, then formats String with provided parameters, using the astrax-core translations
     * @param identifier a translation identifier
     * @param params parameters to format the translation identifier with
     * @return
     */
    public String translate(String identifier, Object... params) {
        return this.coreTranslations.translate(identifier, params);
    }

    public void playUISound(String soundID) {
        this.soundChannel.playSoundOnce(soundID);
    }

    public void playUISound(String soundID, float mult) {
        this.soundChannel.playSoundOnce(soundID, mult);
    }
}

