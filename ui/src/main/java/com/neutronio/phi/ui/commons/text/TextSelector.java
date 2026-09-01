package com.neutronio.phi.ui.commons.text;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pools;
import com.neutronio.astrax.ui.ComponentFactory;
import com.neutronio.astrax.ui.commons.AstraXComponent;
import com.neutronio.astrax.ui.commons.buttons.AstraXButton;
import com.neutronio.astrax.ui.commons.widgets.SelectBoxWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Selects text from a list of texts. Single Selection only. User can click
 * on "next" and "previous" icons to reveal other options, like
 * so: [<<] Text [>>]
 * <br>
 * Logically, works similar to a selectbox, visually very different. Fire
 * ChangeEvent if selection changes.
 */
public class TextSelector<W>
        extends
        AstraXComponent {

    // TODO add styling to this component
    private static String pleaseSelect = "[Select One...]";
    private AstraXButton previousButton;
    private AstraXButton nextButton;
    private Label displayedText;
    private int selectedIndex = -1; // TODO replace with DefaultSelector?
    private List<SelectBoxWrapper<W>> items = new ArrayList<>();

    public TextSelector(ComponentFactory componentFactory) {
        super(componentFactory, "label");
        this.previousButton = new AstraXButton(componentFactory,"icon_inline_arrow_left", "primary-no-check" );
        this.previousButton.setCanCheck(false);
        this.nextButton = new AstraXButton(componentFactory,"icon_inline_arrow_right", "primary-no-check");
        this.nextButton.setCanCheck(false);
        this.displayedText = new Label(pleaseSelect, componentFactory.getSkin(), "default");
        this.displayedText.setAlignment(Align.center, Align.center);
        this.previousButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                previous();
            }
        });

        this.nextButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                next();
            }
        });

        this.add( this.previousButton )
                .size(this.previousButton.getStyle().width,this.previousButton.getStyle().height)
                .padRight(10).right().align(Align.right);
        this.add( this.displayedText).minWidth(200);
        this.add( this.nextButton )
                .size(this.previousButton.getStyle().width,this.previousButton.getStyle().height)
                .size(24,24).padLeft(10).left();
    }

    /**
     * Sets the space between displayed center text and both arrow icons
     * @param space
     */
    public void setTextSpace( float space ) {
        this.getCell( this.displayedText ).space( space );
    }

    /**
     * Sets the minimum width of the center text. Default is 200.
     * @param minWidth
     */
    public void setTextMinWidth( float minWidth ) {
        this.getCell( this.displayedText ).minWidth( minWidth );
    }

    /**
     * Adds a wrapped item to this selector. Also resets current selection.
     * @param wrapper
     */
    public void addItem(SelectBoxWrapper<W> wrapper) {
        this.deselect();
        this.items.add(wrapper);
    }

    /**
     * Sets the items to choose from. Also resets current selection.
     * @param items
     */
    public void setItems( List<SelectBoxWrapper<W>> items) {
        this.deselect();
        this.items.clear();
        this.items.addAll( items );
    }

    public void setItems( SelectBoxWrapper<W>[] items) {
        this.deselect();
        this.items.clear();
        this.items.addAll(Arrays.asList( items ) );
    }

    /**
     * Clears the item list. Also resets current selection.
     */
    public void clearItems() {
        this.deselect();
        this.items.clear();
    }

    /**
     *
     * @return the currently selected index, -1 if no selection is active.
     */
    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    /**
     *
     * @return the selected payload object
     */
    public W getSelectedObject() {
        if( this.selectedIndex < 0) return null;
        return this.items.get(this.selectedIndex).getPayload();
    }

    /**
     * Sets the displayed text to default "please select" text.
     */
    public void deselect() {
        this.selectedIndex = -1;
        this.displayedText.setText(pleaseSelect);
    }

    /**
     * Sets the selected index.
     * @param selectedIndex
     * @param fireEvent if event should be fired
     */
    public void setSelectedIndex(int selectedIndex, boolean fireEvent) { // TODO prevent text selector from firing events here
        if( selectedIndex < 0) return;
        this.selectedIndex = selectedIndex % this.items.size();
        this.displayedText.setText( this.items.get( this.selectedIndex).toString() );

        if(fireEvent) {
            ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);
            boolean cancelled = fire(changeEvent);
            Pools.free(changeEvent);
        }
    }

    /**
     * Attempts to set the selected via the provided wrapped object.
     * Does nothing if selected could not be found in the payload within
     * the contents of SelectBoxWrappers
     * @param selected
     */
    public void setSelected(W selected, boolean fireEvent) {
        int i = 0;
        for( SelectBoxWrapper<W> wrapper : this.items) {
            if(Objects.equals(wrapper.getPayload(), selected)) {
                setSelectedIndex(i, fireEvent);
                break;
            }
            i++;
        }
    }

    /**
     * Selects the next item entry in this selector. Doesn't do anything if
     * there are no items to choose from.
     */
    public void next() {
        if( this.items.size() == 0) return;
        this.selectedIndex = (this.selectedIndex + 1) % this.items.size();
        this.displayedText.setText( this.items.get( this.selectedIndex).toString() );
        this.fire( new ChangeListener.ChangeEvent() );
    }

    /**
     * Selects the previous item entry in this selector. Doesn't do anything if
     * there are no items to choose from.
     */
    public void previous() {
        if( this.items.size() == 0) return;
        this.selectedIndex = this.selectedIndex - 1;
        if( this.selectedIndex < 0) this.selectedIndex = this.items.size()-1;
        this.displayedText.setText( this.items.get( this.selectedIndex).toString() );
        this.fire( new ChangeListener.ChangeEvent() );
    }
}
