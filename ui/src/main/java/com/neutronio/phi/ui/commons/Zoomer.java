package com.neutronio.phi.ui.commons;

import com.badlogic.gdx.math.MathUtils;
import com.neutronio.phi.app.Zoomable;

/**
 * A general purpose, configurable, non-libgdx zooming mechanism
 * that keeps track of scale. Features clamping & tweening. Meant for
 * UI navigation maps.
 */
public class Zoomer implements Zoomable {

    // TODO improve handling of values here. Instead of configuring all fields manually, use a number scale factor to multiply
    // all values with
    private float maxScale = 0.2f;
    private float minScale = 0.005f;
    private float initialScale = 0.01f;
    private float scale = initialScale;
    private float targetScale = initialScale;
    private float scaleDelta = initialScale / 10f;

    public Zoomer() {
    }

    public Zoomer(float initialScale, float minScale, float maxScale) {
        this.initialScale = initialScale;
        this.targetScale = initialScale;
        this.scale = initialScale;
        this.maxScale = maxScale;
        this.minScale = minScale;
        this.scaleDelta =  (minScale / maxScale) * 3;
    }

    @Override
    public void zoom(float strength) {
        if( strength < 0) this.zoomOut( Math.abs(strength));
        else this.zoomIn( Math.abs(strength) );
    }

    @Override
    public void zoomIn(float strength) {
        // float alpha = Interpolation.pow2.apply( (this.scaleDelta * strength) );
        // this.targetScale = MathUtils.clamp( this.scale + alpha, minScale, maxScale);
        this.targetScale = MathUtils.clamp( this.scale + (this.scaleDelta * strength), minScale, maxScale);
    }

    @Override
    public void zoomOut(float strength) {
        // float alpha = Interpolation.pow2.apply( (this.scaleDelta * strength) );
        // this.targetScale = MathUtils.clamp( this.scale - alpha, minScale, maxScale);
        this.targetScale = MathUtils.clamp( this.scale - (this.scaleDelta * strength), minScale, maxScale);
    }

    @Override
    public void resetZoom() {
        this.targetScale = this.initialScale;
    }

    @Override
    public float getCurrentZoom() {
        return this.scale;
    }

    /**
     * Gets the currently saved scale
     * @return
     */
    public float getScale() {
        return scale;
    }

    public void update(float delta) {
        this.scale = MathUtils.lerp( this.scale, this.targetScale, 10f * delta );
    }
}
