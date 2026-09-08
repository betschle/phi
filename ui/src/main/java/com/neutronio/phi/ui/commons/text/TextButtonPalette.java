package com.neutronio.phi.ui.commons.text;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.SelectBoxWrapper;
import com.neutronio.phi.ui.commons.buttons.AstraXTextButton;
import com.neutronio.phi.ui.commons.components.Palette;
import com.neutronio.phi.util.format.StandardFormats;

/**
 * A Palette that uses {@link AstraXTextButton} as children
 * @param <ITEM>
 */
public class TextButtonPalette<ITEM> extends Palette<AstraXTextButton, SelectBoxWrapper<ITEM>> {

    public TextButtonPalette(ComponentFactory factory) {
        super(factory);
        this.buttonUpdater = new ButtonUpdater<AstraXTextButton, SelectBoxWrapper<ITEM>>() {
            @Override
            public AstraXTextButton createButton(ComponentFactory componentFactory, String style) {
                return new AstraXTextButton(componentFactory, style);
            }

            @Override
            public void updateButton(AstraXTextButton button, SelectBoxWrapper<ITEM> item) {
                button.setDisabled(false);
                button.setUserObject(item);
                button.setText(item.getDisplayName());
            }

            @Override
            public void resetButton(AstraXTextButton button) {
                button.setText("");
                button.setDisabled(true);
                button.setUserObject(null);
            }
        };
    }
}
