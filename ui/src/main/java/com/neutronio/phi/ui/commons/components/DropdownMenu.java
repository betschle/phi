package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.SelectBoxWrapper;
import com.neutronio.phi.ui.commons.buttons.AstraXTextButton;
import com.neutronio.phi.ui.commons.buttons.SoloButtonSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * A drop down menu based on {@link TextButton} and {@link com.badlogic.gdx.scenes.scene2d.ui.List}.
 * Size must be set to button.size after initialization was complete.
 */
public class DropdownMenu
        extends SimplePanel {

    // TODO dropdown based on tooltip?
    private SoloButtonSelection<AstraXTextButton> buttonSelection = new SoloButtonSelection<>();
    private List<AstraXTextButton> buttons = new ArrayList<>();
    private List<Dropdown> dropDowns = new ArrayList<>();

    public static class DropDownContent {
        public String dropDownTitle;
        public SelectBoxWrapper<Runnable>[]  dropDownChoices;
    }

    private static class Dropdown {

        private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
        /** Max Dropdown timeout */
        private float maxTimeout = 2f;
        /** Dropdown timeout. If the dropdown is not hovered over with the mouse, it hides itself after a short while. */
        private float timeout = maxTimeout;
        private boolean hovering = false;
        private AstraXTextButton button;
        private ButtonList<AstraXTextButton, SelectBoxWrapper<Runnable>> dropDown;

        public Dropdown(AstraXTextButton textButton, ButtonList dropDownMenu) {
            this.button = textButton;

            this.dropDown = dropDownMenu;
            this.dropDown.addListener(  new ClickListener() {

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    button.setChecked(false);
                    if( dropDown.getSelected() != null ) {
                        logger.fine("Performing action!");
                        dropDown.getSelected().getPayload().run();
                        event.handle();
                    }
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    hovering = true;
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                    hovering = false;
                    if( !button.isChecked() ) {
                        logger.finest("Fade out from dropdown");
                        dropDown.addAction( Tweening.getDropDownFadeOut(0.1f) );
                        event.handle();
                    }
                }
            });
            //////////////////////////////////////////

            this.button.addListener( new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if( button.isChecked() ) {
                        logger.finest("Fade In from button");
                        dropDown.toFront();
                        dropDown.setPosition( button.getX(), 0, Align.topLeft);
                        dropDown.addAction( Tweening.getDropDownFadeIn(0.1f) );
                        // TODO drop down should be behind buttons duriong animation, but in front
                        // of everything else after animation
                        timeout = maxTimeout;
                        event.handle();
                    } else {
                        logger.finest("Fade Out from button");
                        dropDown.addAction( Tweening.getDropDownFadeOut(0.3f) );
                        event.handle();
                    }
                }
            }
            );
        }

        public void update(float delta) {
            if( this.hovering ) {
                this.timeout = this.maxTimeout;
            } else {
                if( this.timeout > 0) this.timeout = this.timeout * (0.92f - delta);
                if( this.timeout < 0.0001f && dropDown.isVisible() ) {
                    this.dropDown.addAction(Tweening.getDropDownFadeOut(0.3f));
                    this.button.setChecked(false);
                    this.timeout = 0;
                }
            }
        }
    }


    public DropdownMenu(ComponentFactory componentFactory) {
        super(componentFactory, "default", null);
        this.buttonSelection.setUncheckLast(true);
    }

    public void clearDropDown() {
        this.dropDowns.clear();
        this.clearChildren();
        this.buttonSelection.clear();
    }

    public void addDropDownEntries(int index, SelectBoxWrapper<Runnable>[] entries) { // entries = runnable?
        Dropdown dropdown = this.dropDowns.get(index);
        // resize button count
        dropdown.dropDown.growButtonCount(entries.length);
        for( SelectBoxWrapper wrapper : entries) {
            dropdown.dropDown.addContent(wrapper);
        }
        dropdown.dropDown.updateToContent();
        dropdown.dropDown.pack();
    }

    public void removeDropDownEntries(int index, SelectBoxWrapper<Runnable>[] entries) {
        Dropdown dropdown = this.dropDowns.get(index);
        // resize button count
        dropdown.dropDown.shrinkButtonCount(entries.length);
        for( SelectBoxWrapper wrapper : entries) {
            dropdown.dropDown.removeContent(wrapper);
        }
        dropdown.dropDown.updateToContent();
        dropdown.dropDown.pack();
    }

    public void addDropDown(DropDownContent dropDownContent) {
        this.addDropDown(dropDownContent.dropDownTitle, dropDownContent.dropDownChoices);
    }

    public void addDropDown(String buttonText, SelectBoxWrapper<Runnable>[]  dropDownChoices) {
        AstraXTextButton button = this.componentFactory.getTextButton("dropDown", buttonText, "dropDown");
        button.setCanCheck(true);
        button.setName("DropDown.TextButton."+buttonText);

        List<SelectBoxWrapper<Runnable>> selectBoxWrappers = AstraXUtil.toList(dropDownChoices);
        ButtonList<AstraXTextButton, SelectBoxWrapper<Runnable>> buttonList = new ButtonList(componentFactory, "transparent");
        buttonList.setName("DropDown.Menu."+buttonText);
        buttonList.setButtonCount(dropDownChoices.length);
        buttonList.addContent(selectBoxWrappers);
        buttonList.setButtonProducer(new Palette.ButtonUpdater<AstraXTextButton, SelectBoxWrapper<Runnable>>() {
            @Override
            public AstraXTextButton createButton(ComponentFactory componentFactory, String style) {
                AstraXTextButton button = new AstraXTextButton(componentFactory, "dropdown");
                button.setTextAlign(Align.left);
                button.setSquishable(false);
                button.setSquishOnClick(false);
                button.setCanCheck(false);
                button.setDisabled(true);
                return button;
            }

            @Override
            public void updateButton(AstraXTextButton button, SelectBoxWrapper<Runnable> selectBoxWrapper) {
                button.setText(selectBoxWrapper.toString());
                button.setTextAlign(Align.left);
                button.setUserObject(selectBoxWrapper);
                button.setDisabled(false);
            }

            @Override
            public void resetButton(AstraXTextButton button) {
                button.setDisabled(true);
                button.setUserObject(null);
            }
        });
        buttonList.setListener(new Palette.PaletteListener<SelectBoxWrapper<Runnable>>() {
            @Override
            public void onSelectedChanged(SelectBoxWrapper<Runnable> selectedItem) {
                if(selectedItem != null)
                    selectedItem.getPayload().run();
            }
        });
        buttonList.fillWithButtons(null);
        buttonList.updateToContent();
        buttonList.pack();

        this.dropDowns.add( new Dropdown(button, buttonList) );
        this.buttonSelection.add(button);
        this.buttons.add(button);
        this.addValue(button);
        this.addActor(buttonList);
        // menu.setPosition( button.getX(), -button.getHeight(), Align.topLeft);
        buttonList.addAction( Tweening.getDropDownFadeOut(0f) );
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for( Dropdown dropdown :this.dropDowns ) {
            dropdown.update(delta);
        }
    }
}
