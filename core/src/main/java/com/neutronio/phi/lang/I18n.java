package com.neutronio.phi.lang;

import com.neutronio.phi.io.FileHandler;

import java.util.*;
import java.util.logging.Logger;

/**
 * Stores and provides access to localization resource bundles. Compatible with LibGdx.
 * There should be one of these instances per resource bundle, they are
 * not intended to hold multiple different bundles.
 */
public class I18n {
    private Logger logger = Logger.getLogger(I18n.class.getCanonicalName());

    private Locale currentLocale;
    private PropertyResourceBundle currentBundle;
    private PropertyResourceBundle baseBundle;

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
        // crashes if currentLocale = Base bundle locale
        if(this.currentBundle.containsKey(key)) {
            translated = this.currentBundle.getString(key);
        } else {
            // fallback
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
        if(this.currentBundle.containsKey(key)) {
            translated = this.currentBundle.getString(key);
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

        if(this.currentLocale.equals(Locale.ROOT)) {
            this.currentBundle = this.baseBundle;
        } else {
            try {
                this.currentBundle = fileHandler.loadBundle(path + "/" + basename + "_" + currentLocale.getLanguage() + ".properties", location);
            } catch (TranslationFileNotFoundException e) {
                e.printStackTrace();
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
