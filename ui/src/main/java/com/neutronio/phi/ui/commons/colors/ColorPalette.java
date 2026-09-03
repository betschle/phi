package com.neutronio.phi.ui.commons.colors;

import com.badlogic.gdx.graphics.Color;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.buttons.AstraXButton;
import com.neutronio.phi.ui.commons.components.Palette;

/**
 * Works like a {@link com.neutronio.astrax.ui.editor.Palette}, except with colors.
 */
public class ColorPalette extends Palette<AstraXButton, Color> {

    public ColorPalette(ComponentFactory componentFactory) {
        super(componentFactory);
        this.setIconCount(10);
        this.buttonUpdater = new ButtonUpdater<AstraXButton, Color>() {
            @Override
            public AstraXButton createButton(ComponentFactory componentFactory, String style) {
                AstraXButton button = new AstraXButton(componentFactory, style == null ? "color" : style);
                button.setStyle(button.getStyle().copy());
                button.setCanCheck(true);
                button.setDisabled(true);
                button.setUserObject(null);
                return button;
            }

            @Override
            public void updateButton(AstraXButton button, Color color) {
                button.setDisabled(false);
                button.setUserObject(color);
                updateButtonStyle(button, color.cpy());
            }

            @Override
            public void resetButton(AstraXButton button) {
                button.setUserObject(null);
                button.setDisabled(true);
                button.setUserObject(null);
            }
        };
    }

    public AstraXButton getSelectedButton() {
        return (AstraXButton) this.buttonGroup.getChecked();
    }

    /**
     * Updates the given button with the given color
     * @param button
     * @param color
     */
    public void updateButtonStyle(AstraXButton button, Color color) {
        button.getStyle().baseColor.upColor = color;
        button.getStyle().baseColor.downColor = color.cpy().mul(0.9f);
        button.getStyle().baseColor.overColor = color.cpy().mul(1.1f);
        button.getStyle().baseColor.checkedColor = color.cpy();
        button.getStyle().baseColor.checkedOverColor = color.cpy().mul(1.1f);
        button.getStyle().baseColor.checkedDownColor = color.cpy().mul(0.9f);
    }
}
