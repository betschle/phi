package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.buttons.Button;
import com.neutronio.phi.ui.commons.buttons.SoloButtonSelection;
import com.neutronio.phi.ui.commons.pagination.DefaultPaginator;
import com.neutronio.phi.ui.commons.pagination.Paginator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A single row list of buttons. Works like a one-column {@link com.neutronio.astrax.ui.editor.Palette}
 * Features pagination, provides methods for dynamic resizing of
 * the displayed content area.
 * Replaces the very crappy {@link com.badlogic.gdx.scenes.scene2d.ui.List} implementation.
 */
public class ButtonList<BUTTON extends Button, ITEM>
        extends
            SimplePanel
        implements
            Paginator {

    protected int buttonColumns = 1;
    /** Total button count */
    protected int buttonCount = 10;
    protected List<ITEM> content = new ArrayList<>();
    protected List<BUTTON> buttons = new ArrayList<>();
    /** This object is used to customize creation and updating behavior for the buttons. */
    protected Palette.ButtonUpdater<BUTTON, ITEM> buttonProducer;
    /** For button selection. */
    protected SoloButtonSelection<Button> buttonGroup = new SoloButtonSelection<>();
    /** A default paginator for this palette. It is wrapped inside this component, which also implements
     * the paginator interface.*/
    protected DefaultPaginator defaultPaginator = new DefaultPaginator();
    /** This listener is invoked when changes to the selection are made.  */
    protected Palette.PaletteListener<ITEM> listener;

    public ButtonList(ComponentFactory componentFactory) {
        super(componentFactory);
    }

    public ButtonList(ComponentFactory componentFactory, String backgroundStyle) {
        super(componentFactory, "default", backgroundStyle);
    }

    // method from palette. create an Interface?
    public void setButtonProducer(Palette.ButtonUpdater<BUTTON, ITEM> buttonUpdater) {
        this.buttonProducer = buttonUpdater;
    }

    /**
     * Adds byCount buttons to this list.
     * updateToContent() must be called after calling this method
     * @param byCount
     */
    public void growButtonCount(int byCount) {
        this.buttonCount += Math.abs(byCount);

        for( int i =0 ; i < byCount; i++) {
            this.addButton();
        }

        this.defaultPaginator.configure(this.content.size(), this.buttonCount);
    }

    /**
     * Clears the children of this list too, and builds it back up.
     * updateToContent() must be called after calling this method
     * @param byCount
     */
    public void shrinkButtonCount(int byCount) {
        this.clearChildren();
        this.construct();
        this.buttonCount -= Math.abs(byCount);

        for(int i =0; i < byCount; i++) {
            BUTTON button = this.buttons.remove(0);
            this.buttonGroup.remove(button);
        }
        // reconstruct
        for( BUTTON button : this.buttons) {
            // pad right needs to be left out when button.size() = 1
            if(this.buttons.size() % this.buttonColumns == 0) {
                this.add((Actor) button).center()
                        .padBottom(5);
                this.row();
            } else {
                this.add((Actor) button).center()
                        .padRight(5).padBottom(5);
            }
        }
        this.defaultPaginator.configure(this.content.size(), this.buttonCount);
    }

    private void addButton() {
        final BUTTON button = this.buttonProducer.createButton(componentFactory, null);
        if (this.buttonGroup != null)
            this.buttonGroup.add(button);

        this.buttons.add(button);

        if (button instanceof Actor) {
            Actor buttonActor = (Actor) button;
            buttonActor.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (getListener() != null && !button.isDisabled() && !event.isStopped()) {
                        // below line will not work if button.canCheck is false, so call via userobject
                        getListener().onSelectedChanged((ITEM) ((Actor) button).getUserObject());
                    }
                }
            });

            this.add((Actor) button).center()
                    //.minSize(button.getStyle().width, button.getStyle().height)
                    .padRight(5).padBottom(5);

            if(this.buttons.size() % this.buttonColumns == 0) this.row();
        }
    }

    /**
     * How many columns of buttons there shall be. Button columns != gdx.table columns.
     * @return
     */
    public int getButtonColumns() {
        return buttonColumns;
    }

    public void setButtonColumns(int buttonColumns) {
        this.buttonColumns = buttonColumns;
    }


    // method from palette. create an Interface?
    /**
     * Updates button content according to current page
     */
    public void updateToContent() {
        this.defaultPaginator.configure(this.content.size(), this.buttonCount);
        int startIndex = (this.defaultPaginator.getCurrentPage()-1) * this.buttons.size();
        int buttonIndex = 0;
        // clear all button displays
        for( BUTTON button : this.buttons) {
            this.buttonProducer.resetButton(button);
        }
        // build it back up
        for( int i = startIndex;
             i < startIndex + this.content.size() &&
                     i < this.content.size()  &&
                     buttonIndex < this.buttons.size(); i++ ) {

            BUTTON button = this.buttons.get(buttonIndex);
            this.buttonProducer.updateButton(button, this.content.get(i));
            buttonIndex++;
        }
        this.pack();
    }

    // method from palette. create an Interface?
    public void fillWithButtons(String astraXButtonStyle) {
        for( int i =0; i < this.buttonCount; i++) {
            this.addButton();
        }
        this.defaultPaginator.configure(this.content.size(), this.buttonCount);
    }

    public void setButtonCount(int buttonCount) {
        this.buttonCount = buttonCount;
    }

    public void setListener(Palette.PaletteListener<ITEM> listener) {
        this.listener = listener;
    }

    public Palette.PaletteListener<ITEM> getListener() {
        return listener;
    }
    /**
     *
     * @param list
     */
    public void setContent(Collection<ITEM> list) {
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
    // method from palette. create an Interface?
    public void setContent(List<ITEM> list) {
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
    // method from palette. create an Interface?
    public void updateButton(ITEM ofItem) {
        int contentIndex = this.content.indexOf(ofItem);
        int page = (contentIndex / this.buttons.size())+1;
        if( page == this.getCurrentPage()) {
            int buttonIndex = contentIndex - ((this.defaultPaginator.getCurrentPage()-1) * this.buttons.size());
            this.buttonProducer.updateButton(this.buttons.get(buttonIndex), ofItem );
        }
    }

    /**
     * Removes an item
     * @param item the item data
     */
    // method from palette. create an Interface?
    public void removeContent(ITEM item) {
        this.content.remove(item);
    }

    /**
     * Adds an item
     * @param item the item data
     */
    // method from palette. create an Interface?
    public void addContent(ITEM item) {
        this.content.add(item);
    }

    /**
     * Adds multiple items
     * @param items the item data
     */
    // method from palette. create an Interface?
    public void addContent(List<ITEM> items) {
        this.content.addAll(items);
    }

    // method from palette. create an Interface?
    public void clearContent() {
        this.content.clear();
        this.updateToContent();
    }

    public void deselect() {
        if(this.buttonGroup != null)
            this.buttonGroup.uncheckAll();
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

    @Override
    public int getCurrentPage() {
        return 0;
    }

    @Override
    public void setCurrentPage(int currentPage) {

    }
}
