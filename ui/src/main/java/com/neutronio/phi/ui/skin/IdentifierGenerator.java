package com.neutronio.phi.ui.skin;

import com.neutronio.phi.lang.TranslationGenerator;

/**
 * Generates translation files for the phi-ui
 */
public class IdentifierGenerator {

    public static void main(String[] args) {
        TranslationGenerator translationGenerator = new TranslationGenerator();
        // doesnt work with resources?
        translationGenerator.generateAndSaveCode(
            "ui/assets/lang/phi.properties",  "PhiUITranslations",
            "com.neutronio.phi.ui.skin", "ui/src/main/java/" );
    }
}
