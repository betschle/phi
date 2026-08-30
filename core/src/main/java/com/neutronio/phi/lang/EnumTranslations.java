package com.neutronio.phi.lang;

import com.neutronio.phi.app.Message;
import com.neutronio.phi.io.FileHandler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Ensures enums can be translated, by providing an interface to save various translations for enums.
 * The translation identifiers themselves must be added to the resource bundle. Allows for enums to have
 * custom identifiers (that deviate from their class name).
 * <p>
 * This object is used to generate translation identifiers that are then used for CSV or .properties import/export.
 * In order to work outside of generation (and read only in-app), the same EnumTranslation must be used for obtaining.
 * For this reason it's best to save it somewhere (e.g. to json).
 * </p>
 */
public class EnumTranslations {
    private Map<Enum, EnumTranslation> enumTranslations = new LinkedHashMap<>();

    /** Maps enums to translation identifiers */
    public class EnumTranslation {
        /** The translated display name of this enum. */
        String nameTranslation;
        /** An optional translation for the enum description. Can be null */
        String descriptionTranslation;

        public EnumTranslation(String nameTranslation, String descriptionTranslation) {
            this.nameTranslation = nameTranslation;
            this.descriptionTranslation = descriptionTranslation;
        }
    }

    /**
     * Removes the translation for given enum constant
     * @param enumConstant
     */
    public void removeTranslation(Enum enumConstant) {
        this.enumTranslations.remove(enumConstant);
    }

    /**
     * For the given enum, create two translation identifiers for name and description. Uses the enum as base identifier
     * e.g. an enum called FileType.INTERNAL generates the identifiers FileType_internal_name and FileType_internal_description
     * @param enumConstant
     */
    public void setTranslationAsNameAndDescription(Enum enumConstant) {
        this.enumTranslations.put(enumConstant, new EnumTranslation(
            enumConstant.getClass().getSimpleName() + "_" + enumConstant.name().toLowerCase() +"_name",
            enumConstant.getClass().getSimpleName() + "_"  + enumConstant.name().toLowerCase() + "_description")
        );
    }

    /**
     * For the given enum class, create two translation identifiers for name and description for each
     * enum constant, e.g. an enum called FileType.INTERNAL generates the identifiers FileType_internal_name and
     * FileType_internal_description. Uses the enum name as base identifier.
     *
     * @param constants
     */
    public void setTranslationsAsNameAndDescription(Enum[] constants) {
        for(Enum element : constants) {
            this.setTranslationAsNameAndDescription(element);
        }
    }

    /**
     * For the given enum, create one translation identifier as name. Uses the enum as base identifier
     * e.g. an enum called FileType.INTERNAL generates the identifier FileType_internal_name
     * @param enumConstant
     */
    public void setTranslationAsName(Enum enumConstant) {
        this.enumTranslations.put(enumConstant, new EnumTranslation(
            enumConstant.getClass().getSimpleName() + "_" + enumConstant.name().toLowerCase() +"_name", null)
        );
    }

    /**
     * For the given enum class, create translation identifiers as name. Uses the enum name as base identifier,
     * e.g. an enum called FileType.INTERNAL generates the identifier FileType_internal_name. Does this
     * to all enum constants.
     * @param constants
     */
    public void setTranslationsAsName(Enum[] constants) {
        for(Enum element : constants) {
            this.setTranslationAsName(element);
        }
    }

    /**
     * Adds a translation using the enum as identifier, appends a customizable suffix to all identifiers
     * @param constants the enum constants to use
     * @param suffix the suffix to add to the constants, e.g. *_message, *_name or *_label (this depends on the use)
     */
    public void createTranslations(Enum[] constants, String suffix) {
        for(Enum element : constants) {
            this.setTranslation(element, element.getClass().getSimpleName() + "_" + element.name().toLowerCase() + "_" + suffix.toLowerCase(), null);
        }
    }

    /**
     * Sets a translation using custom identifier for name and description. Overwrites existing identifiers.
     * @param enumConstant the enum constant to translate
     * @param nameIdentifier the identifier to use for the name. This is the key used to access the translation in {@link I18n}
     * @param descriptionIdentifier the identifier to use. This is the key used to access the translation in {@link I18n}
     */
    public void setTranslation(Enum enumConstant, String nameIdentifier, String descriptionIdentifier) {
        this.enumTranslations.put(enumConstant, new EnumTranslation(nameIdentifier, descriptionIdentifier));
    }

    public Map<Enum, EnumTranslation> getEnumTranslations() {
        return enumTranslations;
    }

    public EnumTranslation getTranslation(Enum enumConstant) {
        return this.enumTranslations.get(enumConstant);
    }

    public static void main(String[] args) {
        EnumTranslations translations = new EnumTranslations();
        translations.setTranslationsAsName(FileHandler.State.values());
        translations.setTranslationsAsNameAndDescription(Message.MessageType.values());
        EnumTranslation translation = translations.getTranslation(Message.MessageType.ALERT);
    }
}
