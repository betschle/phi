package com.neutronio.phi.ui.tooltips;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.neutronio.phi.ui.ComponentFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * - One tooltip can be used for many components
 * - its assumed that only one tooltip can be active at a time. The timer resets when a different
 * tooltip is active
 * - Tooltips act as archetypes, one can be used for many components of similar nature
 */
public class ToolTipManager implements ToolTipListener {
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private Map<Class, ToolTipComponent> tooltips = new HashMap<>();

    private ToolTipComponent activeTooltip;
    private Container container;

    private static final int HIDDEN = 0;
    private static final int HOVER = 1;
    private static final int SHOWN = 2;
    private static final int HIDING = 3;

    /** the amount of time required to bring up the tooltip */
    private float maxHoverTime = 0.7f;
    /** the amount of time not hovering over the component required to hide the tooltip */
    private float maxHidingTime = 0.4f;

    private int state = 0;
    private float hoverTime = 0;
    private float hidingTime = 0;
    private Actor hoveredActor;

    public float offsetX = 15;
    public float offsetY = 19;

    public ToolTipManager(ComponentFactory factory) {
        this.container = new Container();
        this.container.setTouchable(Touchable.disabled);
    }

    @Override
    public void onEnterTooltip(ToolTipInfo toolTipInfo, float x, float y, Actor actor) {
        ToolTipComponent toolTipComponent = this.tooltips.get(toolTipInfo.toolTipClass);
        if(toolTipComponent == null) {
            this.logger.warning("No ToolTip registered of class " + toolTipInfo.toolTipClass);
            return;
        }

        switch(this.state) {
            case HIDING:
                if(Objects.equals(this.activeTooltip, toolTipComponent)) {
                    // hiding state and entering the same tooltip:
                    // => update tooltip and switch to shown state
                    this.state = SHOWN;
                    this.hidingTime = 0;
                    this.changeTooltipContent(toolTipInfo.toolTipData);
                    this.logger.finest("On Enter: Hiding > Shown");
                } else {
                    // hiding state and entering different tooltip class
                    // => update class and switch to shown state
                    this.state = SHOWN;
                    this.hidingTime = 0;
                    this.changeTooltipClass(toolTipComponent, toolTipInfo.toolTipData);
                    this.logger.finest("On Enter: Hiding > Shown");
                }
                break;
            case HIDDEN:
                this.state = HOVER;
                this.logger.finest("On Enter: Hidden > Hover");
                break;
            case SHOWN:
                // when is this code executed? below code is originally executed after setting hovered actor below
                if(Objects.equals(this.activeTooltip, toolTipComponent)) {
                    // > this happens on Shown
                    // if tooltip class is the same, apply new data & reposition
                    if(Objects.equals(this.activeTooltip.getClass(), toolTipInfo.toolTipClass)) {
                        this.changeTooltipContent(toolTipInfo.toolTipData);
                    } else {
                        // if the tooltip is not the same, reset timer
                        if(toolTipComponent != null) {
                            this.changeTooltipClass(toolTipComponent, toolTipInfo.toolTipData);
                        }
                    }
                }
        }
        this.hoveredActor = actor;

        // first time set
        if(this.activeTooltip == null) {
            this.logger.finest("On Enter: first time set");
            this.changeTooltipClass(toolTipComponent, toolTipInfo.toolTipData);
        }
    }

    @Override
    public void onExitTooltip(ToolTipInfo toolTipInfo, float x, float y, Actor actor) {
        switch(this.state) {
            case HOVER:
                this.state = HIDING;
                this.logger.finest("On Exit: Hover > Hiding");
                break;
            case SHOWN:
                this.state = HIDING;
                this.logger.finest("On Exit: Shown > Hiding");
                break;
        }
    }

    public static class ToolTipInfo<C, D> {
        public Class<C> toolTipClass;
        public D toolTipData;

        public ToolTipInfo(Class<C> toolTipClass) {
            this.toolTipClass = toolTipClass;
        }
    }

    /**
     * Visibly shows the tooltip container
     * @param forActor
     */
    private void showTooltip(Actor forActor) {
        if(this.container.isTouchable()) return;
        this.container.setVisible(false);
        this.container.setTouchable(Touchable.disabled);

        forActor.getStage().addActor(this.container);
        this.updateContainerPosition();
        this.container.toFront();
        this.container.addAction(
                Actions.sequence(
                        Actions.show(),
                        Actions.alpha(1, 0.3f),
                        Actions.touchable(Touchable.enabled)));
    }

    /**
     * Visibly hides the tooltip container
     */
    private void hideTooltip() {
        if(!this.container.isTouchable()) return;
        this.container.addAction(
                Actions.sequence(
                        Actions.alpha(0, 0.5f),
                        Actions.hide(),
                        Actions.touchable(Touchable.disabled)));
    }

    private void changeTooltipContent(Object data){
        this.logger.fine("Change content");
        this.activeTooltip.applyData(data);
        this.container.pack();
    }

    private void changeTooltipClass(ToolTipComponent toolTipComponent, Object data){
        this.logger.fine("Change class");
        this.activeTooltip = toolTipComponent;
        if(toolTipComponent != null) {
            this.logger.fine("Change class to " + toolTipComponent );
            Actor actor = (Actor) toolTipComponent;
            this.activeTooltip.applyData(data);
            this.container.setActor(actor);
            this.container.setSize(actor.getWidth(), actor.getHeight());
            this.container.pack();
            this.updateContainerPosition();
        } else {
            this.container.setActor(null);
        }
    }

    private void updateContainerPosition() {
        // TODO consider stage camera transform here, such that this will work on stages with different zoom
        //  and position
        this.container.setPosition(
                Gdx.input.getX() + this.offsetX,
                Gdx.graphics.getHeight()-Gdx.input.getY() - this.container.getHeight() - this.offsetY);
    }

    public void registerTooltip(ToolTipComponent component) {
        this.tooltips.put(component.getClass(), component);
    }

    public void update(float delta) {
        switch(this.state) {
            case HOVER:
                this.hoverTime += delta;
                if(this.hoverTime > this.maxHoverTime) {
                    this.state = SHOWN;
                    if(this.hoveredActor == null) return;
                    this.showTooltip(this.hoveredActor); // Bug: actor can be null here
                    this.logger.finest("Hover > Shown");
                    this.logger.fine("Showing tooltip");
                    this.hoverTime = 0;
                }
                break;
            case HIDING:
                this.hidingTime += delta;
                this.updateContainerPosition();
                if(this.hidingTime > this.maxHidingTime) {
                    // finish hiding and reset everything
                    this.state = HIDDEN;
                    this.hoveredActor = null;
                    this.hideTooltip();
                    if(this.activeTooltip != null) {
                        this.activeTooltip.clearData();
                        this.changeTooltipClass(null, null);
                    }
                    this.logger.finest("hidingTime:" + this.hidingTime);
                    this.logger.finest("Hidden > Hiding");
                    this.logger.finest("Hiding tooltip");
                    this.hidingTime = 0;
                }
                break;
            case SHOWN:
                this.updateContainerPosition();
        }
    }
}
