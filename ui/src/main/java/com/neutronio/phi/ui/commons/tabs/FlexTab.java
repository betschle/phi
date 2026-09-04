package com.neutronio.phi.ui.commons.tabs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.PhiException;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.AstraXComponent;
import com.neutronio.phi.ui.commons.StackController;
import com.neutronio.phi.ui.commons.buttons.AstraXButton;
import com.neutronio.phi.ui.commons.buttons.AstraXTextButton;
import com.neutronio.phi.ui.commons.buttons.Button;
import com.neutronio.phi.ui.commons.buttons.SoloButtonSelection;

import java.util.ArrayList;
import java.util.List;

/**
 * A Flexible tab, that allows for free tab button alignment.
 * Stores buttons in a list to freely use in layout. Both normal
 * and text buttons can be added. The components of this tab must be
 * added to a layout to be usable, contentStack
 * and tabButtons in particular.
 */
public class FlexTab
        extends AstraXComponent {

    // TODO this class does not make use of ButtonSelection properly and is not working
    /** Selection control for tab buttons */
    protected SoloButtonSelection<Button> tabButtonGroup;
    /** The tab buttons */
    protected List<Button> tabButtons;
    protected Stack contentStack;
    protected List<TabContent> contents = new ArrayList<>();
    /** Fade controller for content tabs */
    protected StackController stackController;
    protected FlexTabListener listener;

    public interface FlexTabListener {
        void onTabChanged(Button button, Group content);
    }

    private class TabContent extends ClickListener {
        private boolean hidden = false;
        private Button tabButton;
        private Group tabContent;

        public TabContent(Button tabButton, Group tabContent) {
            this.tabButton = tabButton;
            this.tabContent = tabContent;
        }

        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            if(tabButton.isDisabled()) return;
            stackController.show(tabContent);
            if( listener != null)
                listener.onTabChanged(tabButton, tabContent);
        }
    }

    public FlexTab(ComponentFactory componentFactory) {
        this(componentFactory, "tab");
    }

    public FlexTab(ComponentFactory componentFactory, String backgroundStyle) {
        super(componentFactory, backgroundStyle);
        this.tabButtonGroup = new SoloButtonSelection<>();
        this.tabButtons = new ArrayList<>();
        this.contentStack = new Stack();
//        this.contentStack.setFillParent(true);
        this.stackController = new StackController(contentStack);
    }

    public void setListener(FlexTabListener listener) {
        this.listener = listener;
    }

    public StackController getStackController() {
        return stackController;
    }

    /**
     * Shows a tab by its index and properly selects its tab button as well
     * @param index
     */
    public void showTab(int index) {
        this.stackController.show(index);
        if(index < 0) return;
        this.tabButtonGroup.updateCheckedState(this.tabButtons.get(index), true);
    }

    /**
     * Shows a tab by its actor and properly selects its tab button as well
     * @param ofActor
     */
    public void showTab(Actor ofActor) {
        int index = this.stackController.show(ofActor);
        if(index < 0) return;
        this.tabButtonGroup.updateCheckedState(this.tabButtons.get(index), true);
    }

    /**
     * Shows a tab by its class and properly selects its tab button as well
     * @param byClass the class of contents, only works for different content classes. If
     *                many content actors have the same class, the first entry will be shown
     */
    public void showTab(Class byClass) {
        int index = this.stackController.show(byClass);
        if(index < 0) return;
        this.tabButtonGroup.updateCheckedState(this.tabButtons.get(index), true);
    }

    @Override
    public void construct() {
        this.clear();
        super.construct();
    }

    /**
     * Creates and adds a button that shows the given content as tab.
     * @param buttonStyle
     * @param iconDrawable
     * @param content
     */
    public AstraXButton addTabWithIconButton(String buttonStyle, String iconDrawable, Group content) {
        content.setVisible(false);
        this.contentStack.add(content);

        AstraXButton tabButton = new AstraXButton( this.componentFactory, iconDrawable, buttonStyle);

        TabContent tabContent = new TabContent(tabButton, content);
        tabButton.addListener(tabContent);

        this.tabButtonGroup.add(tabButton);
        this.tabButtons.add( tabButton);
        this.contents.add(tabContent);
        return tabButton;
    }

    /**
     * Creates and adds a text button that shows the given content as tab.
     * @param title
     * @param textButtonStyle
     * @param content
     */
    public AstraXTextButton addTabWithTextButton(String title, String textButtonStyle, Group content) {
        content.setVisible(false);
        this.contentStack.add(content);

        AstraXTextButton tabButton = new AstraXTextButton( this.componentFactory, textButtonStyle);
        tabButton.setTextAlign(Align.center);
        tabButton.setText(title);
        tabButton.setSquishable(false);

        TabContent tabContent = new TabContent(tabButton, content);
        tabButton.addListener(tabContent);

        this.tabButtonGroup.add(tabButton);
        this.tabButtons.add(tabButton);
        this.contents.add(tabContent);
        return tabButton;
    }

    /**
     * Adds a preexisting button that shows the given content as tab.
     * @param button
     * @param content
     */
    public void addTabWithButton(Button button, Group content) {
        content.setVisible(false);
        this.contentStack.add(content);

        TabContent tabContent = new TabContent(button, content);
        if( button instanceof Actor) {
            ((Actor)button).addListener(tabContent);
        } else throw new PhiException(PhiException.ErrorCode.E0004, "Button must be an actor");

        this.tabButtonGroup.add(button);
        this.tabButtons.add(button);
        this.contents.add(tabContent);
    }

    /**
     * Gets the tab buttons for free use in any layout. The layout code must be written on use.
     * @return
     */
    public List<Button> getTabButtons() {
        return tabButtons;
    }

    /**
     * Gets the content stack for free use in any layout. The layout code must be written on use.
     * @return
     */
    public Stack getContentStack() {
        return contentStack;
    }
}
