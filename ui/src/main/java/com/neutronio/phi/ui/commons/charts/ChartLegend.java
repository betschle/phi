package com.neutronio.phi.ui.commons.charts;

import com.badlogic.gdx.utils.Align;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.components.IconLabel;
import com.neutronio.phi.ui.commons.components.SimplePanel;

import java.util.ArrayList;
import java.util.List;

/**
 * A legend for a chart or diagram
 */
public class ChartLegend extends SimplePanel {
    protected String format = "%s %,.1f %%";
    protected List<PieChart.Column> data = new ArrayList<>();

    public ChartLegend(ComponentFactory componentFactory) {
        super(componentFactory);
    }

    public void setData(List<PieChart.Column> data) {
        this.data = data;
        this.clearChildren();
        for(PieChart.Column column : data) {
            IconLabel iconLabel = new IconLabel(this.componentFactory, "clear-28x28");
            iconLabel.setIcon("background_rect_icon");
            iconLabel.setIconColor(column.color);
            iconLabel.setText(String.format(this.format, column.name, column.factorPercent*100));
            iconLabel.align(Align.right);
            this.addValue(iconLabel).left();
            this.row();
        }
        this.pack();
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
