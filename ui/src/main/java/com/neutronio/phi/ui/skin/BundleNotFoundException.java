package com.neutronio.phi.ui.skin;

/**
 * Thrown when a translation bundle could not be found
 */
public class BundleNotFoundException extends RuntimeException {
    public BundleNotFoundException(String bundleName) {
        super("Bundle not found: " + bundleName);
    }
}
