package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.neutronio.phi.app.Message;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.Background;

/**
 * A group of Alerts that stack. Setting the size is important for layouting
 */
public class AlertStack extends Table {

    // TODO use new tweening here and init properly with the right sizes
    protected ComponentFactory componentFactory;
    private AlertStackStyle style;
    private Alert[] alerts;
    private float showDuration = 5f;

    public static class AlertStackStyle {

        /**A success style }*/
        public IconLabel.IconLabelStyle baseIconLabelStyle;
        public Drawable background;

        /////////////
        /**A success style */
        public IconLabel.IconLabelStyle successIconLabelStyle;
        /**A info style  */
        public IconLabel.IconLabelStyle infoIconLabelStyle;
        /**A warning style */
        public IconLabel.IconLabelStyle warningIconLabelStyle;
        /**An error style */
        public IconLabel.IconLabelStyle errorIconLabelStyle;
        public String align;
    }

    public AlertStack(ComponentFactory componentFactory, int size) {
        this(componentFactory, "compact", size);
    }

    public AlertStack(ComponentFactory componentFactory, String style, int size) {
        super(componentFactory.getSkin());
        this.componentFactory = componentFactory;
        this.style = componentFactory.getSkin().get(style, AlertStackStyle.class);

        // invisible separator to retain size
        Separator separator = new Separator(componentFactory.getSkin(), "background_para");
        separator.setTouchable(Touchable.disabled);
        this.add().width(400).height(1).row();

        this.alerts = new Alert[size];
        for(int i = 0; i < this.alerts.length; i++) {
            this.alerts[i] = new Alert(componentFactory, this.style.infoIconLabelStyle);
            this.alerts[i].setVisible(false);
            switch( this.style.align ) {
                case "center":
                    this.add(this.alerts[i])
                            .padBottom(2)
                            .padTop(2)
                            .minHeight(42)
                            .minWidth(this.alerts[i].getStyle().labelMinWidth)
                            .center().row();
                    break;
                case "left":
                    this.add(this.alerts[i])
                            .padBottom(2)
                            .padTop(2)
                            .minHeight(42)
                            .minWidth(this.alerts[i].getStyle().labelMinWidth)
                            .left().row();
                    break;
                case "right":
                    this.add(this.alerts[i])
                            .padBottom(2)
                            .padTop(2)
                            .minHeight(42)
                            .minWidth(this.alerts[i].getStyle().labelMinWidth)
                            .right().row();
                    break;
            }
            this.alerts[i].addAction(Actions.alpha(0f, 0.001f));
        }
        this.pack();
    }

    public void setStyle(AlertStackStyle style) {
        this.style = style;
        for( Alert alert : this.alerts) {
            alert.setStyle(style.infoIconLabelStyle);
        }
    }

    /**
     * Shows a message with the respective style.
     * @param message
     */
    public void showMessage(Message message) {
        switch ( message.getType() ) {
            case SUCCESS:  this.showSuccessGrowl(this.componentFactory.translateMarked(message.getMessage())); break;
            case INFO: this.showInfoGrowl(this.componentFactory.translateMarked(message.getMessage())); break;
            case WARNING: this.showWarningGrowl(this.componentFactory.translateMarked(message.getMessage())); break;
            case ALERT: this.showErrorGrowl(this.componentFactory.translateMarked(message.getMessage())); break;
            case EXCEPTION: this.showErrorGrowl(this.componentFactory.translateMarked(message.getMessage())); break;
        }
    }
    /**
     * Sets the showing duration of all growls.
     * @param showDuration
     */
    public void setShowDuration(float showDuration) {
        this.showDuration = showDuration;
    }

    /**
     * Gets the next usable growl that is in finished state
     * @return may return null if all the growls are busy
     */
    private Alert getNextGrowl() {
        for( Alert alert : this.alerts) {
            if( alert.getColor().a == 0 && !alert.hasActions()) {
                return alert;
            }
        }
        return null;
    }

    public void showInfoGrowl(String text) {
        Alert alert = getNextGrowl();
        if( alert != null) {
            alert.setStyle(style.infoIconLabelStyle);
            alert.setBackgroundStyle( componentFactory.getSkin().get("info", Background.BackgroundStyle.class) );
            alert.setText(text);
            alert.show(this.showDuration);
            this.pack();
        }
    }

    public void showSuccessGrowl(String text) {
        Alert alert = getNextGrowl();
        if( alert != null) {
            alert.setStyle(style.successIconLabelStyle);
            alert.setBackgroundStyle( componentFactory.getSkin().get("success", Background.BackgroundStyle.class) );
            alert.setText(text);
            alert.squishIcon();
            alert.show(this.showDuration);
            this.pack();
        }
    }

    public void showWarningGrowl(String text) {
        Alert alert = getNextGrowl();
        if( alert != null) {
            alert.setStyle(style.warningIconLabelStyle);
            alert.setBackgroundStyle( componentFactory.getSkin().get("warning", Background.BackgroundStyle.class) );
            alert.setText(text);
            alert.squishIcon();
            alert.show(this.showDuration);
            this.pack();
        }
    }

    public void showErrorGrowl(String text) {
        Alert alert = getNextGrowl();
        if( alert != null) {
            alert.setStyle(style.errorIconLabelStyle);
            alert.setBackgroundStyle( componentFactory.getSkin().get("error", Background.BackgroundStyle.class) );
            alert.setText(text);
            alert.squishIcon();
            alert.show(this.showDuration);
            this.pack();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
