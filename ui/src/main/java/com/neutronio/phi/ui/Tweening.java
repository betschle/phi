package com.neutronio.phi.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

/**
 * Collection of frequently used tweening actions.
 */
public class Tweening {
    // TODO when switching Screens, let the button finish tweening, then switch the screen!
    // TODO use libgdx Actions for this
    /**
     * Standard growl fade out
     * @param shopDuration the show/resting duration. 5 is a good value.
     * @return
     */
    public static Action getGrowlAnimation(float shopDuration) {
        return Actions.sequence(
            Actions.show(),
            Actions.touchable(Touchable.enabled),
            Actions.alpha(1f, 0.1f),
            getRumbleX(0.1f, 6f),
            Actions.delay(shopDuration),
            Actions.alpha(0f, 1f),
            Actions.touchable(Touchable.disabled),
            Actions.hide()
        );
    }

    /**
     * Fades out and removes actor from parent.
     * @return
     */
    public static Action getSystemDetachAnimation() {
        return Actions.sequence( Actions.fadeOut(2f), Actions.removeActor() );
    }

    /**
     * Changes visibility to true and fades it in.
     * @return
     */
    public static Action getSystemAttachAnimation() {
        return Actions.sequence(Actions.alpha(0), Actions.visible(true), Actions.fadeIn(2f)  );
    }

    /**
     * A simple, brief scale rumble.
     * @return
     */
    public static Action getScaleRumble() {
        return Actions.repeat( 2,
            Actions.sequence(
                Actions.scaleBy(0.5f, 0.5f, 0.1f ),
                Actions.scaleBy(-0.5f, -0.5f, 0.1f)  ) );
    }


    /**
     * A simple, brief scale rumble.
     * @return
     */
    public static Action getScaleRumble(float amount) {
        return getScaleRumble(amount, 0.2f);
    }

    /**
     * A simple, brief scale rumble.
     * @return
     */
    public static Action getScaleRumble(float amount, float duration) {
        return Actions.repeat( 2,
            Actions.sequence(
                Actions.scaleBy(amount, amount, duration/2f ),
                Actions.scaleTo(1, 1, duration/2f)  ) );
    }

    /**
     * Squishes the actor on x/y axes and then wobbles a little. After tweening
     * executes the custom action.
     * @return
     */
    public static Action getSquish(float strength, CustomAction customAction) {
        strength = MathUtils.clamp(strength, 0, 1f);
        if( customAction != null) {
            return Actions.sequence(
                Actions.scaleBy(-0.1f, -0.1f, 0.05f, Interpolation.smooth),
                Actions.scaleBy(0.2f * strength, -0.1f, 0.07f, Interpolation.smooth),
                Actions.scaleTo(1, 1, 0.1f, Interpolation.smooth),
                Actions.scaleBy(-0.1f, -0.1f, 0.05f, Interpolation.smooth),
                Actions.scaleBy(0.1f, 0.1f, 0.05f, Interpolation.smooth),
                customAction
            );
        } else {
            return Actions.sequence(
                Actions.scaleBy(-0.1f, -0.1f, 0.05f, Interpolation.smooth),
                Actions.scaleBy(0.2f * strength, -0.1f, 0.07f, Interpolation.smooth),
                Actions.scaleTo(1, 1, 0.1f, Interpolation.smooth),
                Actions.scaleBy(-0.1f, -0.1f, 0.05f, Interpolation.smooth),
                Actions.scaleBy(0.1f, 0.1f, 0.05f, Interpolation.smooth)
            );
        }
    }

    /**
     * Squishes the actor on x/y axes and then wobbles a little.
     * @return
     */
    public static Action getSquish(float strength) {
        strength = MathUtils.clamp(strength, 0, 2f);
        return Actions.sequence(
            Actions.scaleBy(-0.1f, -0.1f, 0.05f, Interpolation.smooth),
            Actions.scaleBy(0.2f * strength, -0.1f, 0.07f, Interpolation.smooth),
            Actions.scaleTo(1, 1, 0.1f, Interpolation.smooth),
            Actions.scaleBy(-0.1f, -0.1f, 0.05f, Interpolation.smooth),
            Actions.scaleBy(0.1f, 0.1f, 0.05f, Interpolation.smooth)
        );
    }

    public static Action getSlowInfiniteBounce() {
        return Actions.forever(
            Actions.sequence(
                Actions.moveBy(0, -20, 0.8f ),
                Actions.moveBy(0, 20, 0.2f)  ) );
    }

