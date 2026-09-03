package com.neutronio.phi.ui.commons.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.neutronio.phi.ui.ComponentFactory;
import com.neutronio.phi.ui.commons.Background;

/**
 * Combines any actor with a padded background frame which is drawn
 * below. Note that achieving this with the gdx Stack class is not possible.
 */
public class Avatar extends Table {

    private ComponentFactory factory;
    private Actor avatar;
    private Background background;

    public Avatar(ComponentFactory componentFactory) {
        super();
        this.factory = componentFactory;
        this.background = new Background( componentFactory.getSkin() );
    }

    /**
     *
     * @param actor the actor to use as avatar
     * @param width how large the avatar is being drawn
     * @param height how large the avatar is being drawn
     */
    public void setAvatar(Actor actor, int width, int height) {
        this.clearChildren();
        this.avatar = actor;
        this.avatar.setTouchable(Touchable.disabled);
        this.add(avatar).size(width, height);
        this.pack();
    }

    public void setBackground(String backgroundStyle) {
        this.background.setStyle( factory.getSkin().get(backgroundStyle, Background.BackgroundStyle.class));
        this.pad(this.background.getStyle().padding);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if( this.background != null) {
            this.background.setColor(this.background.getCurrentColor().r,
                    this.background.getCurrentColor().g,
                    this.background.getCurrentColor().b,
                    this.background.getCurrentColor().a * parentAlpha);
            this.background.setSize(getWidth(), getHeight());
            this.background.setPosition(getX(), getY());
            this.background.draw(batch, parentAlpha);
        }
        super.draw(batch, parentAlpha);
    }
}
