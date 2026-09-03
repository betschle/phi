package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.AstraXComponent;
import com.neutronio.phi.ui.commons.buttons.Button;
import com.neutronio.phi.ui.commons.buttons.SoloButtonSelection;
import com.neutronio.phi.ui.commons.pagination.DefaultPaginator;
import com.neutronio.phi.ui.commons.pagination.Paginator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * A generic palette component from which one item can be
 * chosen. The user picks the item via a button, the buttons should have checked states in their styles.
 * It keeps track of the selected item which also
 * can be changed programmatically and also features pagination. Pagination can be activated by
 * creating a new Paginator UI component and connecting it to the DefaultPaginator.
 * @param <ITEM>
 */
public class Palette<BUTTON extends Button, ITEM>
        extends
            AstraXComponent
        implements
            Paginator {
    // TODO Palette needs to work with MultiSelection for Bookmarks in particular

    // TODO for all instances of Palette: update paginator when content in palette changes
    // TODO all buttons inside palette should have a predefined/maximum size to avoid resizing on content change
    //  this issue only exists with text button btw

    // config
    protected Logger logger = Logger.getLogger(Palette.class.getCanonicalName());
    protected int prefIconSize = 24; // style?
    /** Total icon count, distributed across columns */
    protected int iconCount = 25;
    /** Column count */
    protected int columns = 5;

    /** A default paginator for this palette. It is wrapped inside this component, which also implements
     * the paginator interface.*/
    protected DefaultPaginator defaultPaginator = new DefaultPaginator();

    protected List<ITEM> content = new ArrayList<>(); // model
    protected List<BUTTON> buttons = new ArrayList<>();
    /** This listener is invoked when changes to the selection are made.  */
    protected PaletteListener<ITEM> listener;
    /** This object is used to customize creation and updating behavior for the buttons. */
    protected ButtonUpdater<BUTTON, ITEM> buttonUpdater;
    /** For button selection. */
    protected SoloButtonSelection<Button> buttonGroup = new SoloButtonSelection<>();

    // TODO implement?
    public static class PaletteStyle {
        public String baseButtonStyle;
        public int iconCount = 24;
        public int columns = 5;
    }

    public interface PaletteListener<T> {
        /**
         * Called when the selection inside the palette changes.
         * Note: If there is a button inside a button (e.g. CompositeButton) the child button
         * needs a listener and call event.setBubble(false), otherwise all clicks will also trigger the parent (this) event.
         * @param selectedItem
         */
        void onSelectedChanged(T selectedItem);
    }

    /**
     * Allows for customizing the button type on Palette. Ensures palette is functioning with
     * all button types.
     * @param <B>
     */
    public interface ButtonUpdater<B extends Button, ITEM> {
        B createButton(ComponentFactory componentFactory, String style);
        /** This needs to update the button display and also set the user object to item for
         * the onSelectionChanged() event to work. */
        void updateButton(B button, ITEM item);
        /** This needs to update the button display to empty and also unset the user object. */
        void resetButton(B button);
    }

    public void setButtonProducer(ButtonUpdater<BUTTON, ITEM> buttonUpdater) {
        this.buttonUpdater = buttonUpdater;
    }

    public Palette(ComponentFactory factory) {
        super(factory);
        this.buttonGroup.setUncheckLast(true);
    }

    public SoloButtonSelection<Button> getButtonGroup() {
        return buttonGroup;
    }

    public List<ITEM> getContent() {
        return content;
    }

    public void setListener(PaletteListener<ITEM> listener) {
        this.listener = listener;
    }

    public int getIconCount() {
        return iconCount;
    }

    public int getItemCount() {
        return this.content.size();
    }

    // rename to buttonCount
    public void setIconCount(int iconCount) {
        this.iconCount = iconCount;
    }

    //  the override could make problems here as this is a gdx method
    @Override
    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getCurrentPage() {
        return this.defaultPaginator.getCurrentPage();
    }

    @Override
    public void setCurrentPage(int currentPage) {
        this.defaultPaginator.setCurrentPage(currentPage);
        this.updateToContent();
        // TODO uncheck all only if selected index "out of sight"
        if(this.buttonGroup != null)
            this.buttonGroup.uncheckAll();
    }

    public int getPrefIconSize() {
        return prefIconSize;
    }

    public void setPrefIconSize(int prefIconSize) {
        this.prefIconSize = prefIconSize;
    }

    public void setButtonGroup(SoloButtonSelection<Button> buttonGroup) {
        this.buttonGroup = buttonGroup;
    }

    /**
     * Fills the palette with buttons. Palette must be filled with items before calling this.
     */
    public void fillWithButtons(String astraXButtonStyle) {
        for( int i =0; i < this.iconCount; i++) {
            final BUTTON button = this.buttonUpdater.createButton(componentFactory, astraXButtonStyle);
            if(this.buttonGroup != null)
                this.buttonGroup.add(button);

            this.buttons.add(button);

            if( button instanceof Actor) {
                Actor buttonActor = (Actor) button;
                if(button.canCheck()) {
                    buttonActor.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if (getListener() != null && !button.isDisabled() && !event.isStopped()) {
                                getListener().onSelectedChanged((ITEM) ((Actor) button).getUserObject());
                            }
                        }
                    });
                } else {
                    // a button with canCheck = false does not fire Change Events, so listen to click events instead
                    // changing this flag afterwards is not intended and breaks this code
                    buttonActor.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            if (getListener() != null && !button.isDisabled() && !event.isStopped()) {
                                getListener().onSelectedChanged((ITEM) ((Actor) button).getUserObject());
                            }
                        }
                    });
                }

                // pad right needs to be left out when button.size() = 1
                if(this.buttons.size() % this.columns == 0) {
                    this.add((Actor) button).center().fillX()
                            .padBottom(5);
                    this.row();
                } else {
                    this.add((Actor) button).center().fillX()
                            .padRight(5).padBottom(5);
                }
            }
        }
        this.defaultPaginator.configure(this.content.size(), this.iconCount);
        this.buttonGroup.uncheckAll();
    }

    /**
     * Updates button content according to current page
     */
    public void updateToContent() {
        this.defaultPaginator.configure(this.content.size(), this.iconCount);
        int startIndex = (this.defaultPaginator.getCurrentPage()-1) * this.buttons.size();
        int buttonIndex = 0;
        // clear all button displays
        for( BUTTON button : this.buttons) {
            this.buttonUpdater.resetButton(button);
        }
        // build it back up
        for( int i = startIndex;
                i < startIndex + this.content.size() &&
                i < this.content.size()  &&
                buttonIndex < this.buttons.size(); i++ ) {

            BUTTON button = this.buttons.get(buttonIndex);
            this.buttonUpdater.updateButton(button, this.content.get(i));
            buttonIndex++;
        }
        this.pack();
    }

    /**
     * Marks the item as selected in this palette. Updates
     * the selection, Moves the paginator to the correct page within
     * the component and also correctly marks the related button.
     * @param item
     */
    public void setSelected(ITEM item) { // TODO write test for this
        int contentIndex = this.content.indexOf(item);
        // set the page & button index to be selected
        int page = contentIndex / this.columns;
        this.defaultPaginator.setCurrentPage(page+1);
        // update button selection
        int buttonIndex = contentIndex - ( page * this.buttons.size());
        if( this.buttonGroup != null)
            this.buttonGroup.uncheckAll();
        int i = 0;
        for( BUTTON button : this.buttons) {
            if( buttonIndex == i ) button.setChecked(true);
            else button.setChecked(false);
            i++;
        }
    }

    /**
     * Sets the selected index. -1 for deselection.
     * TODO set selected buttons without group
     * @param contentIndex
     */
    public void setSelectedIndex(int contentIndex) {
        if( contentIndex < 0) {
            if( this.buttonGroup != null)
                this.buttonGroup.uncheckAll();
        } else {
            // set the page & button index to be selected
            int page = (contentIndex / this.columns);
            this.defaultPaginator.setCurrentPage(page+1);
            int buttonIndex = contentIndex - (page * this.buttons.size());
            if( this.buttonGroup != null)
                this.buttonGroup.uncheckAll();
            int i = 0;
            // update button selection
            for( Button button : this.buttons) {
                if( buttonIndex == i ) button.setChecked(true);
                else button.setChecked(false);
                i++;
            }
        }
    }

    /**
     * Removes an item
     * @param item the item data
     */
    public void removeItem(ITEM item) {
        this.content.remove(item);
    }

    /**
     * Adds an item
     * @param item the item data
     */
    public void addItem(ITEM item) {
        this.content.add(item);
    }

    /**
     * Adds multiple items
     * @param items the item data
     */
    public void addItems(List<ITEM> items) {
        this.content.addAll(items);
    }

    /**
     * Adds an item assigned to an inline icon to the palette
     * @param item the item data
     * @param paletteIcon the region of the inline icon to use
     */
    @Deprecated
    public void addItem(ITEM item, TextureRegion paletteIcon) {
        this.content.add(item);
    }

    /**
     * Adds an item assigned to an inline icon and respective color to the palette
     * @param item
     * @param paletteIcon
     * @param color
     */
    @Deprecated
    public void addItem(ITEM item, TextureRegion paletteIcon, Color color) {
        this.content.add(item);
    }

    public void deselect() {
        if(this.buttonGroup != null)
            this.buttonGroup.uncheckAll();
    }

    public void clearItems() {
        this.content.clear();
        this.updateToContent();
    }

    /**
     *
     * @param list
     */
    public void setItems( Collection<ITEM> list) {
        // TODO ModuleSettingsPanel as tooltip would be great here, but sadly is not working!!
        // a simple tooltip does work however
//        for(AstraXIcon button : this.swatches.getButtons()) {
//            // button.addListener( new Tooltip<>( new Label("Test", componentFactory.getSkin())));
//            button.addListener( new Tooltip<>( this.modulePanel));
//        }
        this.content.clear();
        this.content.addAll( list );
        if(this.buttonGroup != null)
            this.buttonGroup.uncheckAll();
        this.updateToContent();
    }

    /**
     *
     * @param list
     */
    public void setItems( List<ITEM> list) {
        this.content.clear();
        this.content.addAll( list );
        if(this.buttonGroup != null)
            this.buttonGroup.uncheckAll();
        this.updateToContent();
    }

    /**
     * Invokes an update of a specific button.
     * @param ofItem
     */
    public void updateButton(ITEM ofItem) {
        int contentIndex = this.content.indexOf(ofItem);
        int page = (contentIndex / this.buttons.size())+1;
        if( page == this.getCurrentPage()) {
            int buttonIndex = contentIndex - ((this.defaultPaginator.getCurrentPage()-1) * this.buttons.size());
            this.buttonUpdater.updateButton(this.buttons.get(buttonIndex), ofItem );
        }
    }

    @Override
    public int getMaxPages() {
        return this.defaultPaginator.getMaxPages();
    }

    @Override
    public void setMaxPages(int maxPages) {
        throw new UnsupportedOperationException("This method is not supposed to be used. Use the Paginator within this component instead.");
    }

    @Override
    public void configure(int totalItems, int itemsPerPage) {
        throw new UnsupportedOperationException("This method is not supposed to be used. Use the Paginator within this component instead.");
    }

    @Override
    public void nextPage() {
        this.defaultPaginator.nextPage();
        this.updateToContent();
        if( this.buttonGroup != null)
            this.buttonGroup.uncheckAll();
    }

    @Override
    public void previousPage() {
        this.defaultPaginator.previousPage();
        this.updateToContent();
        if( this.buttonGroup != null)
            this.buttonGroup.uncheckAll();
    }

    public List<BUTTON> getButtons() {
        return buttons;
    }

    public PaletteListener<ITEM> getListener() {
        return listener;
    }

    /**
     *
     * @return null if selected button holds nothing
     */
    public ITEM getSelected() { // TODO write test for this
        if( this.buttonGroup == null) return null;
        int itemIndex = this.buttonGroup.getCheckedIndex() + ((this.defaultPaginator.getCurrentPage()-1)* this.buttons.size());
        if( itemIndex < this.content.size() && itemIndex >= 0 ) {
            return this.content.get(itemIndex);
        } else return null;
    }

    public int getSelectedIndex() { // TODO write test for this
        if( this.buttonGroup == null) return -1;
        int itemIndex = this.buttonGroup.getCheckedIndex() + ((this.defaultPaginator.getCurrentPage()-1)* this.buttons.size());
        if( itemIndex < this.content.size() && itemIndex >= 0 ) {
            return itemIndex;
        } else return -1;
    }
}
