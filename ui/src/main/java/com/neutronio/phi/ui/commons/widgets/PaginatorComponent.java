package com.neutronio.phi.ui.commons.widgets;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.AstraXComponent;
import com.neutronio.phi.ui.commons.Paginator;
import com.neutronio.phi.ui.commons.buttons.AstraXButton;
import com.neutronio.phi.ui.commons.text.TextSelector;

/**
 * Works very similar to a {@link TextSelector}, except
 * displays the current page and is more light-weight.
 * Works as a controller for a {@link Paginator} object.
 */
public class PaginatorComponent
        extends
            AstraXComponent {

    // TODO style?
    private Paginator paginator;
    private AstraXButton previousButton;
    private AstraXButton nextButton;
    private Label displayedText;

    public PaginatorComponent(ComponentFactory componentFactory) {
        super(componentFactory);
        this.previousButton = new AstraXButton(componentFactory, "icon_inline_arrow_left", "primary-no-check");
        this.previousButton.setCanCheck(false);
        this.nextButton = new AstraXButton(componentFactory, "icon_inline_arrow_right", "primary-no-check");
        this.nextButton.setCanCheck(false);
        this.displayedText = new Label("0", componentFactory.getSkin(), "default");
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
                .padLeft(10).left();
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
        this.update();
    }

    public int getMaxPages() {
        return this.paginator.getMaxPages();
    }

    /**
     * Selects the next item entry in this selector. Doesn't do anything if
     * there are no items to choose from.
     */
    public void next() {
        this.paginator.nextPage();
        this.update();
        this.fire( new ChangeListener.ChangeEvent() );
    }

    /**
     * Selects the previous item entry in this selector. Doesn't do anything if
     * there are no items to choose from.
     */
    public void previous() {
        this.paginator.previousPage();
        this.update();
        this.fire( new ChangeListener.ChangeEvent() );
    }

    /**
     * Updates the display text pn the paginator
     */
    public void update() {
        this.displayedText.setText( this.paginator.getCurrentPage() + "/" + this.paginator.getMaxPages() );
    }
}
