package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.buttons.StaticIcon;
import com.neutronio.phi.ui.commons.text.TextProgressbar;
import com.neutronio.phi.util.format.StandardFormats;

/**
 * A Text Progressbar with an icon
 */
public class IconProgressbar extends HorizontalGroup {
    protected TextProgressbar textProgressbar;
    protected StaticIcon icon;

    public static class Style {
        public TextProgressbar.Style textProgressbarStyle;
        public StaticIcon.StaticIconStyle staticIconStyle;
    }

    public IconProgressbar(ComponentFactory factory) {
        this(factory, "default");
    }

    public IconProgressbar(ComponentFactory factory, String style) {
        this(factory, factory.getSkin().get(style, Style.class));
    }

    public IconProgressbar(ComponentFactory factory, Style style) {
        this.space(10);
        this.textProgressbar = new TextProgressbar(factory, style.textProgressbarStyle);
        this.textProgressbar.setUsePercent(true);
        this.textProgressbar.setFormat(StandardFormats.PERCENT);
        this.textProgressbar.setValue(0.45f);

        this.icon = new StaticIcon(factory, style.staticIconStyle);
        this.icon.setInlineIcon("icon_inline_discover");
        this.addActor(this.icon);
        this.addActor(this.textProgressbar);
        this.pack();
    }



    public StaticIcon getIcon() {
        return icon;
    }

    public TextProgressbar getTextProgressbar() {
        return textProgressbar;
    }
}
