package com.neutronio.phi.gfx;

/**
 * Allows for using Shape Rendering on UI Components and Graphics.
 * Classes inheriting this must be Actor or Groups, and also member of the
 * GDX Stage to work.
 */
public interface AdvancedShapeRendering {
    /**
     * @param graphics The shapes object that was obtained from the stage this actor is attached to.
     *                 AstraXStage == actor.getStage()
     *                 AstraXStage.graphics = parameter of this method
     */
    void setGraphics(AdvancedShapes graphics);
}
