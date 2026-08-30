package com.neutronio.phi.lang;

import com.neutronio.phi.app.Message;
import com.neutronio.phi.io.FileHandler;

import java.util.List;

public class TranslationGeneratorExample {

    public enum ServerStatus {
        ERROR,
        MAINTENANCE,
        RUNNING
    }

    public enum ConnectionType {
        ACTIVE,
        TIMEOUT,
        DISCONNECTED
    }

    public static void main(String[] args) {
        TranslationGenerator.TranslationConfiguration configuration = new TranslationGenerator.TranslationConfiguration();
        // for simple enum translation use:
        configuration.enums.add(ServerStatus.class);

        // enum translation config that supports names and descriptions, and allows for live-translation and export functionality
        configuration.enumTranslations = new EnumTranslations();

        // only use translation identifier: enum.name()_name
        configuration.enumTranslations.setTranslationsAsName(FileHandler.State.values());
        // use boh names + descriptions identifiers
        configuration.enumTranslations.setTranslationsAsNameAndDescription(Message.MessageType.values());
        // use custom identifier suffix
        configuration.enumTranslations.createTranslations(ConnectionType.values(), "message");

        // generate a class here, for code export
        // the above created EnumTranslations object must be used when accessing them
        TranslationGenerator translationGenerator = new TranslationGenerator();
        translationGenerator.generateAndSaveCode(
            configuration,  "TranslationsExample",
            "com.neutronio.phi.lang", "examples/src/main/java/" );
    }
}
