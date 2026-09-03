package com.neutronio.phi.ui.commons;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.neutronio.phi.ui.Tweening;

import java.util.Objects;

/**
 * Controls multiple stacked elements, ensures one is selected and fades in/out the other on change
 */
public class StackController {

    /**
     * Defines behavior if index exceeds size or goes below 0.
     * If true, applies cyclic behavior, otherwise clamps index between
     * 0 and size-1.
     */
    private boolean isCyclic = true;
    private int currentIndex = 0;
    private Actor currentContent = null;
    private Stack contentStack;

    /**
     * I cannot reuse/restart gdx actions so they need to be instantiated every time, hence
     * the customizable producer interface as little workaround.
     * Thank the gdx maintainers for this mess.
     */
    protected ActionProducer showActionProducer;
    /**
     * I cannot reuse/restart gdx actions so they need to be instantiated every time, hence
     * the customizable producer interface as little workaround.
     * Thank the gdx maintainers for this mess.
     */
    protected ActionProducer hideActionProducer;

    public interface ActionProducer {
        Action createAction();
    }

    /**
     * If children are added to Stack after creation of this StackController,
     * added children must be set to be invisible
     * @param stack
     */
    public StackController(Stack stack) {
        this.contentStack = stack;
        this.showActionProducer = new ActionProducer() {
            @Override
            public Action createAction() {
                return Tweening.getStackControllerFadeIn();
            }
        };
        this.hideActionProducer = new ActionProducer() {
            @Override
            public Action createAction() {
                return Tweening.getStackControllerFadeOut();
            }
        };

        if( this.contentStack.getChildren().size > 0) {
            for (Actor actor : this.contentStack.getChildren()) {
                actor.setVisible(false);
            }
            this.show(this.contentStack.getChildren().get(0));
        }
    }

    public void setShowActionProducer(ActionProducer showActionProducer) {
        this.showActionProducer = showActionProducer;
    }

    public void setHideActionProducer(ActionProducer hideActionProducer) {
        this.hideActionProducer = hideActionProducer;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * @return Returns cyclic behavior. If true, the controller clamps index between
     * 0 and size-1.
     */
    public boolean isCyclic() {
        return isCyclic;
    }

    /**
     * @param cyclic Defines behavior if index exceeds size or goes below 0.
     * If true, applies cyclic behavior, otherwise clamps index between
     * 0 and size-1.
     */
    public void setCyclic(boolean cyclic) {
        isCyclic = cyclic;
    }

    public Actor getCurrentContent() {
        return currentContent;
    }

    /**
     *
     * @return true if stack controller is displaying the first item of its contents.
     */
    public boolean isShowingFirst() {
        return this.currentIndex == 0;
    }

    /**
     *
     * @return true if stack controller is displaying the last item of its contents.
     */
    public boolean isShowingLast() {
        return this.currentIndex == this.contentStack.getChildren().size-1;
    }

    /**
     * Shows the next content in this stack.
     */
    public void next() {
        // TODO I could use Selector for this
        // the logic here works but is not very pretty. Optimize?
        this.currentIndex++;
        if( this.currentIndex >= this.contentStack.getChildren().size) {
            if( this.isCyclic ) {
                this.currentIndex = 0;
            } else {
                this.currentIndex = this.contentStack.getChildren().size-1;
            }
        }
        this.show( this.currentIndex );
    }

    /**
     * Shows the previous content in this stack.
     */
    public void previous() {
        // TODO I could use Selector for this
        // the logic here works but is not very pretty. Optimize?
        this.currentIndex--;
        if( this.currentIndex < 0) {
            if( this.isCyclic ) {
                this.currentIndex = this.contentStack.getChildren().size - 1;
            } else {
                this.currentIndex = 0;
            }
        }
        this.show( this.currentIndex );
    }

    /**
     * Shows content by its class. Gives the first occurrence of
     * class in the stack.
     * @param contentType
     * @return index of shown content, -1 if none was found
     */
    public int show(Class contentType) {
        int i = 0;
        for( Actor actor : this.contentStack.getChildren()) {
            if( Objects.equals( actor.getClass(), contentType) ) {
                this.currentIndex = i;
                this.show(this.contentStack.getChildren().get(i));
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * Shows content by its index.
     * @param index
     * @return index of shown content, -1 if none was found
     */
    public int show(int index) {
        int i = 0;
        for( Actor actor : this.contentStack.getChildren()) {
            if( i == index) {
                this.currentIndex = i;
                this.show(actor);
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * Shows content by actor. Actor must be part of the stack for this to work.
     * @param content
     */
    public int show(Actor content) {
        // no change if actor already shown
        if( Objects.equals(this.currentContent , content)) return this.currentIndex;
        int index = this.contentStack.getChildren().indexOf(content, true);
        if( this.currentContent != null) {
            if( contentStack.isVisible() && !this.currentContent.hasActions() ) {
                this.currentContent.addAction( this.hideActionProducer.createAction() );
            } else {
                // TODO acts funny sometimes, becuause this.currentContent.removeAction( ); is missing?
                this.currentContent.setVisible(false);
            }
            this.currentContent = null;
        }
        if( content != null && !content.hasActions()) {
            if( contentStack.isVisible() ) {
                content.addAction( this.showActionProducer.createAction() );
            } else {
                content.setVisible(true);
            }
            this.currentContent = content;
        }
        return index;
    }

    public void hide() {
        if( contentStack.isVisible() ) {
            this.currentContent.addAction( this.hideActionProducer.createAction() );
        }
    }
}
