package com.neutronio.phi.app;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.SnapshotArray;
import com.neutronio.phi.gfx.AdvancedShapes;

/**
 * A GDX stage wrapping some shape renderer objects within it.
 */
public class PhiStage extends Stage {
    protected AdvancedShapes advancedShapes;

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

    public PhiStage(TextureRegion textureRegion, Skin skin) {
        this.advancedShapes = new AdvancedShapes(this, textureRegion, skin);
    }

    public AdvancedShapes getAdvancedShapes() {
        return this.advancedShapes;
    }

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
        this.initShape(actor);
    }

    /**
     * Initializes Advanced shape rendering recursively.
     * This is not an ideal solution, but still the most convenient considering all
     * other options. It will not work if Actor B is added to a Group X,
     * while Group X has been already added to the stage (this skips this call entirely).
     * This is a temporary solution until other practical solutions have been found and tested
     * @param actor
     */
    private void initShape(Actor actor) {
        if(actor instanceof AdvancedShapeRendering) {
            AdvancedShapeRendering rendering = (AdvancedShapeRendering) actor;
            rendering.setGraphics(this.advancedShapes);
        }
        if(actor instanceof Group) {
            SnapshotArray<Actor> children = ((Group) actor).getChildren();
            for (Actor child : children) {
                this.initShape(child);
            }
        }
    }
}
