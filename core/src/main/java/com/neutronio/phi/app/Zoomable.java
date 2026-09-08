package com.neutronio.phi.app;

/**
 * A UI Component with zooming functionality.
 */
public interface Zoomable {
    /**
     * Zooms in or out, depending on if strength is negative or positive.
     */
    void zoom(float strength);
    /**
     * Zooms in.
     */
    void zoomIn(float strength);

    /**
     * Zooms out.
     */
    void zoomOut(float strength);

    /**
     * Resets the zoom to standard.
     */
    void resetZoom();

    float getCurrentZoom();
}
