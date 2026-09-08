package com.neutronio.phi.atlas;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.components.SimplePanel;
import com.neutronio.phi.ui.commons.text.AstraXLabel;

/**
 * A tool intended to be used in an editor UI.
 */
public abstract class ToolBase {
    /** Whether this tool is currently selected or not */
    protected boolean active;
    /** factory for constructing UI components */
    protected final ComponentFactory factory;
    /** The tooltip shown for this tool */
    public String tooltip; // editor relevant
    /** A short one sentence description for this tool */
    public String description;
    /** The icon for this tool for the toolbar */
    public String icon;
    /** A panel that allows for tool specific actions */
    protected SimplePanel controlPanel;

    public ToolBase(ComponentFactory factory) {
        this.factory = factory;
    }

    public SimplePanel getControlPanel() {
        return controlPanel;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * A hook for when the tools get selected by its parent editor.
     */
    public void onToolSelected() {}
    /**
     * A hook for when the tools get deselected by its parent editor.
     */
    public void onToolDeselected() {}

    /**
     * Gets a basic control panel base, featuring a header and the tool's description.
     * @param colspan the col span of header and description
     * @return
     */
    protected SimplePanel getControlPanelBase(int colspan) {
        SimplePanel panel = new SimplePanel(factory, "toolbar", "transparent");
        panel.align(Align.top);

        Table headerGroup = new Table();

        AstraXLabel descriptionLabel = panel.getLabel(this.description);
        descriptionLabel.setWrap(true);
        headerGroup.align(Align.center);
        headerGroup.add(panel.getHeader(this.tooltip)).fillX().expandX().padBottom(10f).row();
        headerGroup.add(descriptionLabel).fillX().padBottom(10);

        panel.add(headerGroup).colspan(colspan).fillX().expandX();
        panel.row();
        return panel;
    }

    /** Hook for manual updates */
    public void update(float delta) {}
}
