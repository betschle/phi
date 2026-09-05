package com.neutronio.phi.lang.csv;

import java.util.Locale;
import java.util.Map;

// a model for various exporters/importers
// displays a CSV file where identifier is the row ID
public class TranslationIdentifier {
    /** The identifier by which the translation is referenced */
    protected String identifier;
    /** The comment for this identifier, that gives hints to the translator */
    protected String comment;
    /** Translations, key is country code and value is the actual translation */
    protected Map<String, String> translations; // should be variable by configuration. Export options should tell what languages to export

    public void setTranslation(Locale locale, String content) {
        this.translations.put(locale.toLanguageTag(), content);
    }

    public String getTranslation(Locale locale) {
        return this.translations.get(locale.toLanguageTag());
    }
}
