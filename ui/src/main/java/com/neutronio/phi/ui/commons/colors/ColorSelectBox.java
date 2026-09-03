package com.neutronio.phi.ui.commons.colors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Pools;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.buttons.AstraXButton;

import java.util.Collection;

/**
 * A Color swatch component for picking a color that works
 * similar to a select box or drop down. Fires a change event
 * when a new color is selected.
 */
public class ColorSelectBox
        extends
            Group
        implements
            ColorIconArray.ColorIconArrayListener {

    // TODO add tiny x in the corner
    // TODO stopped working in dressup panel for some reason
    private ComponentFactory factory;

    private ColorIconArray colorIconArray;
    private AstraXButton selectedIcon;

    private Vector2 tempPos = new Vector2();

    protected String soundExpand = "expand";
    protected String soundRetract = "retract";

    public ColorSelectBox(ComponentFactory componentFactory) {
        this.factory = componentFactory;
        this.colorIconArray = new ColorIconArray(componentFactory, "inset");
        this.colorIconArray.setVisible(false);
        this.colorIconArray.setListener(this);
        this.selectedIcon = new AstraXButton(factory, "color-small");
        this.selectedIcon.setCanCheck(true);
        this.selectedIcon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(selectedIcon.isDisabled()) return;
                if(colorIconArray.isVisible()) {
//                    Actor target = event.getTarget();
                    hideColorIcons(0);
                } else {
                    showColorIcons(x, y);
                }
            }
        });
        this.addActor(this.selectedIcon);
        this.colorIconArray.setPosition(this.selectedIcon.getWidth()/2f, this.selectedIcon.getHeight()/2f);
        this.colorIconArray.addListener(new ClickListener() {
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (toActor == null ||
                        !toActor.isDescendantOf(event.getListenerActor())
                        ) {
                    // TODO if I move mouse from icon array to parent, do not hide
                    hideColorIcons(0);
                }
            }
        });
        this.setSize(this.selectedIcon.getMinWidth(), this.selectedIcon.getMinHeight());
    }

    public ColorIconArray getColorIconArray() {
        return colorIconArray;
    }

    /**
     * Sets the disabled state on the icon
     * @param disabled
     */
    public void setDisabled(boolean disabled) {
        this.selectedIcon.setDisabled(disabled);
    }

    public void setColors(Collection<Color> colors) {
        this.colorIconArray.setColorPalette(colors);
    }

    public void setColors(Color... colors) {
        this.colorIconArray.setColorPalette(colors);
    }

    @Override
    public void onColorSelected(int index, Color color) {
        this.setIconColor(color);
        this.hideColorIcons(0.2f);

        ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);
        fire(changeEvent);
        Pools.free(changeEvent);
    }

    public Color getSelectedColor() {
        return this.colorIconArray.getSelectedColor();
    }

    public int getSelectedIndex() {
        return this.colorIconArray.getSelectedIndex();
    }

    /**
     *
     * @param index -1 for empty, < -2 for error state
     */
    public void setSelectedColor(int index) {
        this.colorIconArray.selectColor(index);
        if(index < 0) this.setIconColor(Color.RED);
        else this.setIconColor(colorIconArray.icons[index].getColor());
    }

    private void setIconColor(Color color) {
        this.selectedIcon.getStyle().baseColor.upColor = color;
        this.selectedIcon.getStyle().baseColor.downColor = color;
        this.selectedIcon.getStyle().baseColor.overColor = color;
        this.selectedIcon.getStyle().baseColor.focusedColor = color;
        this.selectedIcon.getStyle().baseColor.focusedCheckedColor = color;
        this.selectedIcon.getStyle().baseColor.checkedColor = color;
        this.selectedIcon.getStyle().baseColor.checkedDownColor = color;
        this.selectedIcon.getStyle().baseColor.checkedOverColor = color;
        this.selectedIcon.getStyle().baseColor.disabledColor = color;
    }

    private void showColorIcons(float x, float y) { // provide x/y from onEnter event here
        if(!this.colorIconArray.hasParent())
            this.getStage().addActor(this.colorIconArray);
        this.tempPos.set(x, y);
        this.tempPos = this.localToStageCoordinates(this.tempPos);
        this.colorIconArray.setPosition(this.tempPos.x, this.tempPos.y);

        this.colorIconArray.toFront();
        this.colorIconArray.addAction(Actions.sequence(
                Actions.show(),
                Actions.fadeIn(0.2f),
                Actions.touchable(Touchable.enabled)
        ));
        factory.playUISound(soundExpand);
    }

    private void hideColorIcons(float delay) {
        this.colorIconArray.addAction(Actions.sequence(
                Actions.delay(delay),
                Actions.fadeOut(0.5f),
                Actions.hide(),
                Actions.touchable(Touchable.disabled)
        ));
        this.selectedIcon.setChecked(false, false);
        this.factory.playUISound(soundRetract);
    }
}
