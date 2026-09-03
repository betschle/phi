package com.neutronio.phi.ui.commons.pagination;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.buttons.AstraXButton;
import com.neutronio.phi.ui.commons.buttons.SoloButtonSelection;
import com.neutronio.phi.ui.skin.PhiUITranslations;

/**
 * Basically a bunch of knobs where each represents a page.
 */
public class BubblePaginator extends HorizontalGroup {
    // basically a bunch of knobs
    private ComponentFactory factory;
    private Paginator paginator;
    private SoloButtonSelection<AstraXButton> buttonGroup;

    public BubblePaginator(ComponentFactory componentFactory) {
        this.space(10);
        this.factory = componentFactory;
        this.buttonGroup = new SoloButtonSelection<>();
        // TODO activate force select
//        this.buttonGroup.setMaxCheckCount(1);
//        this.buttonGroup.setMinCheckCount(1);
        this.buttonGroup.setUncheckLast(true);
    }

    /**
     * Sets the paginator to control
     * @param defaultPaginator
     */
    public void setPaginator(Paginator defaultPaginator) {
        this.clearChildren();
        this.buttonGroup.clear();
        this.paginator = defaultPaginator;
        for( int i = 0; i < this.paginator.getMaxPages(); i++) {
            AstraXButton button = new AstraXButton(factory, "bubblePaginatorButton");
            button.setCanCheck(true);
            button.setUserObject(i);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if( actor.getUserObject() instanceof Integer) {
                        int buttonIndex = (int) actor.getUserObject();
                        getPaginator().setCurrentPage(buttonIndex+1);
                    }
                }
            });
            String paginatorText = factory.translate(PhiUITranslations.PAGINATOR_PAGE, (i + 1), this.paginator.getMaxPages());
            button.addListener(factory.getToolTip(paginatorText));

            this.addActor(button);
            this.buttonGroup.add(button);

            if( i == defaultPaginator.getCurrentPage()-1 )
                button.setChecked(true);
        }
    }

    private Paginator getPaginator() {
        return paginator;
    }

    /**
     * Updates the component to display what's currently stored in the underlying paginator
     */
    public void update() {
        this.buttonGroup.setChecked(this.paginator.getCurrentPage()-1);
    }

    /**
     * Invokes an update for when the amount of pages changed
     */
    public void updateChange() {
        this.setPaginator(this.paginator);
    }
}
