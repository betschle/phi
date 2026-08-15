package com.neutronio.phi.util.format;

/**
 * Interface for number or date formatters.
 */
public interface Formatter {

     String format(String format, Object... values);
}
