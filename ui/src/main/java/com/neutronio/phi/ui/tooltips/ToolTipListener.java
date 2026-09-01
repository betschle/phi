package com.neutronio.phi.ui.tooltips;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * An interface to catch enter/exit events from input listeners and process them.
 */
public interface ToolTipListener {
    void onEnterTooltip(ToolTipManager.ToolTipInfo toolTipInfo, float x, float y, Actor actor);
    void onExitTooltip(ToolTipManager.ToolTipInfo toolTipInfo, float x, float y, Actor actor);
}
