package com.neutronio.phi.lang;

import com.neutronio.phi.PhiException;

/**
 * Thrown if a translation File could not be found. This is a non-critical error, that is, if it
 * doesn't affect the base bundle.
 */
public class TranslationFileNotFoundException extends PhiException {

    public TranslationFileNotFoundException(String fileName, Throwable cause) {
        super(fileName, cause);
    }
}
