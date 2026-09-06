package com.neutronio.phi.ui;

import com.neutronio.phi.io.FileHandler;
import com.neutronio.phi.io.JavaFileHandler;
import com.neutronio.phi.lang.TranslationFileNotFoundException;
import com.neutronio.phi.sfx.StaticSoundChannel;
import com.neutronio.phi.ui.skin.PhiUITranslations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Locale;

public class ComponentFactoryTest {

    @Test
    public void loadTranslation() {
        ComponentFactory componentFactory = new ComponentFactory(new StaticSoundChannel("ui"));
        componentFactory.setFileHandler(new JavaFileHandler());
        componentFactory.loadTranslations("phi", Locale.ROOT, "assets/lang", FileHandler.FileLocation.LOCAL);

        // just testing one sample to ensure it works
        String translated = componentFactory.translate("phi", PhiUITranslations.BUTTON_TEXT_YES);
        Assertions.assertNotNull(translated);
        Assertions.assertEquals(3, translated.length());
    }

    @Test
    public void failToLoadBaseBundle() {
        Assertions.assertThrows(TranslationFileNotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                ComponentFactory componentFactory = new ComponentFactory(new StaticSoundChannel("ui"));
                componentFactory.setFileHandler(new JavaFileHandler());
                componentFactory.loadTranslations("framework", Locale.ROOT, "assets/lang", FileHandler.FileLocation.LOCAL);
            }
        });
    }
}
