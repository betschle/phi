package com.neutronio.phi.ui.commons;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.neutronio.phi.app.Zoomable;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.buttons.AstraXButton;
import com.neutronio.phi.ui.commons.buttons.AstraXTextButton;
import com.neutronio.phi.ui.skin.PhiUITranslations;
import com.neutronio.phi.util.format.StandardFormats;

/**
 * Control buttons for a zoomable component.
 */
public class ZoomButtons extends Table {

    private AstraXButton zoomInButton;
    private AstraXButton zoomOutButton;
    private AstraXTextButton resetZoomButton;
    /** The controlled zoomable element. */
    private Zoomable zoomable;

    public ZoomButtons(ComponentFactory factory) {
        Skin skin = factory.getSkin();
        this.zoomInButton = new AstraXButton(factory, "icon_inline_zoom_in", "primary");
        this.zoomInButton.setSize(32, 32);
        this.zoomInButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                getZoomable().zoomIn(2f);
                event.stop();
            }
        });

        this.zoomInButton.addListener(new TextTooltip(factory.translate(PhiUITranslations.TOOLTIP_ZOOM_IN), skin));
        this.zoomInButton.setDisabled(false);
        this.zoomInButton.setCanCheck(false);

        this.resetZoomButton = new AstraXTextButton(factory, "inventory-multiplier");
        this.resetZoomButton.setCanCheck(false);
        this.resetZoomButton.setSquishable(false);
        this.resetZoomButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getZoomable().resetZoom();
                event.stop();
            }
        });
        this.resetZoomButton.addListener(new TextTooltip(factory.translate(PhiUITranslations.TOOLTIP_ZOOM_RESET), skin));
        this.resetZoomButton.setDisabled(false);
        this.resetZoomButton.setStyle( this.resetZoomButton.getStyle().copy() );
        this.resetZoomButton.getStyle().width = 60;
        this.resetZoomButton.getStyle().height = 24;

        this.zoomOutButton = new AstraXButton(factory, "icon_inline_zoom_out", "primary");
        this.zoomOutButton.setSize(32, 32);
        this.zoomOutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                getZoomable().zoomOut(2f);
                event.stop();
            }
        } );
        this.zoomOutButton.addListener(new TextTooltip(factory.translate(PhiUITranslations.TOOLTIP_ZOOM_OUT), skin));
        this.zoomOutButton.setDisabled(false);
        this.zoomOutButton.setCanCheck(false);
    }

    public Zoomable getZoomable() {
        return zoomable;
    }

    public void setZoomable(Zoomable zoomable) {
        this.zoomable = zoomable;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(this.getZoomable() == null) return;
        this.resetZoomButton.setText(StandardFormats.FACTOR.format( this.getZoomable().getCurrentZoom() ));
    }

    public void construct() {
        this.add(this.zoomOutButton).padRight(5f);
        this.add(this.resetZoomButton).padRight(5f);
        this.add(this.zoomInButton);
        this.pack();
    }
}
