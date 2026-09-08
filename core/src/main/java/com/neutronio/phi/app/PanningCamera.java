package com.neutronio.phi.app;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.neutronio.phi.PhiException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Camera with extended functions such as panning, interpolation and rumble.
 */
public class PanningCamera implements Zoomable {

    // TODO bug: zoom actually does not go lower than 0.2 if minZoom < 0.2
    private float maxZoom = 10f;
    private float minZoom = 2.5f;

    private Logger logger = Logger.getLogger(PanningCamera.class.getSimpleName());
    private float constantRumbleStrength = 0f;
    private float rumbleFactor = 0f;
    private float xDisplacement = 0f;
    private float yDisplacement = 0f;

    private Vector2 currentFocusPos = new Vector2();
    private Vector2 currentFocusPosInterpolated = new Vector2();
    private Vector2 offset = new Vector2();
    private Vector2 offsetInterpolated = new Vector2();
    private float targetZoom = 1f;
    private Actor focusedActor;
    private OrthographicCamera stageCamera;
    private OrthographicCamera backGroundCamera;
    private List<Class> filter = new ArrayList<>();

    public PanningCamera(OrthographicCamera stageCamera,
                         OrthographicCamera backGroundCamera
                         ) {
        this.stageCamera = stageCamera;
        this.backGroundCamera = backGroundCamera;
    }

    public void setMaxZoom(float maxZoom) {
        this.maxZoom = Math.abs(maxZoom);
        this.targetZoom = MathUtils.clamp(this.targetZoom + (this.maxZoom * 0.3f), this.minZoom, this.maxZoom);
    }

    public void setMinZoom(float minZoom) {
        this.minZoom = Math.abs(minZoom);
        this.targetZoom = MathUtils.clamp(this.targetZoom + (this.maxZoom * 0.3f), this.minZoom, this.maxZoom);
    }

    /**
     * Sets the filter that only allows focus on the specified classes
     * @param filter
     */
    public void setFilter(List<Class> filter) {
        this.filter = filter;
    }

    public Actor getFocusedActor() {
        return focusedActor;
    }

    public void setFocusedActor(Actor focusedTarget) {
        if( focusedTarget == null) throw new PhiException(PhiException.ErrorCode.E0004);
        if(this.filter.isEmpty()) {
            logger.fine("Focused on: " + focusedTarget.getName() );
            this.focusedActor = focusedTarget;
            this.resetPanning();
            return;
        }
        if(this.filter.contains(focusedTarget.getClass())) {
            logger.fine("Focused on: " + focusedTarget.getName() );
            this.focusedActor = focusedTarget;
            this.resetPanning();
        }

    }

    /**
     * The currently focused position, locked on to an actor or close to it
     * @return
     */
    public Vector2 getCurrentFocusPos() {
        return currentFocusPos;
    }

    /**
     * Causes a brief rumble
     * @param strength clamped between 0-10
     */
    public void rumble(float strength) {
        this.rumbleFactor += MathUtils.clamp( strength, 0f, 10f );
    }

    /**
     * Adds a constant rumble displacement until
     * the rumble is ended. Adds up to brief rumbles.
     * @param strength clamped between 0-10
     */
    public void startRumble(float strength) {
        this.constantRumbleStrength = MathUtils.clamp( strength, 0f, 10f );
    }

    /**
     * Ends the rumbling. Eren hated that.
     */
    public void endRumble() {
        this.constantRumbleStrength = 0f;
    }

    public void panCamera(float x, float y) {
        this.offset.add(x, y);
    }

    public void resetPanning() {
        this.offset.set(0, 0);
    }

    @Override
    public void zoom(float strength) {
        if(strength < 0) this.zoomOut( Math.abs(strength));
        else this.zoomIn( Math.abs(strength) );
    }

    /**
     * Zooms in the camera.
     */
    @Override
    public void zoomIn( float strength ) {
        float alpha = Interpolation.pow2.apply( 0.3f * strength);
        this.targetZoom = MathUtils.clamp(this.targetZoom - (alpha * maxZoom * 0.3f), minZoom, maxZoom);
    }

    /**
     * Zooms out the camera.
     */
    @Override
    public void zoomOut( float strength ) {
        float alpha = Interpolation.pow2.apply( 0.3f * strength);
        this.targetZoom = MathUtils.clamp(this.targetZoom + (alpha * maxZoom * 0.3f), minZoom, maxZoom);
    }

    /**
     * Resets the zoom to default.
     */
    @Override
    public void resetZoom() {
        this.targetZoom = 1f;
    }

    @Override
    public float getCurrentZoom() {
        return this.targetZoom;
    }

    /**
     * Updates the camera and applies zoom and focus tweening.
     */
    public void update() {
        this.stageCamera.zoom = this.stageCamera.zoom * 0.95f + targetZoom * 0.05f;
        this.backGroundCamera.zoom = this.stageCamera.zoom;

        if( this.constantRumbleStrength <= 0f) {
            if (this.rumbleFactor > 0) {
                this.xDisplacement = ((float) (Math.random() - 0.5f) * 10f * this.rumbleFactor) * this.stageCamera.zoom;
                this.yDisplacement = ((float) (Math.random() - 0.5f) * 10f * this.rumbleFactor) * this.stageCamera.zoom;
                this.rumbleFactor = this.rumbleFactor * 0.94f;
            }
        } else {
            // adds up brief rumbles with constant rumble
            this.xDisplacement = (((float) (Math.random() - 0.5f) * 7 * this.constantRumbleStrength) + ((float) (Math.random() - 0.5f) * this.rumbleFactor * 5)) * this.stageCamera.zoom;
            this.yDisplacement = (((float) (Math.random() - 0.5f) * 7 * this.constantRumbleStrength) + ((float) (Math.random() - 0.5f) * this.rumbleFactor * 5)) * this.stageCamera.zoom;
            if (this.rumbleFactor > 0) this.rumbleFactor = this.rumbleFactor * 0.94f;
        }

        // Cam only works with focused actor being set
        if(this.focusedActor == null) return;
        this.currentFocusPos.set(this.focusedActor.getWidth()/2f, this.focusedActor.getHeight()/2f);
        // TODO eventually this should operate on target level
//        AstraXUtil.getWorldPosition(this.currentFocusPos, this.focusedActor);

        this.currentFocusPosInterpolated.set(
                this.currentFocusPosInterpolated.x * 0.8f + this.currentFocusPos.x * 0.2f,
                this.currentFocusPosInterpolated.y * 0.8f + this.currentFocusPos.y * 0.2f
        );
        this.offsetInterpolated.set( this.offsetInterpolated.x * 0.8f + this.offset.x * 0.2f,
                this.offsetInterpolated.y * 0.8f + this.offset.y * 0.2f);

        this.stageCamera.position.x = this.currentFocusPosInterpolated.x + this.offsetInterpolated.x + this.xDisplacement;
        this.stageCamera.position.y = this.currentFocusPosInterpolated.y + this.offsetInterpolated.y + this.yDisplacement;

        this.backGroundCamera.position.x = (this.currentFocusPosInterpolated.x + this.offsetInterpolated.x + this.xDisplacement) * 0.05f;
        this.backGroundCamera.position.y = (this.currentFocusPosInterpolated.y + this.offsetInterpolated.y + this.yDisplacement) * 0.05f;
    }
}
