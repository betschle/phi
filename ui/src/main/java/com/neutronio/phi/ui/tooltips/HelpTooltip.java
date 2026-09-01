package com.neutronio.phi.ui.tooltips;

import com.neutronio.astrax.ui.ComponentFactory;
import com.neutronio.astrax.ui.commons.text.AstraXLabel;

public class HelpTooltip extends AstraXLabel
        implements ToolTipComponent<String>{

    public HelpTooltip(ComponentFactory componentFactory) {
        super(componentFactory, "transparent");
    }

    @Override
    public void applyData(String data) {
        this.setText(data);
    }

    @Override
    public void clearData() {
        this.setText("");
    }
}
