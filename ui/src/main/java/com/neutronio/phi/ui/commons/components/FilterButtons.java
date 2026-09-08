package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.buttons.AstraXTextButton;
import com.neutronio.phi.ui.commons.buttons.MultiButtonSelection;

import java.util.ArrayList;
import java.util.List;

/**
 * Specifically made to apply filters to palettes
 * @param <P> the filter payload object, e.g. a String tag or an Enum
 */
public class FilterButtons<P> extends SimplePanel {

    // TODO allow AstraXButtons too, add a button producer then!
    public static class Filter<P> {
        /** Displayed filter name. If using AstraXButtons, this is the tooltip. */
        public String displayName;
        /** Internal filter payload, can be a simple string tag, an integer or an enum */
        public P payload;
        /** If using AstraXButtons, the inline icon. Field ignored for text only buttons */
        public String inlineIcon;

        public Filter(String displayName) {
            this.displayName = displayName;
        }
        public Filter(String displayName, P payload) {
            this.displayName = displayName;
            this.payload = payload;
        }

        @Override
        public String toString() {
            return "Filter{" +
                    "displayName='" + displayName + '\'' +
                    '}';
        }
    }
    protected List<Filter<P>> filters;
    protected MultiButtonSelection<AstraXTextButton> filterButtonGroup;
    protected int columns = 3;

    public FilterButtons(ComponentFactory componentFactory) {
        super(componentFactory);

        this.filterButtonGroup = new MultiButtonSelection<>();
        this.filterButtonGroup.setMaxCheckCount(5); // TODO make this customizable
        this.filterButtonGroup.setMinCheckCount(0);
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setFilterOptions(List<Filter<P>> filters) {
        for(AstraXTextButton button : this.filterButtonGroup.getButtons()) {
            button.remove();
        }
        this.filterButtonGroup.clear();
        this.filters = filters;
        if(this.filters.isEmpty()) {
            this.addLabel("No filters available");
            return;
        }
        this.addLabel("Filter:");
        this.row();
        for(int i = 0; i < this.filters.size(); i++) {
            AstraXTextButton button = new AstraXTextButton(componentFactory, "secondary-compact");
            button.setUserObject(this.filters.get(i));
            button.setText(this.filters.get(i).displayName);
            button.setSquishable(false);
            // pad right needs to be left out when button.size() = 1
            if((i+1) % this.columns == 0) {
                this.add((Actor) button).center().fillX()
                        .padBottom(5);
                this.row();
            } else {
                this.add((Actor) button).center().fillX()
                        .padRight(5).padBottom(5);
            }
            this.filterButtonGroup.add(button);
        }
        this.pack();
    }

    public void setFilterOptions(Filter<P>... filters) {
        this.setFilterOptions(List.of(filters));
    }

    public List<Integer> getSelectedIndices() {
        return this.filterButtonGroup.getCheckedIndices();
    }

    public List<Filter<P>> getSelectedFilters() {
        List<Integer> checkedIndices = this.filterButtonGroup.getCheckedIndices();
        List<Filter<P>> filters = new ArrayList<>();
        for(Integer index : checkedIndices) {
            filters.add(this.filters.get(index));
        }
        return filters;
    }
}
