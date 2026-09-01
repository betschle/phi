package com.neutronio.phi.ui.commons.buttons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Base class for Button Selection Groups
 * @param <T>
 */
public abstract class AbstractButtonSelection<T extends Button> implements ButtonSelection<T>{

    protected List<T> buttons = new ArrayList<>();

    @Override
    public List<T> getButtons() {
        return this.buttons;
    }

    @Override
    public void add(T... buttons) {
        if (buttons == null) throw new IllegalArgumentException("buttons cannot be null.");
        for (int i = 0, n = buttons.length; i < n; i++)
            add(buttons[i]);
    }

    @Override
    public void addAll(Collection<T> buttons) {
        if (buttons == null) throw new IllegalArgumentException("buttons cannot be null.");
        for (T button : buttons)
            add(button);
    }

    @Override
    public void remove(T button) {
        if (button == null) throw new IllegalArgumentException("button cannot be null.");
        button.setButtonGroup(this);
        buttons.remove(button);
    }

    @Override
    public void remove(T... buttons) {
        if (buttons == null) throw new IllegalArgumentException("buttons cannot be null.");
        for (int i = 0, n = buttons.length; i < n; i++)
            remove(buttons[i]);
    }

    @Override
    public void clear() {
        this.buttons.clear();
    }
}
