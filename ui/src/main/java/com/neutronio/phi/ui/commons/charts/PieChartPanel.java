package com.neutronio.phi.ui.commons.charts;

import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.components.SimplePanel;
import com.neutronio.phi.ui.commons.text.AstraXLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * Panel with a pie chart and a legend
 */
public class PieChartPanel extends SimplePanel {

    protected AstraXLabel title;
    protected PieChart pieChart;
    protected ChartLegend chartLegend;

    public static class Model {
        public String title;
        public List<PieChart.Column> data = new ArrayList<>();
    }
    /**
     *
     * @param componentFactory
     */
    public PieChartPanel(ComponentFactory componentFactory) {
        super(componentFactory);
        this.title = getHeader2("");
        this.pieChart = new PieChart();
        this.pieChart.setType(PieChart.Type.PIE);
        this.chartLegend = new ChartLegend(componentFactory);
        this.add(this.title).colspan(2).padBottom(10).row();
        this.add(this.pieChart).padRight(10);
        this.add(this.chartLegend);
        this.pack();
    }

    public void setModel(Model model) {
        this.setTitle(model.title);
        this.pieChart.setData(model.data);
        this.chartLegend.setData(model.data);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setAsPie() {
        this.pieChart.setType(PieChart.Type.PIE);
    }

    public void setAsArc(float arcWidth) {
        this.pieChart.setType(PieChart.Type.ARC);
        this.pieChart.setArcWidth(arcWidth);
    }

    public void setPieRadius(float radius) {
        this.pieChart.setRadius(radius);
        this.pack();
    }

    public void setData(List<PieChart.Column> data) {
        this.pieChart.setData(data);
        this.chartLegend.setData(data);
        this.pack();
    }

}
