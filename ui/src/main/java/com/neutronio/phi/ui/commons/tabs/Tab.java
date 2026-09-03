package com.neutronio.phi.ui.commons.tabs;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.AstraXComponent;
import com.neutronio.phi.ui.commons.Background;
import com.neutronio.phi.ui.commons.StackController;
import com.neutronio.phi.ui.commons.buttons.AstraXTextButton;
import com.neutronio.phi.ui.commons.buttons.SoloButtonSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * A Tabbed component. Shows different content depending on which
 * tab was clicked on.
 */
public class Tab extends AstraXComponent {

    protected Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
    protected TabStyle tabStyle;
    /** Selection control for tab buttons */
    protected SoloButtonSelection<AstraXTextButton> tabButtonGroup;
    protected Table tabButtons;
    protected Stack contentStack;
    protected List<TabContent> contents = new ArrayList<>();
    /** Fade controller for content tabs */
    protected StackController stackController;

    public static class TabStyle {
        public Background.BackgroundStyle backgroundStyle;
        public String tabButtonStyle;
    }

    private class TabContent extends ClickListener {
        private boolean hidden = false;
        private AstraXTextButton tabButton;
        private Group tabContent;

        public TabContent(AstraXTextButton tabButton, Group tabContent) {
            this.tabButton = tabButton;
            this.tabContent = tabContent;
        }

        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            stackController.show(tabContent);
        }
    }

    // TODO add

    /**
     *
     * @param factory
     * @param styleName use default for standard style
     * @param backgroundStyle null for no background
     */
    public Tab(ComponentFactory factory, String styleName, String backgroundStyle) {
        super(factory); // init with empty background, so it can be set properly later
        this.setTabStyle( factory.getSkin().get(styleName, TabStyle.class) );
        if( backgroundStyle != null) {
            this.setBackgroundStyle(factory.getSkin().get(backgroundStyle, Background.BackgroundStyle.class));
        }
        this.tabButtonGroup = new SoloButtonSelection<>();
        this.tabButtonGroup.setUncheckLast(true);
        this.tabButtons = new Table();
        this.contentStack = new Stack();
        this.stackController = new StackController(contentStack);
    }


    public TabStyle getTabStyle() {
        return tabStyle;
    }

    public void setTabStyle(TabStyle tabStyle) {
        this.tabStyle = tabStyle;
    }


    @Override
    public void construct() {
        this.clear();
        super.construct();
        this.add(tabButtons).padTop(5).padBottom(10).left();
        this.row();
        this.add(contentStack).padBottom(15);
        this.pack();
        this.stackController.show(0);
    }

    // TODO hide tabs. Must reconstruct buttons

    /**
     * Toggles tab contents by its class
     * @param clazz
     */
    public void toggleTab(Class clazz) {
        for( TabContent content : this.contents) {
            if( clazz.isInstance(content.tabContent) ) {
                content.hidden = !content.hidden;
                updateTabButtons();
                break;
            }
        }
    }

    /**
     * Shows or hides a tab contents by its class.
     * If false, prevents a whole tab and its contents to show up as tab.
     * @param clazz
     */
    public void setTabVisible(Class clazz, boolean visible) {
        for( TabContent content : this.contents) {
            if( clazz.isInstance(content.tabContent) ) {
                content.hidden = !visible;
                updateTabButtons();
                break;
            }
        }
    }

    /**
     * If the tab button for a content class is visible
     * @param clazz
     * @return
     */
    public boolean isTabVisible(Class clazz) {
        for( TabContent content : this.contents) {
            if( clazz.isInstance(content.tabContent) ) {
                return !content.hidden;
            }
        }
        return false;
    }

    /**
     * Rebuilds the button group and only adds buttons that are visible.
     */
    private void updateTabButtons() {
        // rebuild the whole button group
        this.tabButtons.clear();
        for (TabContent tabContent : this.contents) {
            if( !tabContent.hidden ) {
                this.tabButtons.add(tabContent.tabButton).padRight(5);
            }
        }
        this.tabButtons.pack();
    }

    public void addTab(String title, Group content) {
        content.setVisible(false);
        this.contentStack.add(content);

        AstraXTextButton tabButton = new AstraXTextButton( this.componentFactory, this.tabStyle.tabButtonStyle);
        tabButton.setTextAlign(Align.center);
        tabButton.setText(title);

        TabContent tabContent = new TabContent(tabButton, content);
        tabButton.addListener( tabContent );

        this.tabButtonGroup.add(tabButton);
        this.tabButtons.add( tabButton ).padRight(5);
        this.contents.add(tabContent);
    }
}
