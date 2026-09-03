package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.AstraXComponent;
import com.neutronio.phi.ui.commons.Background;
import com.neutronio.phi.ui.commons.buttons.AstraXButton;
import com.neutronio.phi.ui.commons.buttons.StaticIcon;

public class Header extends AstraXComponent {

    protected StaticIcon headerIcon; // optional
    protected AstraXButton exitButton;
    protected AstraXButton helpButton;
    protected Label headerText;
    protected Background background;


    // TODO add icon?
    // TODO add style?
    public Header(ComponentFactory factory) {
        super(factory, "window-header");
        this.align(Align.center);
        this.headerIcon = new StaticIcon(factory, "header");
        this.headerIcon.setInlineIcon((Drawable) null);
        this.headerText = new Label("Header", componentFactory.getSkin(), "header-no-bg");
        this.exitButton = new AstraXButton(componentFactory, "icon_inline_x", "primary-no-check");
        this.exitButton.setCanCheck(false);
        this.helpButton = new AstraXButton(componentFactory, "icon_inline_questionmark", "primary-no-check");
        this.helpButton.setCanCheck(false);
    }

    public void setHeaderIcon(String inlineIcon) {
        this.headerIcon.setInlineIcon(inlineIcon);
    }

    public AstraXButton getExitButton() {
        return exitButton;
    }

    public AstraXButton getHelpButton() {
        return helpButton;
    }

    public void setTitle(String text) {
        this.headerText.setText(text);
    }

    public void construct() {
        super.construct();
        this.add( this.headerIcon ).padRight(5);
        this.add( this.headerText ).padLeft(5).padTop(2).left().expand();
        this.add( this.helpButton ).padRight(2).padTop(5).right().top();
        this.add( this.exitButton ).padRight(5).padTop(5).right().top();
        this.pack();
    }
}
