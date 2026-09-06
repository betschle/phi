package com.neutronio.phi.ui.skin;

import com.neutronio.phi.io.FileHandler;
import com.neutronio.phi.lang.TranslationGenerator;
import com.neutronio.phi.ui.ComponentFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Generates translation files for the phi-ui
 */
public class IdentifierGenerator {

    public static void main(String[] args) {
        TranslationGenerator.TranslationConfiguration config = new TranslationGenerator.TranslationConfiguration();
        Properties phiDefaults = new Properties();
        try {
            phiDefaults.load(new FileInputStream("ui/assets/lang/phi.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        config.properties.add(phiDefaults);
        config.enumTranslations = ComponentFactory.getDefaultEnumTranslations();

        TranslationGenerator translationGenerator = new TranslationGenerator();
        translationGenerator.generateAndSaveCode(
            config,  "PhiUITranslations",
            "com.neutronio.phi.ui.skin", "ui/src/main/java/" );
    }
}
