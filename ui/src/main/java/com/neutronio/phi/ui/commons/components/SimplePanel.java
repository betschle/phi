package com.neutronio.phi.ui.commons.components;

import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.components.AbstractPanel;

public class SimplePanel extends AbstractPanel {

    public SimplePanel(ComponentFactory componentFactory) {
        super(componentFactory);
    }

    public SimplePanel(ComponentFactory componentFactory, String style, String backgroundStyle) {
        super(componentFactory, style, backgroundStyle);
    }

    protected SimplePanel(ComponentFactory componentFactory, AbstractPanelStyle style, String backgroundStyle) {
        super(componentFactory, style, backgroundStyle);
    }

    @Override
    public void resetValues() {

    }
}
