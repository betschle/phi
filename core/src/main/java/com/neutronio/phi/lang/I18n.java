package com.neutronio.phi.lang;

import com.neutronio.phi.io.FileHandler;

import java.util.*;
import java.util.logging.Logger;

/**
 * Stores localization resources inside a bundle. Compatible with LibGdx.
 */
public class I18n {
    public static final Locale[] supportedLanguages = { Locale.ENGLISH, Locale.GERMAN };
    private Logger logger = Logger.getLogger(I18n.class.getCanonicalName());

    private Locale currentLocale;

    private Map<Locale, PropertyResourceBundle> resourceBundles = new HashMap<>();
    private ResourceBundle baseBundle;

    public I18n(Locale locale) {
        this.currentLocale = locale;
    }

    /**
     * Gets the translation for a given key.
     * @param key
     * @return
     * @throws MissingResourceException if the fallback bundle does not contain the specified key
     */
    public String translate(String key) throws MissingResourceException {
        String translated = null;
        if(this.resourceBundles.get(currentLocale).containsKey(key)) {
            translated = this.resourceBundles.get(currentLocale).getString(key);
        } else {
            translated = this.baseBundle.getString(key);
        }
        return translated;
    }

    /**
     * Translates an enum whose key was generated with TranslationGenerator
     * @param element
     * @return
     * @throws MissingResourceException
     */
    public String translateEnum(Enum element) throws MissingResourceException {
        String translated = null;
        String key = element.getClass().getSimpleName() + "_" + element.name().toLowerCase(Locale.ROOT);
        if(this.resourceBundles.get(currentLocale).containsKey(key)) {
            translated = this.resourceBundles.get(currentLocale).getString(key);
        } else {
            translated = this.baseBundle.getString(key);
        }
        return translated;
    }

    /**
     * Translates, then formats String with provided parameters
     * @param key
     * @param params
     * @return
     * @throws MissingResourceException if the fallback bundle does not contain the specified key
     */
    public String translate(String key, Object... params) throws MissingResourceException {
        return String.format( this.translate(key), params);
    }

    /**
     * Loads the base bundle and all other locale bundles.
     * @param path the base folder, e.g. resources/bundles
     * @param basename the resource bundle name, e.g. labels. Associated bundles will be loaded automatically according to expected locales
     * @param location the location
     */
    public void loadBundles(FileHandler fileHandler, String path, FileHandler.FileLocation location, String basename){
        this.baseBundle = fileHandler.loadBundle(path + "/" + basename + ".properties", location);
        for(Locale locale : supportedLanguages) {
            PropertyResourceBundle bundle = fileHandler.loadBundle(path + "/" + basename + "_" + locale.getLanguage()+".properties", location);
            if(bundle != null) {
                this.resourceBundles.put(locale, bundle);
            }
        }
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public ResourceBundle getBaseBundle() {
        return baseBundle;
    }

}
