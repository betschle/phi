package com.neutronio.phi.ui.commons.components;

import com.neutronio.phi.app.PanningCamera;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.CustomAction;
import com.neutronio.phi.ui.commons.ZoomButtons;
import com.neutronio.phi.ui.commons.buttons.AstraXButton;
import com.neutronio.phi.ui.commons.buttons.StaticIcon;

/**
 * Component for controlling a panning camera in a world
 */

public class PanningCameraButtons extends SimplePanel {

    /*
        Change zoom rate?
     */
    private PanningCamera camera;
    /** Resets panning offset */
    private AstraXButton resetFocusButton;
    private StaticIcon icon;
    private ZoomButtons zoomButtons;
    private CustomAction resetFocusAction;

    public PanningCameraButtons(ComponentFactory componentFactory) {
        super(componentFactory);
        this.zoomButtons = new ZoomButtons(componentFactory);
        this.zoomButtons.construct();
        this.icon = new StaticIcon(componentFactory, "secondary-32x32");
        this.icon.setInlineIcon("icon_inline_camera");
        this.icon.addListener(componentFactory.getToolTip("Camera Controls"));
        this.resetFocusButton = new AstraXButton(componentFactory, "icon_inline_center", "primary");
        this.resetFocusButton.setCanCheck(false);
        this.resetFocusAction = new CustomAction(new Runnable() {
            @Override
            public void run() {
                getCamera().resetPanning();
            }
        });
        this.resetFocusButton.addListener(componentFactory.getToolTip("Reset Focus"));
        this.resetFocusButton.setButtonAction(this.resetFocusAction);

        this.add(this.icon).padRight(10f);
        this.add(this.zoomButtons).padRight(20f);
        this.add(this.resetFocusButton);
        this.pack();
    }

    public PanningCamera getCamera() {
        return camera;
    }

    public void setPanningCamera(PanningCamera camera) {
        this.camera = camera;
        this.zoomButtons.setZoomable(this.camera);
    }
}