    public static Action getInfiniteScaleBounce() {
        return Actions.forever(
            Actions.sequence(
                Actions.scaleBy(0.5f, 0.5f, 0.8f ),
                Actions.scaleBy(-0.5f, -0.5f, 0.2f)  ) );
    }

    /**
     * This does not work for buttons for some reason.
     * @param times
     * @param duration speed variance as factor 0-1 deviating from normal speed
     * @param scaleExtent the extent of the scale bounce
     * @return
     */
    public static Action getScaleBounce(int times, float scaleExtent, float duration) {
        return Actions.repeat( times,
            Actions.sequence(
                Actions.scaleBy(scaleExtent, scaleExtent, 0.8f * duration, Interpolation.pow2 ),
                Actions.scaleBy(-scaleExtent, -scaleExtent, 0.2f * duration)  ) );
    }

    /**
     * Simple, brief 2x Rumble on Y axis
     * @param duration default 0.1
     * @return
     */
    public static Action getRumbleY(float duration) {
        return Actions.repeat( 2,
            Actions.sequence(
                Actions.moveBy(0, 6, duration ),
                Actions.moveBy(0, -12, duration),
                Actions.moveBy(0, 6, duration)
            ) );
    }

    /**
     * Simple, brief 2x Rumble on Y axis
     * @param duration default 0.1
     * @return
     */
    public static Action getRumbleY(float duration, int pixels) {
        return Actions.repeat( 2,
            Actions.sequence(
                Actions.moveBy(0, pixels, duration ),
                Actions.moveBy(0, -pixels*2, duration),
                Actions.moveBy(0, pixels, duration)
            ) );
    }

    /**
     * Simple, brief 2x Rumble on X axis
     * @param duration default 0.1
     * @param pixels default 6
     * @return
     */
    public static Action getRumbleX(float duration, float pixels) {
        return Actions.repeat( 2,
            Actions.sequence(
                Actions.moveBy(pixels, 0, duration ),
                Actions.moveBy(-pixels*2, 0, duration),
                Actions.moveBy(pixels, 0, duration)
            ) );
    }

