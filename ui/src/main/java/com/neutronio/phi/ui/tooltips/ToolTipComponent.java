package com.neutronio.phi.ui.tooltips;

/**
 * Components that can be called as tooltip should implement this interface.
 * @param <D>
 */
public interface ToolTipComponent<D extends Object> {
    void applyData(D data);
    void clearData();
}
