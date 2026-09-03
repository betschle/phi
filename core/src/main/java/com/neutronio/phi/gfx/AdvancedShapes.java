package com.neutronio.phi.gfx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * Advanced/particular shapes for rendering.
 * If one gets an IllegalStateException with the message
 * "SpriteBatch.begin must be called before draw." that means
 * the shapes are not drawn on the stage they were created with.
 * The batch must be the same!
 */
public class AdvancedShapes {
    // make this a map of graphics
    public final ShapeDrawer shapeDrawer;
//    public final ShapeTargetor shapeTargetor;
//    public final CircleBar circleBarRenderer;

    public AdvancedShapes(Stage targetStage, TextureRegion textureRegion, Skin skin) {
        this.shapeDrawer = new ShapeDrawer(targetStage.getBatch(), textureRegion);
//        this.shapeTargetor = new ShapeTargetor(skin);
//        this.circleBarRenderer = new CircleBar(skin);
        // this bypasses the auto adding algo in stage but fack it
//        this.shapeTargetor.setGraphics(this);
//        this.circleBarRenderer.setGraphics(this);
//        this.shapeTargetor.setGraphics(this);
    }
}