    public static Action getAsteroidCrackRumbleXY(float startPosX, float startPosY) {
        return Actions.sequence(
            Actions.moveBy(MathUtils.random(-1f, 1f) * 4, MathUtils.random(-1f, 1f) * 4, 0.05f),
            Actions.moveBy(MathUtils.random(-1f, 1f) * 3, MathUtils.random(-1f, 1f) * 3, 0.05f),
            Actions.moveBy(MathUtils.random(-1f, 1f) * 2, MathUtils.random(-1f, 1f) * 2, 0.05f),
            Actions.moveBy(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f), 0.05f),
            Actions.moveTo(startPosX, startPosY, 0.1f)
        );
    }

    /**
     * Simple Dropdown fade out tweening.
     * @return
     */
    public static Action getDropDownFadeOut(float duration) {
        return Actions.sequence(
            Actions.parallel(
                // Actions.moveBy(0, 100, duration),
                Actions.fadeOut(duration),
                Actions.touchable(Touchable.disabled) ),
            Actions.hide(),
            Actions.alpha(0f) // ensure that hidden
        );
    }

    /**
     * Simple Dropdown fade in tweening.
     * @return
     */
    public static Action getDropDownFadeIn(float duration) {
        return Actions.sequence(
            Actions.moveBy(0, 100), // instant movement to slide in right after
            Actions.alpha(0f), // ensure that invisible
            Actions.show(),
            Actions.parallel(
                Actions.moveBy(0, -100, duration, Interpolation.pow2),
                Actions.touchable(Touchable.enabled),
                Actions.fadeIn(duration) )
        );
    }

    /**
     * Simple slide out fade out tweening.
     * @return
     */
    public static Action getSlideFadeOut(float duration, float moveToX, float moveToY) {
        return Actions.sequence(
            Actions.alpha(1f), // ensure alpha 1 for fade out
            Actions.show(), // ensure visible
            Actions.parallel(
                Actions.moveTo(moveToX, moveToY, duration, Interpolation.pow2),
                Actions.fadeOut(duration),
                Actions.touchable(Touchable.disabled) ),
            Actions.hide() // ensure hidden
        );
    }

    /**
     * Simple slide in fade in tweening.
     * @return
     */
    public static Action getSlideFadeIn(float duration, float moveToX, float moveToY) {
        return Actions.sequence(
            Actions.alpha(0f), // ensure alpha 0 for fade in
            Actions.show(), // ensure visible
            Actions.parallel(
                Actions.moveTo(moveToX, moveToY, duration),
                Actions.fadeIn(duration),
                Actions.touchable(Touchable.enabled) )
        );
    }

    /**
     * Simple slide out fade out tweening that appends a custom action towards the end.
     * @return
     */
    public static Action getSlideFadeOutWithAction(float duration, float moveToX, float moveToY, CustomAction customAction) {
        return Actions.sequence(getSlideFadeOut(duration, moveToX, moveToY), customAction);
    }

    /**
     * Simple slide in fade in tweening that appends a custom action towards the end.
     * @return
     */
    public static Action getSlideFadeInWithAction(float duration, float moveToX, float moveToY, CustomAction customAction) {
        return Actions.sequence(getSlideFadeIn(duration, moveToX, moveToY), customAction);
    }


    /**
     * More complex fade out + rumble for UI component tweening.
     * @return
     */
    public static Action getDefaultFadeOut(float duration) {
        return Actions.sequence(
            Actions.parallel( getRumbleY(0.1f), Actions.fadeOut(duration) ),
            Actions.hide(),
            Actions.touchable(Touchable.disabled)
        );
    }

    /**
     * More complex fade in + rumble for UI component tweening.
     * @return
     */
    public static Action getDefaultFadeIn(float duration) {
        return Actions.sequence(
            Actions.touchable(Touchable.enabled),
            Actions.show(),
            Actions.parallel( getRumbleY(0.1f), Actions.fadeIn(duration) )
        );
    }

    /**
     * Simple fade out for tweening.
     * @return
     */
    public static Action getSimpleFadeOut(float duration) {
        return Actions.sequence(
            Actions.fadeOut(duration),
            Actions.hide(),
            Actions.touchable(Touchable.disabled)
        );
    }

    /**
     * Simple fade in for tweening.
     * @return
     */
    public static Action getSimpleFadeIn(float duration) {
        return Actions.sequence(
            Actions.touchable(Touchable.enabled),
            Actions.show(),
            Actions.fadeIn(duration)
        );
    }

    /**
     * Fades in any component from [X center, Y offscreen] to the
     * center of the screen. Comes from top & does a little rumble once it rests at the center.
     * @return
     */
    public static Action getCenteredFadeIn() {
        return Actions.sequence(
            Actions.moveToAligned(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight(), Align.center ),
            Actions.visible(true),
            Actions.parallel(Actions.fadeIn(0.2f), Actions.moveToAligned(
                Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, Align.center,
                0.5f, Interpolation.swing)),
            // rumble
            Actions.moveBy(-6, 0, 0.05f),
            Actions.moveBy(6, 0, 0.05f),
            Actions.moveBy(-6, 0, 0.05f),
            Actions.moveBy(6, 0, 0.05f),
            Actions.moveBy(-3, 0, 0.05f)
        );
    }

    /**
     * Fades out any component from the center of the screen, to [X center, Y offscreen], then
     * sets the component to invisible. Fades out into the top, offscreen.
     * @return
     */
    public static Action getCenteredFadeOut() {
        return Actions.sequence(
            Actions.parallel(Actions.fadeOut(0.4f), Actions.moveToAligned(
                Gdx.graphics.getWidth()/2f, -Gdx.graphics.getHeight(),
                Align.center, 0.8f, Interpolation.swing)),
            Actions.visible(false)
        );
    }

    /**
     * Fade out Action for the {@link StackController} mechanism.
     * @return
     */
    public static Action getStackControllerFadeOut() {
        return Actions.sequence(
            Actions.parallel(
                Actions.moveTo(-Gdx.graphics.getWidth(), 0, 0.4f, Interpolation.pow2),
                Actions.fadeOut(0.1f) ),
            Actions.visible(false));
    }

    /**
     * Fade out Action for the {@link StackController} mechanism.
     * @return
     */
    public static Action getStackControllerFadeIn() {
        return Actions.sequence(
            Actions.visible(true),
            Actions.parallel(
                Actions.fadeIn(0.1f),
                Actions.moveTo(0, 0, 0.4f, Interpolation.pow2) ));
    }
}

