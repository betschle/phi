package com.neutronio.phi.ui.commons.charts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.neutronio.phi.gfx.AdvancedShapeRendering;
import com.neutronio.phi.gfx.AdvancedShapes;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.List;

/**
 * A radial pie chart
 */
public class PieChart extends Group
                      implements AdvancedShapeRendering {
    // TODO add AdvancedShapeRendering to core
    protected AdvancedShapes advancedShapes;
    private List<Column> data = new ArrayList<>();
    /** Radius of the chart. */
    private float radius = 240;
    /** Width of the drawn arc, if type is of ARC */
    private float arcWidth = 100;
    /** Type of chart: Pie for full circle, Arc for an arc chart*/
    private Type type = Type.PIE;
    /** The extent of circle to be dawn, Pi * 2 for full circle, Pi for semi circle */
    private float extent = (float) (Math.PI * 2f);

    public enum Type {
        PIE,
        ARC
    }

    public static class Column {
        public String name;
        public float factorPercent;
        public Color color;

        /**
         *
         * @param name
         * @param factorPercent 0-1
         * @param color
         */
        public Column(String name, float factorPercent, Color color) {
            this.name = name;
            this.factorPercent = MathUtils.clamp(factorPercent, 0, 1);
            this.color = color;
        }
    }

    public PieChart() {
        this.setRadius(this.radius);
    }

    @Override
    public void setGraphics(AdvancedShapes graphics) {
        this.advancedShapes = graphics;
    }

    public void setData(List<Column> data) {
        this.data = data;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        this.setSize(radius*2 + this.arcWidth, radius*2 + this.arcWidth);
    }

    /**
     * Sets the width of the arc to be drawn. For
     * ARC piechart types
     * @param arcWidth default is 100
     */
    public void setArcWidth(float arcWidth) {
        this.arcWidth = arcWidth;
    }

    /**
     * Sets the type of pie chart
     * @param type
     */
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        super.drawChildren(batch, parentAlpha);
        this.render(this.advancedShapes.shapeDrawer, parentAlpha);
    }

    private void render(ShapeDrawer shapeDrawer, float parentAlpha) {
        if(this.data.isEmpty()) {
            shapeDrawer.setColor(Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, parentAlpha);
            shapeDrawer.circle(getWidth()/2f, getHeight()/2f, this.radius);
            return;
        }
        float currentRadians = 0;
        switch (this.type) {
            case PIE:
                for (Column column : this.data) {
                    shapeDrawer.setColor(column.color.r, column.color.g, column.color.b, parentAlpha);
                    shapeDrawer.sector(getWidth()/2f, getHeight()/2f, this.radius, currentRadians, column.factorPercent * this.extent);
                    currentRadians = currentRadians + column.factorPercent * this.extent;
                }
                break;
            case ARC:
                shapeDrawer.setDefaultLineWidth(this.arcWidth);
                for (Column column : this.data) {
                    shapeDrawer.setColor(column.color.r, column.color.g, column.color.b, parentAlpha);
                    shapeDrawer.arc(getWidth()/2f, getHeight()/2f, this.radius, currentRadians, column.factorPercent * this.extent);
                    currentRadians = currentRadians + column.factorPercent * this.extent;
                }
                break;
        }
    }

}
